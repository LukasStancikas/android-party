package com.example.lukas.tesonettest.util

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.example.lukas.tesonettest.model.Token


/**
 * Created by lukas on 17.2.10.
 */
object Prefs {
	private val AUTHORIZATION = "authorization"
	lateinit var context: Context

	val preferences: SharedPreferences by lazy {
		PreferenceManager.getDefaultSharedPreferences(context)
	}

	var authorization: Token?
		get() {
			preferences.getString(AUTHORIZATION, null)?.let {
				return gson.fromJson(it, Token::class.java)
			} ?: return null
		}
		set(value) {
			val json = value?.let {
				gson.toJson(it)
			}
			preferences.edit().putString(AUTHORIZATION, json).apply()

		}
}