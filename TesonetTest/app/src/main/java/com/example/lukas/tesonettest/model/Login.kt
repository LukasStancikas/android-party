package com.example.lukas.tesonettest.model

import android.content.Context
import com.example.lukas.tesonettest.api.Api

/**
 * Created by lukas on 17.8.17.
 */
class Login(val username: String, val password: String) {

	fun login(context: Context) = Api.getAppService(context).login(this)
}