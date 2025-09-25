package com.kvn.jobopportunityapp.data.network

import com.kvn.jobopportunityapp.BuildConfig
import com.kvn.jobopportunityapp.data.AppPreferences
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ServiceLocator {

    @Volatile
    private var apiInstance: ApiService? = null

    fun api(prefs: AppPreferences): ApiService = apiInstance ?: synchronized(this) {
        apiInstance ?: buildApi(prefs).also { apiInstance = it }
    }

    private fun buildApi(prefs: AppPreferences): ApiService {
        // Asegurar que la base URL termina en '/'
        val rawBase = BuildConfig.API_BASE_URL
        val baseUrl = if (rawBase.endsWith('/')) rawBase else "$rawBase/"

        val authAndCommonHeaders = Interceptor { chain ->
            val original = chain.request()
            val builder = original.newBuilder()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")

            val token = prefs.authToken
            if (!token.isNullOrBlank()) {
                builder.header("Authorization", "Bearer $token")
            }
            chain.proceed(builder.build())
        }

        val logging = HttpLoggingInterceptor().apply {
            // Sólo log detallado en BuildConfig.DEBUG para no filtrar producción
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }

        // Interceptor simple de reintento idempotente para respuestas 502/503/504
        val retryInterceptor = Interceptor { chain ->
            var attempt = 0
            val maxRetries = 2
            var response = chain.proceed(chain.request())
            while (attempt < maxRetries && (response.code == 502 || response.code == 503 || response.code == 504)) {
                response.close()
                attempt++
                response = chain.proceed(chain.request())
            }
            response
        }

        val client = OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .addInterceptor(authAndCommonHeaders)
            .addInterceptor(retryInterceptor)
            .addInterceptor(logging)
            .build()

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
