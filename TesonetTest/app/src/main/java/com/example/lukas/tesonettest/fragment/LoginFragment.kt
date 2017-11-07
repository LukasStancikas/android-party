package com.example.lukas.tesonettest.fragment

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.View
import com.example.lukas.tesonettest.R
import com.example.lukas.tesonettest.model.Login
import com.example.lukas.tesonettest.util.Prefs
import com.example.lukas.tesonettest.viewmodel.LoginViewModel
import com.example.lukas.tesonettest.viewmodel.ServerViewModel
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_login.*
import java.util.concurrent.TimeUnit


/**
 * Created by lukas on 17.8.17.
 */
class LoginFragment : BaseFragment() {
	override val layoutId = R.layout.fragment_login

	private val serversViewModel by lazy {
		activity?.let {
			ViewModelProviders.of(it).get(ServerViewModel::class.java)
		}
	}
	private val loginViewModel by lazy {
		activity?.let {
			ViewModelProviders.of(it).get(LoginViewModel::class.java)
		}
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		setupFields()
		val usernameObservable = RxTextView.textChanges(login_username)
		val passwordObservable = RxTextView.textChanges(login_password)

		val isSignInEnabled: Observable<Boolean> = Observable
				.combineLatest(usernameObservable,
				               passwordObservable,
				               BiFunction { u, p -> u.isNotEmpty() && p.isNotEmpty() })
		isSignInEnabled.subscribe { login_button.isEnabled = it }
		login_button.setOnClickListener { onLoginClick() }
		context?.let {
			if (Prefs.getAuthorization(it) != null) {
				fetchServers(it)
			}
		}

	}

	private fun setupFields() {
		login_button.isEnabled = false
		login_password.typeface = Typeface.DEFAULT
		login_password.transformationMethod = PasswordTransformationMethod()
	}

	private fun onLoginClick() {
		context?.let { context ->
			loginViewModel?.let {
				val body = Login(login_username.text.toString(), login_password.text.toString())
				it.login(body, context)
						.doOnSubscribe {
							showProgress(true)
							setLoadingText(R.string.loading_text_login)
						}
						.doOnError {
							showProgress(false)
						}
						.subscribe({ token ->
							           token.save(context)
							           fetchServers(context)
						           }, Throwable::printStackTrace)
						.addTo(disposable)
			}
		}

	}

	private fun fetchServers(context: Context) {
		serversViewModel?.let {
			it.getServers(context)
					//delay to make the progress visible
					.delay(2000L, TimeUnit.MILLISECONDS)
					.subscribeOn(Schedulers.io())
					.observeOn(AndroidSchedulers.mainThread())
					.doOnSubscribe {
						showProgress(true)
						setLoadingText(R.string.loading_text_servers)
					}
					.doOnError {
						showProgress(false)
					}
					.subscribe({
						           changeFragment(ServerListFragment(), false)
						           serversViewModel?.data?.postValue(it)
					           }, Throwable::printStackTrace)
					.addTo(disposable)
		}
	}

	private fun showProgress(show: Boolean) {
		login_logo.visibility = if (show) View.GONE else View.VISIBLE
		login_container.visibility = if (show) View.GONE else View.VISIBLE
		login_progress_container.visibility = if (show) View.VISIBLE else View.GONE
	}

	private fun setLoadingText(resId: Int) {
		login_progress_text.text = getString(resId)
	}
}