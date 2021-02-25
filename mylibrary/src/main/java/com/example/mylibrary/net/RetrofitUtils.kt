package com.example.mylibrary.net


import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object RetrofitUtils {

   lateinit var retrofit: Retrofit

    fun init() {
        retrofit=Retrofit.Builder().baseUrl(BaseApi.BASE_URL).client(getOkHttpClick())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    private fun  getOkHttpClick():OkHttpClient{
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level= HttpLoggingInterceptor.Level.BODY
      return OkHttpClient.Builder()
          .addInterceptor(SignatureInterceptor())
          .addInterceptor(httpLoggingInterceptor)
          .build()

    }

    fun <T> create(clazz : Class<T>)=retrofit.create(clazz)
}