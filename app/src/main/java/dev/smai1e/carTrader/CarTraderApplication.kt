package dev.smai1e.carTrader

import android.app.Application
import android.content.Context
import dev.smai1e.carTrader.di.AppComponent
import dev.smai1e.carTrader.di.DaggerAppComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class CarTraderApplication : Application() {

    private val applicationScope = CoroutineScope(SupervisorJob())

    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .context(context = this)
            .applicationScope(scope = applicationScope)
            .build()
    }
}

val Context.appComponent: AppComponent
    get() = when (this) {
        is CarTraderApplication -> appComponent
        else -> applicationContext.appComponent
    }