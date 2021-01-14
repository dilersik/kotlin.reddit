package com.dilermando.reddit.presentation.splash.view

import android.content.Intent
import android.os.Bundle

import com.dilermando.reddit.R
import com.dilermando.reddit.RedditApplication
import com.dilermando.reddit.presentation.BaseActivity
import com.dilermando.reddit.presentation.list.view.ListActivity
import com.dilermando.reddit.presentation.splash.SplashContract

import javax.inject.Inject

class SplashActivity : BaseActivity(), SplashContract.View {

    @Inject
    lateinit var presenter: SplashContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupDependencyInjection()
        setContentView(R.layout.activity_splash)
        presenter.setView(this)
        presenter.setupTransition()
    }

    override fun gotoListActivity() {
        startActivity(Intent(this@SplashActivity, ListActivity::class.java))
        finish()
    }

    private fun setupDependencyInjection() {
        (application as RedditApplication).appComponent?.inject(this)
    }
}
