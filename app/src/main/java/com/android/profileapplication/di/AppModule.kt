package com.android.profileapplication.di

import com.android.profileapplication.data.remote.repository.FirebaseAuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideFirebaseRepository(): FirebaseAuthRepository {
        return FirebaseAuthRepository()
    }
}