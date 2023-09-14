package com.example.mvi.di

import com.example.mvi.repo.DataRepository
import com.example.mvi.repo.DataRepositoryImpl
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