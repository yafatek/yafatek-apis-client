package dev.yafatek.networking.v1.wrappers;


import android.os.Build;
import dev.yafatek.networking.v1.api.NetworkClient;
import dev.yafatek.networking.v1.api.NetworkClientBuilder;


import java.util.HashMap;
import java.util.Map;

public final class NetworkClientBuilderWrapper implements NetworkClientBuilder {

    private static volatile NetworkClientBuilderWrapper networkClientBuilderWrapper;
    // hashtable to hold any extra stuff the api needs.
    private final Map<String, String> extras = new HashMap<>();

    // singleton only.
    private NetworkClientBuilderWrapper() {
    }

    public  static NetworkClientBuilderWrapper getInstance() {
        System.out.println("builder instance...");
        if (networkClientBuilderWrapper == null) {
            synchronized (NetworkClientBuilderWrapper.class) {
                networkClientBuilderWrapper = new NetworkClientBuilderWrapper();
            }
        }
        return networkClientBuilderWrapper;
    }

    @Override
    public NetworkClientBuilder url(String apiUrl) {
        extras.put("url", apiUrl);
        return this;
    }

    @Override
    public NetworkClientBuilder headers(Map<String, String> headers) {
        // adding the headers.
        headers.forEach(extras::put);
        return this;
    }

    @Override
    public NetworkClient then() {
        return BasicNetworkClientWrapper.getInstance(extras);
    }

}
