package com.example.newsify.di

import android.content.Context
import com.example.newsify.data.local.UserDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Singleton
    @Provides
    fun provideUserDataBase(@ApplicationContext context: Context): UserDataBase {
        return UserDataBase.getInstance(context)
    }
}