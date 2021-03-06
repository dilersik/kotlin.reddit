package com.dilermando.reddit.data.logger

import android.util.Log

import timber.log.Timber

class LoggerTree : Timber.Tree() {

    override fun log(priority: Int, tag: String?, message: String, throwable: Throwable?) {
        if (priority == Log.VERBOSE || priority == Log.DEBUG) {
            return
        }

//        Crashlytics.setInt(CRASHLYTICS_KEY_PRIORITY, priority)
//        Crashlytics.setString(CRASHLYTICS_KEY_TAG, tag)
//        Crashlytics.setString(CRASHLYTICS_KEY_MESSAGE, message)

        if (throwable == null) {
//            Crashlytics.log(message)
            Timber.log(priority, message)
        } else {
            Timber.log(priority, throwable)
//            Crashlytics.logException(throwable)
        }

    }

    companion object {
        private val CRASHLYTICS_KEY_PRIORITY = "priority"
        private val CRASHLYTICS_KEY_TAG = "tag"
        private val CRASHLYTICS_KEY_MESSAGE = "message"
    }
}
