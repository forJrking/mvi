package com.example.study.repo

/**
 * Created by 45166662 on 2022/02/25.
 */
interface DataRepository {

    suspend fun fetchData(): List<Contact>

    suspend fun fetchWeChat(): String
}