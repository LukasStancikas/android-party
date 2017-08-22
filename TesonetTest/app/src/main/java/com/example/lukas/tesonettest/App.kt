package com.example.lukas.tesonettest

import android.app.Application
import com.example.lukas.tesonettest.api.Api
import com.example.lukas.tesonettest.util.Prefs

/**
 * Created by lukas on 17.2.10.
 */
class App : Application() {
	override fun onCreate() {
		super.onCreate()
		Prefs.context = applicationContext
		Api.context = applicationContext
	}
}
