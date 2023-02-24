package com.example.droidsoftthird

import com.example.droidsoftthird.model.domain_model.ViewPort
import com.example.droidsoftthird.repository.BaseRepositoryImpl
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.core.test.TestCase
import org.junit.Rule
import javax.inject.Inject


@HiltAndroidTest
class MapUseCaseSpec: DescribeSpec(){
    @get:Rule
    val hiltRule = HiltAndroidRule(this)
    @Inject
    lateinit var repository: BaseRepositoryImpl
    val viewPort = ViewPort(northEast = LatLng(35.659281, 139.746433), southWest = LatLng(35.658581, 139.745433))

    override fun beforeTest(f: suspend (TestCase) -> Unit) {
        //super.beforeTest(f)
        hiltRule.inject()
    }

    init {
        describe("MapUseCase") {
            context("searchPlaces") {
                it("should return a list of places") {
                    repository.searchIndividualPlace("ラーメン", viewPort)

                }
            }
        }
    }
}