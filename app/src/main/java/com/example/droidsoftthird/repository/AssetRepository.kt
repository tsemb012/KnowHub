package com.example.droidsoftthird.repository

import com.example.droidsoftthird.repository.csvloader.CityCsvLoader
import com.example.droidsoftthird.repository.csvloader.PrefectureCsvLoader

interface AssetRepository {
    suspend fun loadPrefectureCsv(): List<PrefectureCsvLoader.PrefectureLocalItem>
    suspend fun loadCityCsv(): List<CityCsvLoader.CityLocalItem>
}
