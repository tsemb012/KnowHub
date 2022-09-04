package com.example.droidsoftthird.di

import com.example.droidsoftthird.api.MainApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    /*TODO インターセプターでトークンを入れるようにする。*/
/*    @Provides
    @Singleton
    fun provideHeaderInterceptor(dataStore: DataStore<Preferences>) : HeaderInterceptor {
        return HeaderInterceptor(dataStore)
    }

    class HeaderInterceptor(val dataStore: DataStore<Preferences>) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response = chain.run {
            val requestBuilder = request().newBuilder()
                .addHeader("Accept", "application/json")
                .addHeader("deviceplatform", "android")
                .addHeader("User-Agent", "DroidSoftThird")
            val flow = dataStore.data.map { preferences ->
                preferences[stringPreferencesKey(DataStoreRepository.TOKEN_ID_KEY)]
            }
            var tokenA: String? = null
            runBlocking {
                flow.collect { token -> tokenA = token }
                proceed(//TODO DataStoreの値を入れる。Interceptorの書き方が汚いので綺麗にする。
                    if (tokenA != null) {
                        requestBuilder.addHeader("Authorization", "Bearer $tokenA")
                    } else {
                        requestBuilder
                    }
                        .build()
                )
            }
        }
    }*/

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {//interceptor: HeaderInterceptor
        return OkHttpClient.Builder()
            //.addInterceptor(interceptor)
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
    }//TODO サーバーとパスワードやりとりをする方法を調べる。

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
