package com.example.droidsoftthird

import com.tsemb.droidsoftthird.model.domain_model.ViewPort
import com.tsemb.droidsoftthird.repository.BaseRepositoryImpl
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import javax.inject.Inject

@HiltAndroidTest
class MapTest{
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repository: BaseRepositoryImpl
    val viewPort = ViewPort(northEast = LatLng(35.659281, 139.746433), southWest = LatLng(35.658581, 139.745433))

    @Before
    fun init() {
        hiltRule.inject()
    }

}