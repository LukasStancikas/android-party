package com.example.lukas.tesonettest.fragment

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.view.View
import com.example.lukas.tesonettest.R
import com.example.lukas.tesonettest.activity.MainActivity
import com.example.lukas.tesonettest.custom.ServerListAdapter
import com.example.lukas.tesonettest.model.Server
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_server_list.*
import com.example.lukas.tesonettest.api.Api
import com.example.lukas.tesonettest.util.gson


/**
 * Created by lukas on 17.8.17.
 */
class ServerListFragment : BaseFragment() {
	companion object {
		private val LIST = "list"

		fun getInstance(servers: List<Server>): ServerListFragment {
			val fragment = ServerListFragment()
			val bundle = Bundle()
			bundle.putParcelableArrayList(LIST, ArrayList(servers))
			fragment.arguments = bundle
			return fragment
		}
	}

	override val layoutId = R.layout.fragment_server_list

	override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		val servers: List<Server> = arguments.getParcelableArrayList(LIST)
		setupMenu()
		setupRecyclerData(servers)
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


