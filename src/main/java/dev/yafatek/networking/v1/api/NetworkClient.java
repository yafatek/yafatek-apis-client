package dev.yafatek.networking.v1.api;


import android.graphics.Bitmap;
import dev.yafatek.networking.v1.models.ApiResponse;

import java.io.File;
import java.lang.reflect.Type;

/**
 * interface to Hold the Core Functions of the backend api call methods
 *
 * @author Feras E Alawadi
 * @version 1.0.0
 * @since 1.0.0
 */
public interface NetworkClient {

    /**
     * method to request data as a GET request from the Api
     *
     * @param <T> the Type of the query
     * @return an instance to continue chaining
     */

    /**
     * Get without headers.
     *
     * @param <T>
     * @param type
     * @return
     */
    <T> ApiResponse<T> get(Type type, boolean withHeaders);

    <T> ApiResponse<T> uploadFile(Type type, File file);

    /**
     * method to perform POST request to the Api
     *
     * @param <T>  the Object type
     * @param type the type of the Object
     * @param type
     * @param body the json Object to send to the api
     * @return an instance to continue chaining
     */

    <T, R> ApiResponse<R> post(Type type, T body, boolean withHeaders);

    /**
     * loading images to android using okhttp
     *
     * @param imgUrl the image url where the img file is hosted.
     * @return Bitmap file.
     */

    // Bitmap in android
    Bitmap loadImage(String imgUrl);

    /**
     * method to Perform Delete request to the Api.
     *
     * @param type  type of the object
     * @param body  the object to Delete
     * @param token security access token.
     * @param <T>   the object type
     * @return an instance to continue chaining
     */

    <T> ApiResponse<T> delete(Type type, T body, String token);

    /**
     * method to update resource in the DB, send it to the Api to update it
     *
     * @param <T>  the object type
     * @param type the object type
     * @param body the object as json
     * @return an instance to continue chaining
     */
    <T, R> ApiResponse<R> update(Type type, T body);

}