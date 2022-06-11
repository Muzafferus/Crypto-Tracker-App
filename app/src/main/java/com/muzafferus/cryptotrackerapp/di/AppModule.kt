package com.muzafferus.cryptotrackerapp.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.muzafferus.cryptotrackerapp.data.local.AlertDao
import com.muzafferus.cryptotrackerapp.data.local.AppDatabase
import com.muzafferus.cryptotrackerapp.data.remote.CryptoRemoteDataSource
import com.muzafferus.cryptotrackerapp.data.remote.CryptoService
import com.muzafferus.cryptotrackerapp.data.repository.AlertRepository
import com.muzafferus.cryptotrackerapp.data.repository.CryptoRepository
import com.muzafferus.cryptotrackerapp.util.Utility
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson, httpClient: OkHttpClient.Builder): Retrofit = Retrofit.Builder()
        .baseUrl(Utility.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(httpClient.build())
        .build()

    @Provides
    fun provideOkHttpClientBuilder(): OkHttpClient.Builder {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)
        return httpClient
    }

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    fun provideCryptoService(retrofit: Retrofit): CryptoService =
        retrofit.create(CryptoService::class.java)

    @Singleton
    @Provides
    fun provideCryptoRemoteDataSource(cryptoService: CryptoService) =
        CryptoRemoteDataSource(cryptoService)

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) =
        AppDatabase.getDatabase(appContext)

    @Singleton
    @Provides
    fun provideAlertDao(db: AppDatabase) = db.alertDao()

    @Singleton
    @Provides
    fun provideAlertRepository(localDataSource: AlertDao) = AlertRepository(localDataSource)

    @Singleton
    @Provides
    fun provideCryptoRepository(remoteDataSource: CryptoRemoteDataSource) =
        CryptoRepository(remoteDataSource)
}