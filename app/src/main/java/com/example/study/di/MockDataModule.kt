package com.example.study.di

import com.example.study.di.DispatcherModule.IO
import com.example.study.repo.DataProviders
import com.example.study.repo.DataRepository
import com.example.study.repo.Contact
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Named
import kotlin.time.Duration.Companion.seconds

/**
 * Created by 45166662 on 2022/02/25.
 */
@Module
@InstallIn(ViewModelComponent::class)
object MockDataModule {

    @Provides
    fun provideMockRepository(
        @Named(IO) ioDispatchers: CoroutineDispatcher
    ): DataRepository = object : DataRepository {
        override suspend fun fetchData(): List<Contact> =
            withContext(ioDispatchers) { DataProviders.items }

        override suspend fun fetchWeChat(): String = withContext(ioDispatchers) {
            delay(3.seconds)
            """朋友圈一般指的是腾讯微信上的一个社交功能，于微信4.0版本2012年4月19日更新时上线用户可以通过朋友圈发表文字和图片，同时可通过其他软件将文章或者音乐分享到朋友圈。 用户可以对好友新发的照片进行“评论”或“赞”，其他用户只能看相同好友的评论或赞。"""
        }
    }
}