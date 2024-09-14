package com.example.splash1;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit retrofit = null;
    private static final String BASE_URL = "https://messagingapi.azurewebsites.net/api/";

    public static Retrofit getClient() {
        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(chain -> {
                        okhttp3.Request request = chain.request().newBuilder()
                                .addHeader("token", "EAA1hkIAlJQsBOyfncnv5UcLBH3tdzzz8syQCu7ELuyclZBE6OAIWWzm8Pwn5QAntyfpF2oRaeN3FG3mXrzVL6ekFqHIIFbdzgMyQ31n3FAvjyHSePoGE3sWLrfs8Q24szBaWxpZBGofd1hXRZBqLD0V4a72tusNlFHZAHILKkKmZC9ZAgGKUVf1dclk8c7jbw9aJqSWXKXjeD9N8rvKeAZD")
                                .build();
                        return chain.proceed(request);
                    })
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}