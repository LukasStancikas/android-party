package com.example.lukas.tesonettest.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import com.example.lukas.tesonettest.api.Api
import com.example.lukas.tesonettest.model.Server
import io.reactivex.Observable

/**
 * Created by lukas on 17.11.3.
 */
class ServerViewModel : ViewModel() {
	val data = MutableLiveData<List<Server>>()

	fun getServers(context: Context): Observable<List<Server>> = Api
			.getAppService(context)
			.getServers()
			.doOnNext {
				data.postValue(it)
			}
}