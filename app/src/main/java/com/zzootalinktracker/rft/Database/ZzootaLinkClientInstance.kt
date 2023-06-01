package com.zzootalinktracker.rft.Database

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ZzootaLinkClientInstance {
    private var BASE_URL = com.zzootalinktracker.rft.Utils.BASE_URL

    // *** COMMENT THE LINES BELOW WHEN USING APPROOV ***
    private var retrofit: Retrofit? = null
    @JvmStatic
    val retrofitInstance: Retrofit?
        get() {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(UnsafeOkHttpClient.getUnsafeOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit
        }

}