package com.example.droidsoftthird.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine


class AuthenticationRepositoryImpl @Inject constructor(
        private val dataStore: DataStore<Preferences>
): AuthenticationRepository {

    override suspend fun refreshToken(): String =
        suspendCoroutine { continuation ->
            FirebaseAuth.getInstance().currentUser?.getIdToken(true)?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val idToken = task.result?.token ?: throw Exception("idToken is null")
                    continuation.resumeWith(Result.success(idToken))
                } else {
                    continuation.resumeWith(Result.failure(task.exception!!))
                }
            }
        }

    override suspend fun saveTokenId(tokenId: String) {
        dataStore.edit { preferences ->
            preferences[stringPreferencesKey(DataStoreRepository.TOKEN_ID_KEY)] = tokenId
        }
    }
}
