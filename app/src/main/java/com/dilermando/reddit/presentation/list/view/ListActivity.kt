package com.dilermando.reddit.presentation.list.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dilermando.reddit.R
import com.dilermando.reddit.RedditApplication
import com.dilermando.reddit.data.entity.Thing
import com.dilermando.reddit.databinding.ActivityListBinding
import com.dilermando.reddit.presentation.BaseActivity
import com.dilermando.reddit.presentation.list.ListContract
import com.dilermando.reddit.presentation.list.view.adapter.ItemSubredditAdapter
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import java.util.*
import javax.inject.Inject

class ListActivity : BaseActivity(), ListContract.View, ItemSubredditAdapter.OnItemClickListener {

    @Inject
    lateinit var presenter: ListContract.Presenter
    private var snackbar: Snackbar? = null
    private lateinit var binding: ActivityListBinding
    private lateinit var redditsAdapter: ItemSubredditAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupDependencyInjection()

        setupToolbar()
        setupRecyclerViewWithReddits()
        setupSwipeRefreshLayout()
        setupPresenter();
    }

    private fun setupPresenter() {
        presenter.setView(this)
        presenter.setupConnectionBroadcastReceiver()
        presenter.getReddits(false)
//        presenter.getRedditList().observe(this, Observer<MutableList<Thing>> { result: MutableList<Thing>? -> })
    }

    private fun setupDependencyInjection() {
        (application as RedditApplication).appComponent?.inject(this)
    }

    private fun setupRecyclerViewWithReddits() {
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.recyclerView.layoutManager = linearLayoutManager

        redditsAdapter = ItemSubredditAdapter(ArrayList(), this, this)
        binding.recyclerView.adapter = redditsAdapter
        binding.recyclerView.itemAnimator = DefaultItemAnimator()
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (!binding.recyclerView.canScrollVertically(1)) {
                    presenter.getReddits(true)
                }
            }
        })
    }

    private fun setupSwipeRefreshLayout() {
        binding.swipeContainer.setOnRefreshListener {
            presenter.getReddits(false)
        }
        binding.swipeContainer.setColorSchemeResources(
                R.color.primary,
                R.color.accent)
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.primary_text))
        binding.toolbar.title = title
    }

    override fun onResume() {
        super.onResume()
        presenter.resume()
    }

    override fun onPause() {
        super.onPause()
        presenter.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
        snackbar = null
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    private fun hideSwipeRefreshing() {
        binding.swipeContainer.isRefreshing = false
    }

    private fun hideConnectionMessage() {
        if (snackbar != null && snackbar?.isShownOrQueued == true) {
            snackbar?.dismiss()
        }
    }

    override fun updateListOfReddits(listOfReddits: List<Thing>) {
        redditsAdapter.clear()
        redditsAdapter.setItems(listOfReddits)
        hideConnectionMessage()
        hideSwipeRefreshing()
    }

    override fun hideLoading() {
        binding.progress.visibility = View.INVISIBLE
    }

    override fun showLoading() {
        binding.progress.visibility = View.VISIBLE
    }

    override fun hideNoConnectionMessage() {
        binding.offlineMessage.visibility = View.GONE
    }

    override fun showNoConnectionMessage() {
        binding.offlineMessage.visibility = View.VISIBLE
    }

    override fun registerConnectionBroadcastReceiver(broadcastReceiver: BroadcastReceiver) {
        registerReceiver(broadcastReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun unRegisterConnectionBroadcastReceiver(broadcastReceiver: BroadcastReceiver) {
        unregisterReceiver(broadcastReceiver)
    }

    override fun showErrorDuringRequestMessage() {
        hideSwipeRefreshing()
        showRetrySnackbar(R.string.error_loading)
    }

    override fun showEmptyResponseMessage() {
        hideSwipeRefreshing()
        showRetrySnackbar(R.string.empty_response)
    }

    private fun showRetrySnackbar(@StringRes message: Int) {
        snackbar = Snackbar.make(binding.coordinator, message, Snackbar.LENGTH_LONG)
                .setAction(R.string.retry) { presenter.getReddits(false) }
        snackbar?.setActionTextColor(Color.WHITE)
        snackbar?.duration = BaseTransientBottomBar.LENGTH_INDEFINITE

        snackbar?.view?.let { snackbarView ->
            snackbarView.setBackgroundColor(ContextCompat.getColor(this, R.color.primary))
            val textView = snackbarView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
            textView.setTextColor(Color.WHITE)
        }

        snackbar?.show()
    }

    override fun onClick(view: View, thing: Thing) {
        presenter.onItemClicked(this, thing)
    }
}
