package com.ma.bakingrecipes.data.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * source https://code.tutsplus.com/tutorials/getting-started-with-retrofit-2--cms-27792
 */

public class RetrofitClient {

    private static volatile Retrofit retrofit;
    private static final Object LOCK = new Object();

    public static Retrofit getClient(String baseUrl) {
        if (retrofit == null) {
            synchronized (LOCK) {
                if (retrofit == null) {
                    retrofit = new Retrofit.Builder()
                            .baseUrl(baseUrl)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                }
            }
        }
        return retrofit;
    }
}
