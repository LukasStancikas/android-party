package com.example.lukas.tesonettest.util

import android.content.Context
import android.preference.PreferenceManager
import com.example.lukas.tesonettest.model.Token


/**
 * Created by lukas on 17.2.10.
 */
object Prefs {
	private val AUTHORIZATION = "authorization"

	private fun getSharedPreferences(context: Context) =
			PreferenceManager.getDefaultSharedPreferences(context)

	fun setAuthorization(context: Context, token: Token?) {
		val json = token?.let {
			gson.toJson(it)
		}
		getSharedPreferences(context).edit().putString(AUTHORIZATION, json).apply()
	}

	fun getAuthorization(context: Context): Token? {
		return getSharedPreferences(context).getString(AUTHORIZATION, null)?.let {
			gson.fromJson(it, Token::class.java)
		}
	}
}