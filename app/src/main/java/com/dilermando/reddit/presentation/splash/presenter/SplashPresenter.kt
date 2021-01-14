package com.dilermando.reddit.presentation.splash.presenter

import com.dilermando.reddit.presentation.BaseView
import com.dilermando.reddit.presentation.splash.SplashContract

class SplashPresenter : SplashContract.Presenter {

    private var view: SplashContract.View? = null

    override fun setView(view: BaseView) {
        this.view = view as SplashContract.View
    }

    override fun setupTransition() {
        view?.gotoListActivity()
    }
}
