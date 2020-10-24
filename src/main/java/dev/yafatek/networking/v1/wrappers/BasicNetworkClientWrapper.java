package dev.yafatek.networking.v1.wrappers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import dev.yafatek.networking.v1.deserializers.CustomJsonDeserializer;
import dev.yafatek.networking.v1.models.ApiResponse;
import dev.yafatek.networking.v1.models.Body;
import dev.yafatek.networking.v1.system.NetworkClient;
import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * for deserializing the objects please refer to: https://stackoverflow.com/questions/34660339/gson-deserialization-with-generic-types-and-generic-field-names
 *
 * @author Feras E Alawadi
 * @version 1.0.0
 */
public final class BasicNetworkClientWrapper implements NetworkClient {
    private static final Logger logger = Logger.getLogger(BasicNetworkClientWrapper.class.getName());
    // meta Data for the post request.
    private static final MediaType JSON_UTF8 = MediaType.parse("application/json; charset=utf-8");
    // External Api Client
    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();

    private Gson gson = new Gson();
    private static BasicNetworkClientWrapper basicNetworkClientWrapper;
    // extra api attr
    private static Map<String, String> extras = new HashMap<>();
    // api response Object.
    private ApiResponse<Body<?>> apiResponse;

    /* static methods only  */
    private BasicNetworkClientWrapper() {
    }

    /* singleton object */
    public static BasicNetworkClientWrapper getInstance(Map<String, String> attributes) {
        System.out.println("basic instance...");
        extras = attributes;
        // the object is expensive so use only the singleton object.
        if (basicNetworkClientWrapper == null) {
            synchronized (BasicNetworkClientWrapper.class) {
                basicNetworkClientWrapper = new BasicNetworkClientWrapper();
            }
        }
        return basicNetworkClientWrapper;
    }

    @Override
    public <T> NetworkClient get(Class<T> type, String header, String token) {
        Request request = new Request.Builder()
                .header(header, token)
                .url(extras.get("url"))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                Type deSerializeType = new TypeToken<T>() {
                }.getType();

                apiResponse = gson.fromJson(Objects.requireNonNull(response.body()).charStream(), deSerializeType);

            } else {
                throw new IllegalAccessException("Connection Not Successful to Apis");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public <T> NetworkClient uploadFile(Class<T> type, File file) {
        RequestBody formBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(),
                        RequestBody.create(file, MediaType.parse("text/plain")))
                .build();
        Request request = new Request.Builder()
                .url(extras.get("url"))
                .post(formBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                Type deSerializeType = new TypeToken<T>() {
                }.getType();
                apiResponse = gson.fromJson(Objects.requireNonNull(response.body()).charStream(), deSerializeType);
            }

        } catch (IOException e) {
            logger.info("can't Upload the File, check the api call");
        }

        return this;
    }

    @Override
    public <T> NetworkClient get(Class<T> type, String token) {

        logger.info("in");
        Type returnType = new TypeToken<Body<T>>() {
        }.getType();
        gson = new GsonBuilder()
                .registerTypeAdapter(returnType, new CustomJsonDeserializer<>(type)
                ).create();

        Request request = new Request.Builder()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", token)
                .url(extras.get("url"))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                Type deSerializeType = new TypeToken<T>() {
                }.getType();
                apiResponse = gson.fromJson(Objects.requireNonNull(response.body()).charStream(), returnType);
                System.out.println("apiResponse" + apiResponse);
//                apiResponse = gson.fromJson(Objects.requireNonNull(response.body()).charStream(), deSerializeType);

            } else System.out.println("not success..." + response.code());
        } catch (Exception e) {
            throw new IllegalStateException("can't get data from api, error: " + e.getMessage());

        }
        return this;

    }


    @Override
    public <D, T> NetworkClient post(Class<D> type, T body) {
        // Generating request body.
        RequestBody requestBody = RequestBody.create(gson.toJson(body), JSON_UTF8);

        Request request = new Request.Builder()
                .url(extras.get("url"))
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            System.out.println(response.code());
            if (response.isSuccessful()) {
                Type deSerializeType = new TypeToken<D>() {
                }.getType();
                apiResponse = gson.fromJson(Objects.requireNonNull(response.body()).charStream(), deSerializeType);
            }
        } catch (Exception e) {
            throw new IllegalStateException("can't post date to the Api, error: " + e.getMessage());
        }
        return this;
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
    public <T> NetworkClient delete(Class<T> type, T body) {
        return this;
    }

    @Override
    public <D, T> NetworkClient update(Class<D> type, T body, String token) {
        RequestBody requestBody = RequestBody.create(gson.toJson(body), JSON_UTF8);
        Request request = new Request.Builder()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", token)
                .url(extras.get("url"))
                .patch(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                Type deSerializeType = new TypeToken<D>() {
                }.getType();

                apiResponse = gson.fromJson(Objects.requireNonNull(response.body()).charStream(), deSerializeType);

            }

        } catch (IOException e) {
            throw new IllegalStateException("can't perform an update request");
        }
        return this;
    }

    @Override
    public <T> T fetchData(Class<T> type) {
        return (T) apiResponse;
    }

}
