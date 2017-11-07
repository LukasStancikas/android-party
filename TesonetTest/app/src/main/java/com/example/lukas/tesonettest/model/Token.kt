package com.example.lukas.tesonettest.model

import android.content.Context
import com.example.lukas.tesonettest.util.Prefs

/**
 * Created by lukas on 17.8.17.
 */
class Token(val token: String){
	fun save(context: Context) {
		Prefs.setAuthorization(context,this)
	}
}