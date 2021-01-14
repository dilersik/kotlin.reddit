package com.dilermando.reddit.presentation.splash

import com.dilermando.reddit.presentation.BasePresenter
import com.dilermando.reddit.presentation.BaseView

interface SplashContract {

    interface Presenter : BasePresenter {

        fun setupTransition()
    }

    interface View : BaseView {

        fun gotoListActivity()
    }
}
