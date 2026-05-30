package com.example.footystats.data.remote

import okhttp3.Interceptor
import okhttp3.Response
import com.example.footystats.BuildConfig

class AuthInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
            .newBuilder()
            .addHeader("X-Auth-Token", BuildConfig.FOOTBALL_API_KEY)
            .build()

        return chain.proceed(request)
    }
}