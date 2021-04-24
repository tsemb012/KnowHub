
package com.example.droidsoftthird


import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import timber.log.Timber.DebugTree

@HiltAndroidApp//Trigger generating Code of Hilt
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

