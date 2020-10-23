package dev.yafatek.networking.v1.system;

import android.graphics.Bitmap;

import java.io.File;

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
     * @param query the query to api
     * @param <T>   the Type of the query
     * @return an instance to continue chaining
     */
    <T> NetworkClient get(Class<T> type, String query);

    <T> NetworkClient get(Class<T> type, String header, String token);

    <T> NetworkClient uploadFile(Class<T> type, File file);

    /**
     * method to perform POST request to the Api
     *
     * @param type the type of the Object
     * @param body the json Object to send to the api
     * @param <T>  the Object type
     * @return an instance to continue chaining
     */

    <D, T> NetworkClient post(Class<D> type, T body);

    // Bitmap in android
    Bitmap loadImage(String imgUrl);

    /**
     * method to Perform Delete request to the Api.
     *
     * @param type type of the object
     * @param body the object to Delete
     * @param <T>  the object type
     * @return an instance to continue chaining
     */

    <T> NetworkClient delete(Class<T> type, T body);

    /**
     * method to update resource in the DB, send it to the Api to update it
     *
     * @param type the object type
     * @param body the object as json
     * @param <T>  the object type
     * @return an instance to continue chaining
     */
    <D, T> NetworkClient update(Class<D> type, T body, String token);


    /**
     * method to get the api response after a successFull call to the Backend
     *
     * @param type the response wrapper
     * @param <T>  the Type of the wrapper
     * @return the Api response as a java object
     */
    <T> T load(Class<T> type);

}
