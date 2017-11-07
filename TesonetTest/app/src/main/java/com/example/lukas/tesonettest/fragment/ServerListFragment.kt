package com.example.lukas.tesonettest.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.view.View
import com.example.lukas.tesonettest.R
import com.example.lukas.tesonettest.activity.MainActivity
import com.example.lukas.tesonettest.custom.ServerListAdapter
import com.example.lukas.tesonettest.model.Server
import com.example.lukas.tesonettest.viewmodel.ServerViewModel
import kotlinx.android.synthetic.main.fragment_server_list.*


/**
 * Created by lukas on 17.8.17.
 */
class ServerListFragment : BaseFragment() {
	private val serversViewModel by lazy {
		activity?.let {
			ViewModelProviders.of(it).get(ServerViewModel::class.java)
		}
	}

	override val layoutId = R.layout.fragment_server_list

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		setupMenu()

		serversViewModel
				?.data
				?.observe(this, Observer {
					it?.let {
						setupRecyclerData(it)
					}

				})
	}

	private fun setupRecyclerData(servers: List<Server>) {
		server_list_recycler.adapter = ServerListAdapter(servers)
		server_list_recycler.addItemDecoration(DividerItemDecoration(context,
		                                                             DividerItemDecoration.VERTICAL))
	}

	private fun setupMenu() {
		server_list_toolbar.menu.clear()
		server_list_toolbar.inflateMenu(R.menu.menu_server_list)
		server_list_toolbar.setOnMenuItemClickListener {
			when (it.itemId) {
				R.id.menuLogout -> {
					if (activity is MainActivity) {
						(activity as MainActivity).logout()
					}
					true
				}
				else            -> {
					false
				}
			}
		}
	}


}


