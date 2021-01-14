package com.dilermando.reddit.presentation

interface BasePresenter {

    fun resume() {}

    fun pause() {}

    fun destroy() {}

    fun setView(view: BaseView) {}

}
