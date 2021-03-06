package com.sanjay.networking.interceptors;


import androidx.annotation.NonNull;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class AuthHeaderInterceptor implements Interceptor {
    private final String token;

    public AuthHeaderInterceptor(String token) {
        this.token = token;
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();

        if (token == null) {
            return chain.proceed(request);
        } else {
            request = request.newBuilder()
                    .addHeader("token", token)
                    .build();
        }

        return chain.proceed(request);
    }
}
