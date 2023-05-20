package com.android.profileapplication.di

import android.content.Context
import com.android.profileapplication.utility.ResourceHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideResourceHelper(@ApplicationContext context: Context): ResourceHelper {
        return ResourceHelper(context)
    }
}