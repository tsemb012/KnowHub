package com.tsemb.droidsoftthird.repository

import com.tsemb.droidsoftthird.repository.csvloader.CityCsvLoader
import com.tsemb.droidsoftthird.repository.csvloader.PrefectureCsvLoader

interface AssetRepository {
    suspend fun loadPrefectureCsv(): List<PrefectureCsvLoader.PrefectureLocalItem>
    suspend fun loadCityCsv(): List<CityCsvLoader.CityLocalItem>
}
