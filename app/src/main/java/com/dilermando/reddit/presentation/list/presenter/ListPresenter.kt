package com.dilermando.reddit.presentation.list.presenter

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import com.dilermando.reddit.data.api.RedditsService
import com.dilermando.reddit.data.entity.Listing
import com.dilermando.reddit.data.entity.Thing
import com.dilermando.reddit.doIfBothAreNotNull
import com.dilermando.reddit.domain.utils.Constants
import com.dilermando.reddit.domain.utils.Utils
import com.dilermando.reddit.presentation.BaseView
import com.dilermando.reddit.presentation.details.DetailsActivity
import com.dilermando.reddit.presentation.list.ListContract
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class ListPresenter @Inject
constructor(private val redditsService: RedditsService) : ListContract.Presenter {

    private var view: ListContract.View? = null
    private var connectionBroadcastReceiver: BroadcastReceiver? = null
    private var mNextPage: String = ""
    private val myList: MutableLiveData<MutableList<Thing>> = MutableLiveData()

    override fun resume() {
        doIfBothAreNotNull(view, connectionBroadcastReceiver) { view, broadcastReceiver ->
            view.registerConnectionBroadcastReceiver(broadcastReceiver)
        }
    }

    override fun pause() {
        doIfBothAreNotNull(view, connectionBroadcastReceiver) { view, broadcastReceiver ->
            view.unRegisterConnectionBroadcastReceiver(broadcastReceiver)
        }
    }

    override fun destroy() {
        connectionBroadcastReceiver = null
        mNextPage = ""
    }

    override fun setView(view: BaseView) {
        this.view = view as ListContract.View
    }

    override fun onItemClicked(context: Context, thing: Thing) {
        // Launch Activity
        val intent = Intent(context, DetailsActivity::class.java)
        intent.putExtra(Constants.REDDIT_SELECTED_KEY, Utils.toString(thing))
        this.view?.startActivity(intent)
    }

    override fun getRedditList(): MutableLiveData<MutableList<Thing>> {
        return myList
    }

    override fun getReddits(passNextPage: Boolean) {
        if (!passNextPage) {
            mNextPage = ""
            myList.value = ArrayList()
        } else {
            view?.showLoading()
        }

        val comp = CompositeDisposable()
        comp.add(
                redditsService.getPaginatedReddits(mNextPage, Constants.DEFAULT_LIMIT)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ response ->
                            run {
                                Timber.d("Got some feed back!")
                                if (myList.value == null) {
                                    myList.value = ArrayList()
                                }
                                (myList.value as ArrayList<Thing>).addAll(getListOfApplications(response.listing))
                                if (myList.value?.isEmpty() == true) {
                                    view?.showEmptyResponseMessage()
                                    Timber.i("Empty response from API.")
                                } else {
                                    view?.updateListOfReddits(myList.value!!)
                                    Timber.i("Apps data was loaded from API.")
                                }
                                if (passNextPage) {
                                    view?.hideLoading()
                                }
                            }
                        }, { t ->
                            run {
                                Timber.e(t, "Failed to get feed!")
                                view?.showErrorDuringRequestMessage()
                                view?.hideLoading()
                            }
                        }))
    }

    private fun getListOfApplications(feedWrapper: Listing?): List<Thing> {
        var listing: Listing? = null
        var things: List<Thing> = ArrayList()
        if (feedWrapper != null) {
            listing = feedWrapper
        }
        if (listing != null) {
            if (listing.things != null && listing.things!!.isNotEmpty()) {
                Timber.i("getListOfApplications(): size: %s", listing.things!!.size)
                things = listing.things!!
                mNextPage = listing.after.toString()
            } else {
                Timber.w("getListOfApplications(): Empty entries.")
            }
        } else {
            Timber.w("getListOfApplications(): Empty listing!")
        }
        return things
    }

    override fun setupConnectionBroadcastReceiver() {
        connectionBroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (Utils.hasNetwork(context)) {
                    view?.hideNoConnectionMessage()
                } else {
                    view?.showNoConnectionMessage()
                }
            }
        }
    }
}
