package com.dilermando.reddit.presentation.list

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.lifecycle.MutableLiveData

import com.dilermando.reddit.data.entity.Thing
import com.dilermando.reddit.presentation.BasePresenter
import com.dilermando.reddit.presentation.BaseView


interface ListContract {

    interface Presenter : BasePresenter {

        fun onItemClicked(context: Context, thing: Thing)

        fun getReddits(passNextPage: Boolean)

        fun getRedditList() : MutableLiveData<MutableList<Thing>>

        fun setupConnectionBroadcastReceiver()
    }

    interface View : BaseView {

        fun updateListOfReddits(listOfReddits: List<Thing>)

        fun showLoading()

        fun hideLoading()

        fun hideNoConnectionMessage()

        fun showNoConnectionMessage()

        fun registerConnectionBroadcastReceiver(broadcastReceiver: BroadcastReceiver)

        fun unRegisterConnectionBroadcastReceiver(broadcastReceiver: BroadcastReceiver)

        fun showErrorDuringRequestMessage()

        fun showEmptyResponseMessage()

        fun startActivity(intent: Intent)
    }
}
