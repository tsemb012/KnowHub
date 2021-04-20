
package com.example.droidsoftthird


import android.app.Application
import timber.log.Timber
import timber.log.Timber.DebugTree


class DroidSoftThird: Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(DebugTree())
    }
/*    // Resource Provider
    private var mResourceProvider: ResourceProvider? = null

    val resourceProvider: ResourceProvider?
        get() {
            if (mResourceProvider == null) mResourceProvider = ResourceProvider(this)
            return mResourceProvider
        }*/
}

