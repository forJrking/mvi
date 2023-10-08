package com.spider.composes

import android.app.Application
import com.spider.composes.di.dispatcherModule
import com.spider.composes.model.engine.soundModule
import com.spider.composes.viewmodel.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

/**
 * @description:
 * @author: forjrking
 * @date: 2023/9/23 17:50
 */
class GameApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@GameApp)
            modules(dispatcherModule, viewModelModule, soundModule)
        }
    }
}