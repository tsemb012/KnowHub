
package com.example.droidsoftthird


import android.app.Application
import com.example.droidsoftthird.BuildConfig.MAPS_API_KEY
import com.google.android.libraries.places.api.Places
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import timber.log.Timber.DebugTree

@HiltAndroidApp//Trigger generating Code of Hilt
class DroidSoftThird: Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(DebugTree())

        // Initialize the SDK
        Places.initialize(applicationContext, MAPS_API_KEY)
    }
/*    // Resource Provider
    private var mResourceProvider: ResourceProvider? = null

    val resourceProvider: ResourceProvider?
        get() {
            if (mResourceProvider == null) mResourceProvider = ResourceProvider(this)
            return mResourceProvider
        }*/
}

