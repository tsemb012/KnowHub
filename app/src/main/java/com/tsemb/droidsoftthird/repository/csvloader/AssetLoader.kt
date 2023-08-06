package com.tsemb.droidsoftthird.repository.csvloader

import javax.inject.Inject

class AssetLoader @Inject constructor(
    val prefectureCsvLoader: PrefectureCsvLoader,
    val cityCsvLoader: CityCsvLoader
)
