package com.example.lukas.tesonettest.api

import com.example.lukas.tesonettest.GlobalErrorEvent
import com.example.lukas.tesonettest.util.gson
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.HttpException
import retrofit2.Response
import java.lang.reflect.Type

class RxErrorCallAdapter<R>(private val mDelegate: CallAdapter<R, Any>,
                            private val isResponse: Boolean) : CallAdapter<R, Any> {

	override fun responseType(): Type {
		return mDelegate.responseType()
	}

	override fun adapt(call: Call<R>): Any {

		if (isResponse) {
			val observable = mDelegate.adapt(call) as Observable<Response<Any>>
			return observable
					.subscribeOn(Schedulers.io())
					.observeOn(AndroidSchedulers.mainThread())
					.doOnNext {
						if (!it.isSuccessful) {
							it.errorBody()?.let {
								sendErrorEvent(it.string())
							}
						}
					}
					.doOnError {
					}
					.doOnComplete {
					}

		} else {
			val observable = mDelegate.adapt(call) as Observable<Any>
			return observable
					.subscribeOn(Schedulers.io())
					.observeOn(AndroidSchedulers.mainThread())
					.doOnNext {
					}
					.doOnError { e ->
						if (e is HttpException) {
							e.response().errorBody()?.let {
								sendErrorEvent(it.string())
							}
						}
					}
					.doOnComplete {
					}
		}
	}

	private fun sendErrorEvent(body: String) {
		val errorResponse = gson.fromJson(body, ErrorResponse::class.java)
		EventBus.getDefault().post(GlobalErrorEvent(errorResponse.message))
	}

}

