package com.example.droidsoftthird.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.droidsoftthird.api.MainApi
import com.example.droidsoftthird.repository.AuthenticationRepositoryImpl
import com.squareup.moshi.*
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    companion object {
        //private const val BASE_URL = "http://10.0.2.2:3000/" //エミュレーターで起動する場合
        private const val BASE_URL = "http://192.168.10.104:3000/"
        //private const val BASE_URL = "http://192.168.200.21:3000/"
        private const val TOKEN_ID_KEY = "token_id_key"
    }

    @Provides
    @Singleton
    fun provideHeaderInterceptor(dataStore: DataStore<Preferences>) : HeaderInterceptor = HeaderInterceptor(dataStore)

    @Provides
    @Singleton
    fun provideJwtAuthenticator(repository: AuthenticationRepositoryImpl) : JwtAuthenticator = JwtAuthenticator(repository)
    //TODO 本当はRailsとFirebaseに分離させたかったが、インターフェース経由で依存関係を注入する方法がわからなかったのでそのまま入れ込んでいる。

    @Provides
    @Singleton
    fun provideOkHttpClient(interceptor: HeaderInterceptor, authenticator: JwtAuthenticator): OkHttpClient {//clientがhttpリクエストを受けたり、おくっったりする。
        return OkHttpClient.Builder()
            .authenticator(authenticator)
            .addInterceptor(interceptor)
            .connectTimeout(40, TimeUnit.SECONDS)
            .readTimeout(40, TimeUnit.SECONDS)
            .writeTimeout(40, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(LocalTimeAdapter)
        .add(LocalDateAdapter)
        .addLast(KotlinJsonAdapterFactory()).build()

    @Singleton
    @Provides
    fun providerRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(BASE_URL)
            .build()
    }

    @Provides
    @Singleton
    fun provideMainApi(retrofit: Retrofit): MainApi = retrofit.create(MainApi::class.java)

    class HeaderInterceptor(private val dataStore: DataStore<Preferences>) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response = chain.run {
            val token = runBlocking {
                dataStore.data.map { preferences ->
                    preferences[stringPreferencesKey(TOKEN_ID_KEY)]
                }.first()
            }
            val request = request().newBuilder().apply {
                if (!token.isNullOrBlank()) addHeader("Authorization", "Bearer $token")
            }.build()
            proceed(request)
        }
    }

    class JwtAuthenticator(
            private val repository: AuthenticationRepositoryImpl,
    ): Authenticator {
        private val Response.retryCount: Int
            get() {
                var currentResponse = priorResponse
                var result = 1
                while (currentResponse != null) {
                    result++
                    currentResponse = currentResponse.priorResponse
                }
                return result
            }

        override fun authenticate(route: Route?, response: Response): Request? = when {
            response.retryCount >= 3 -> null
            else -> {
                runBlocking {
                    val token = async { repository.refreshToken() }
                    token.await()?.let { repository.saveTokenId(it) }
                }
                response.request
            }
        }
    }
}

//TODO 適切な場所に移動するように
object LocalTimeAdapter {
    @ToJson
    fun toJson(value: LocalTime): String {
        return value.format(DateTimeFormatter.ISO_LOCAL_TIME)
    }

    @FromJson
    fun fromJson(value: String): LocalTime {
        return LocalTime.parse(value, DateTimeFormatter.ISO_LOCAL_TIME)
    }
}

object LocalDateAdapter {
    @ToJson
    fun toJson(value: LocalDate): String {
        return value.toString()
    }

    @FromJson
    fun fromJson(value: String): LocalDate {
        return LocalDate.parse(value)
    }
}
