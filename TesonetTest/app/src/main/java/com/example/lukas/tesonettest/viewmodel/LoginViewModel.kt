package com.example.lukas.tesonettest.viewmodel

import android.arch.lifecycle.ViewModel
import android.content.Context
import com.example.lukas.tesonettest.api.Api
import com.example.lukas.tesonettest.model.Login

/**
 * Created by lukas on 17.11.3.
 */
class LoginViewModel : ViewModel() {
	fun login(login: Login, context: Context) = Api.getAppService(context).login(login)

}