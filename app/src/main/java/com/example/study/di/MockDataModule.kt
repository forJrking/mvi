package com.example.study.di

import com.example.study.repo.DataRepository
import com.example.study.repo.DataRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

/**
 * Created by 45166662 on 2022/02/25.
 */
@Module
@InstallIn(ViewModelComponent::class)
interface MockDataModule {

    @Binds
    fun bindsMockRepository(repository: DataRepositoryImpl): DataRepository
}