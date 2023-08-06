
package com.tsemb.droidsoftthird


import android.app.Application
import com.example.droidsoftthird.BuildConfig.MAPS_API_KEY
import com.google.android.libraries.places.api.Places
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import timber.log.Timber.DebugTree

@HiltAndroidApp//Trigger generating Code of Hilt
class DroidSoftThird: Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(DebugTree())
        AndroidThreeTen.init(this)//APIレベル23のデバイスでもjava.timeパッケージの機能を利用できるようにする
        Places.initialize(applicationContext, MAPS_API_KEY)
    }
}

