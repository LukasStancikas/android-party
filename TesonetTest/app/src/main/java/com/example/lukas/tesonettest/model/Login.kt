package com.example.lukas.tesonettest.model

import io.reactivex.Observable
import com.example.lukas.tesonettest.api.Api

/**
 * Created by lukas on 17.8.17.
 */
class Login(val username: String,val password:String){

	fun login(): Observable<Token> {
		return Api.appService.login(this)
	}
}