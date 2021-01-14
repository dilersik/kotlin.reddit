package com.dilermando.reddit.domain.di

import com.dilermando.reddit.presentation.details.DetailsActivity
import com.dilermando.reddit.presentation.list.view.ListActivity
import com.dilermando.reddit.presentation.splash.view.SplashActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(target: ListActivity)

    fun inject(target: SplashActivity)

    fun inject(target: DetailsActivity)
}
