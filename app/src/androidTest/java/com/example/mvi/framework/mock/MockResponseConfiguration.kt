package com.example.mvi.framework.mock

import java.net.HttpURLConnection

data class MockResponseConfiguration(val responseList: List<JsonMockResponse>)

data class JsonMockResponse(
    val path: String,
    val code: Int = HttpURLConnection.HTTP_OK,
    val method: String? = "",
    val successResponsePath: String? = null,
    val errorResponsePath: String? = null,
)
