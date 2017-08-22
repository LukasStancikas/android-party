package com.example.lukas.tesonettest.api

import android.content.Context
import com.example.lukas.tesonettest.BuildConfig
import com.example.lukas.tesonettest.UnauthorizedEvent
import com.example.lukas.tesonettest.util.Prefs
import com.example.lukas.tesonettest.util.gson
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.greenrobot.eventbus.EventBus
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

object Api {
	val BASE_URL = BuildConfig.BASE_API_URL
	lateinit var context: Context
	private val TIMEOUT = 30L


	val appService: AppService by lazy {
		Api.retrofit.create(AppService::class.java)
	}

	internal val retrofit by lazy {
		val retrofitBuilder = Retrofit.Builder()
		retrofitBuilder.addConverterFactory(GsonConverterFactory.create(gson))
		retrofitBuilder.addCallAdapterFactory(RxErrorCallAdapterFactory(RxJava2CallAdapterFactory.create()))
		retrofitBuilder.client(okClient)
		retrofitBuilder.baseUrl(BASE_URL)
		retrofitBuilder.build()
	}

	val okClient: OkHttpClient by lazy {
		val okBuilder = OkHttpClient.Builder()

		val httpCacheDirectory = File(context.cacheDir, "responses")
		val cacheSize = 100L * 1024L * 1024L // 100 MiB
		val cache = Cache(httpCacheDirectory, cacheSize)

		okBuilder.cache(cache)
		okBuilder.connectTimeout(TIMEOUT, TimeUnit.SECONDS)
		okBuilder.writeTimeout(TIMEOUT, TimeUnit.SECONDS)
		okBuilder.readTimeout(TIMEOUT, TimeUnit.SECONDS)

		val loggingInterceptor = HttpLoggingInterceptor()
		loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
		okBuilder.interceptors().add(loggingInterceptor)

		okBuilder.interceptors().add(getHeaderInterceptor())
		okBuilder.authenticator { _, _ ->
			EventBus.getDefault().post(UnauthorizedEvent())
			return@authenticator null
		}
		okBuilder.build()
	}


	private fun getHeaderInterceptor(): Interceptor? {
		return Interceptor {
			chain ->

			val requestBuilder = chain.request().newBuilder()
			requestBuilder.header("Content-Type", "application/json")
			requestBuilder.header("Accept", "application/json")

			Prefs.authorization?.let {
				requestBuilder.addHeader("Authorization", "Bearer ${it.token}")
			}

			chain.proceed(requestBuilder.build())
		}

	}

}
