package com.example.lukas.tesonettest.model

import android.os.Parcelable
import com.example.lukas.tesonettest.util.Prefs
import kotlinx.android.parcel.Parcelize

/**
 * Created by lukas on 17.8.17.
 */
@Parcelize
class Token(val token: String) : Parcelable{
	fun save() {
		Prefs.authorization = this
	}
}