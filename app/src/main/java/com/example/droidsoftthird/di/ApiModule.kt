package com.example.droidsoftthird.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.droidsoftthird.api.MainApi
import com.example.droidsoftthird.repository.DataStoreRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    @Provides
    @Singleton
    fun provideHeaderInterceptor(dataStore: DataStore<Preferences>) : HeaderInterceptor {
        return HeaderInterceptor(dataStore)
    }

    class HeaderInterceptor(val dataStore: DataStore<Preferences>) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response = chain.run {
            val token = runBlocking {
                dataStore.data.map { preferences ->
                    preferences[stringPreferencesKey("token")]
                }.first()
            }
            val request = request().newBuilder().apply {
                if (!token.isNullOrBlank()) addHeader("Authorization", "Bearer $token")
            }.build()
            proceed(request)
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(interceptor: HeaderInterceptor): OkHttpClient {//clientがhttpリクエストを受けたり、おくっったりする。
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .connectTimeout(40, TimeUnit.SECONDS)
            .readTimeout(40, TimeUnit.SECONDS)
            .writeTimeout(40, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun providerRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideMainApi(retrofit: Retrofit): MainApi = retrofit.create(MainApi::class.java)

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()

    companion object {
        private const val BASE_URL = "http://10.0.2.2:3000/"
    }
}
