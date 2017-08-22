package com.example.lukas.tesonettest.model

import android.os.Parcelable
import io.reactivex.Observable
import com.example.lukas.tesonettest.api.Api
import kotlinx.android.parcel.Parcelize

/**
 * Created by lukas on 17.8.17.
 */
@Parcelize
class Server(val name: String, val distance: Long) : Parcelable  {
	companion object {
		fun getServers(): Observable<List<Server>> = Api.appService.getServers()
	}

}