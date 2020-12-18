package dev.yafatek.networking.v1.api;

import dev.yafatek.networking.v1.wrappers.NetworkClientBuilderWrapper;

import java.util.Map;

public interface NetworkClientBuilder {
    /**
     * method to Build a new implementation of the interface
     *
     * @param clazz the class
     * @param <E>   return type usually the class that implements the interface
     * @return new instance of the new implementation
     * @throws Exception Constructors Exception
     */
    static <E> E build(Class<E> clazz) throws Exception {
        return clazz.getDeclaredConstructor().newInstance();
    }

    /**
     * method to build the object with the default interface implementation.
     * ded
     *
     * @return new instance of that implementation.
     */
    static NetworkClientBuilderWrapper defaults() {
        return  NetworkClientBuilderWrapper.getInstance();
    }

    /**
     * Method to get the api url (usually the End point for a resource)
     *
     * @param apiUrl the End point url
     * @return chaining Object
     */

    NetworkClientBuilder url(String apiUrl);

    NetworkClientBuilder headers(Map<String, String> headers);

    NetworkClient then();
}
