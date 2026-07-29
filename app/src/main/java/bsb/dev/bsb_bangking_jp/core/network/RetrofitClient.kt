package bsb.dev.bsb_bangking_jp.core.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Konfigurasi Retrofit terpusat (singleton), supaya cuma ada 1 instance
 * OkHttpClient/Retrofit di seluruh aplikasi.
 *
 * TODO: kalau nanti backend production sudah ada, ganti BASE_URL dan
 * tambahkan interceptor Authorization (Bearer token dari SessionManager).
 */
object RetrofitClient {

    private const val BASE_URL = "https://dummyjson.com/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val authApi: AuthApiService by lazy {
        retrofit.create(AuthApiService::class.java)
    }
}
