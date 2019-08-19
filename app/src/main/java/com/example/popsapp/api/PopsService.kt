package com.example.popsapp.api

import com.example.popsapp.BuildConfig
import com.example.popsapp.data.model.LoginRequestBody
import com.example.popsapp.data.model.LoginResponse
import com.example.popsapp.data.model.orders.OrdersResponse
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface PopsService {

    @POST("login")
    fun login(@Body query: LoginRequestBody): Observable<LoginResponse>

    @POST("logout")
    fun logout(@Header("sessionToken") sessionToken: String): Observable<ResponseBody>

    @GET("orders")
    fun fetchOrders(
        @Header("sessionToken") sessionToken: String,
        @QueryMap params: Map<String, String>
    ): Observable<OrdersResponse>

    companion object {
        private val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        private var popsService: PopsService? = null

        fun getService(): PopsService {
            if (popsService == null) {
                val httpClient = OkHttpClient.Builder()

                /**
                 * Enable http logs during development
                 */
                if (BuildConfig.DEBUG) {
                    httpClient.addInterceptor(logging)
                }

                val retrofit = Retrofit.Builder()
                    .baseUrl("https://apibo3dev.pops.co/")
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build()
                popsService = retrofit.create(PopsService::class.java)
            }

            return popsService!!
        }
    }

}