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
	private val TIMEOUT = 30L


	fun getAppService(context: Context) =
			Api.getRetrofit(context).create(AppService::class.java)

	private fun getRetrofit(context: Context): Retrofit {
		val retrofitBuilder = Retrofit.Builder()
				.addConverterFactory(GsonConverterFactory.create(gson))
				.addCallAdapterFactory(RxErrorCallAdapterFactory(RxJava2CallAdapterFactory.create()))
				.client(getOkHttp(context))
				.baseUrl(BASE_URL)
		return retrofitBuilder.build()
	}

	private fun getOkHttp(context: Context): OkHttpClient {
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

		okBuilder.interceptors().add(getHeaderInterceptor(context))
		okBuilder.authenticator { _, _ ->
			EventBus.getDefault().post(UnauthorizedEvent())
			return@authenticator null
		}
		return okBuilder.build()
	}


	private fun getHeaderInterceptor(context: Context): Interceptor? {
		return Interceptor { chain ->

			val requestBuilder = chain.request().newBuilder()
			requestBuilder.header("Content-Type", "application/json")
			requestBuilder.header("Accept", "application/json")

			Prefs.getAuthorization(context)?.let {
				requestBuilder.addHeader("Authorization", "Bearer ${it.token}")
			}

			chain.proceed(requestBuilder.build())
		}

	}

}
