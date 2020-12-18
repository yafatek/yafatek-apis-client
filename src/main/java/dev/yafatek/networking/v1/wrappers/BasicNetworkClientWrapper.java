package dev.yafatek.networking.v1.wrappers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dev.yafatek.networking.v1.api.NetworkClient;
import dev.yafatek.networking.v1.models.ApiResponse;
import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

/**
 * for deserializing the objects please refer to: https://stackoverflow.com/questions/34660339/gson-deserialization-with-generic-types-and-generic-field-names
 *
 * @author Feras E Alawadi
 * @version 1.0.0
 */
public final class BasicNetworkClientWrapper implements NetworkClient {
    private static final ReentrantLock lock = new ReentrantLock();
    private static final Logger logger = Logger.getLogger(BasicNetworkClientWrapper.class.getName());
    // meta Data for the post request.
    private static final MediaType JSON_UTF8 = MediaType.parse("application/json; charset=utf-8");
    // External Api Client
    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();

    private final Gson gson = new Gson();
    private static volatile BasicNetworkClientWrapper basicNetworkClientWrapper;
    // extra api attr
    private static final Map<String, String> extras = new HashMap<>();

    /* static methods only  */
    private BasicNetworkClientWrapper() {
    }

    /* singleton object */
    public static BasicNetworkClientWrapper getInstance(Map<String, String> attributes) {
        attributes.forEach(extras::put);
        // the object is expensive so use only the singleton object.
        if (basicNetworkClientWrapper == null) {
            synchronized (BasicNetworkClientWrapper.class) {
                return basicNetworkClientWrapper = new BasicNetworkClientWrapper();
            }
        }
        return basicNetworkClientWrapper;
    }


    @Override
    public <T> ApiResponse<T> uploadFile(Type type, File file) {
        RequestBody formBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(),
                        RequestBody.create(MediaType.parse("text/plain"), file))
                .build();
        Request request = new Request.Builder()
                .url(Objects.requireNonNull(extras.get("url")))
                .post(formBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                Type deSerializeType = new TypeToken<T>() {
                }.getType();
                return gson.fromJson(Objects.requireNonNull(response.body()).charStream(), deSerializeType);
            }

        } catch (IOException e) {
            logger.info("can't Upload the File, check the api call");
            return null;
        }
        return null;
    }

    private Request buildRequest(String url, String token, boolean withHeaders) {
        if (withHeaders)
            return new Request.Builder()
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .url(url)
                    .build();

        else
            return new Request.Builder()
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .url(url)
                    .build();
    }

    private Request buildRequest(String url, String token, RequestBody requestBody, boolean withHeaders) {
        if (withHeaders)
            return new Request.Builder()
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .url(url)
                    .post(requestBody)
                    .build();
        else
            return new Request.Builder()
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .url(Objects.requireNonNull(extras.get("url")))
                    .post(requestBody)
                    .build();
    }

    @Override
    public <T> ApiResponse<T> get(Type type, boolean withHeaders) {
        Request request = buildRequest(extras.get("url"), extras.get("token"), withHeaders);
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return gson.fromJson(Objects.requireNonNull(response.body()).charStream(), type);
            } else {
                System.out.println("not success..." + response.code());
                return null;
            }
        } catch (Exception e) {
            throw new IllegalStateException("can't get data from api, error: " + e.getMessage());
        }

    }


    @Override
    public <T, R> ApiResponse<R> post(Type type, T body, boolean withHeaders) {
        // Generating request body.
        RequestBody requestBody = RequestBody.create(JSON_UTF8, gson.toJson(body));

        Request request = buildRequest(extras.get("url"), extras.get("token"), requestBody, withHeaders);

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return gson.fromJson(Objects.requireNonNull(response.body()).charStream(), type);
            } else {
                logger.info("can't Post Data To The APIs, response code: " + response.code());
                return null;
            }
        } catch (Exception e) {
            logger.info("can't post date to the Api, error: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Bitmap loadImage(String imgUrl) {

        Request request = new Request.Builder()
                .url(imgUrl)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return BitmapFactory.decodeStream(Objects.requireNonNull(response.body()).byteStream());
            } else return null;
        } catch (IOException e) {
            return null;
        }

    }

    @Override
    public <T> ApiResponse<T> delete(Type type, T body, String token) {
        return null;
    }

    @Override
    public <T, R> ApiResponse<R> update(Type type, T body) {
        RequestBody requestBody = RequestBody.create(JSON_UTF8, gson.toJson(body));
        Request request = buildRequest(Objects.requireNonNull(extras.get("url")),
                Objects.requireNonNull(extras.get("token")), true);
        new Request.Builder()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", Objects.requireNonNull(extras.get("token")))
                .url(Objects.requireNonNull(extras.get("url")))
                .patch(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.code() == 401) {
                // unAuthenticated
                // refresh token.
                // rest call to refresh the token
                String refreshToken = extras.get("refreshToken");
            }
            if (response.isSuccessful()) {
                return gson.fromJson(Objects.requireNonNull(response.body()).charStream(), type);
            } else {
                logger.info("can't Update resource: error code: " + response.code());
                return null;
            }
        } catch (IOException e) {
            logger.info("can't perform an update request");
            return null;
        }
    }

}
