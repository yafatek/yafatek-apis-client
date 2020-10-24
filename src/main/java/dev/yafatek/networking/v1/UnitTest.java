package dev.yafatek.networking.v1;

import dev.yafatek.networking.v1.models.ApiResponse;
import dev.yafatek.networking.v1.system.NetworkClientBuilder;

import java.util.HashMap;
import java.util.Map;

public class UnitTest {

    public static void main(String[] args) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");

        Object o = NetworkClientBuilder.defaults()
                .url("https://apis.sufertas.online/api/v1/items/")
                .headers(headers)
                .then()
                .get(ApiResponse.class, "")
                .fetchData(ApiResponse.class);

        System.out.println(o);

    }
}
