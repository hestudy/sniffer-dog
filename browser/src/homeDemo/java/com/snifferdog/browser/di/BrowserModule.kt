package com.snifferdog.browser.di

import com.snifferdog.browser.BrowserEngine
import com.snifferdog.browser.StubBrowserEngine
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class BrowserModule {
    @Binds
    @Singleton
    abstract fun bindBrowserEngine(impl: StubBrowserEngine): BrowserEngine
}
