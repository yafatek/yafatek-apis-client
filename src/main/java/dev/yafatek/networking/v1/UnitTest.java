package dev.yafatek.networking.v1;

import com.google.gson.reflect.TypeToken;
import dev.yafatek.networking.v1.models.ApiResponse;
import dev.yafatek.networking.v1.system.NetworkClientBuilder;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class UnitTest {

    public static void main(String[] args) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");

        // type:
        Type type = new TypeToken<ApiResponse<?>>() {
        }.getType();

        Object o = NetworkClientBuilder.defaults()
                .url("https://eu-apis.my-face.app/api/v1/app-settings/by-region?region=eu")
                .headers(headers)
                .then()
                .get(type, false);

        System.out.println(o);

    }
}
