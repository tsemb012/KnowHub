package com.tsemb.droidsoftthird.repository

interface DataStoreRepository {
    companion object {
        const val TOKEN_ID_KEY = "token_id_key"
    }
    suspend fun saveTokenId(tokenId: String)
    suspend fun clearTokenId()
}
