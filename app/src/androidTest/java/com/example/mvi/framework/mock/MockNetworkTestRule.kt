package com.example.mvi.framework.mock

import com.example.mvi.framework.base.MockTestRule
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import java.net.HttpURLConnection
import java.net.URLDecoder

object MockNetworkTestRule : MockTestRule {

    private var mockWebServer: MockWebServer = MockWebServer()

    override fun beforeEachTest() {
        mockWebServer = MockWebServer()
        mockWebServer.start(8000)
    }

    override fun afterEachTest() {
        mockWebServer.shutdown()
    }

    fun setUp(configuration: MockResponseConfiguration) {
        setDispatcher(object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return configuration.responseList.firstOrNull {
                    it.path.matchPath(request.path.orEmpty()) && it.method.matchMethod(request.method.orEmpty())
                }?.let {
                    getMockDataResponse(
                        httpCode = it.code,
                        resourceName = (if (it.code == HttpURLConnection.HTTP_OK) it.successResponsePath else it.errorResponsePath).orEmpty()
                    )
                } ?: MockResponse()
                    .setResponseCode(HttpURLConnection.HTTP_NOT_FOUND)
                    .setBody("Don't configs ${request.path} MockResponse")
            }
        })
    }

    fun setDispatcher(dispatcher: Dispatcher) {
        mockWebServer.dispatcher = dispatcher
    }


    /**
     * This method will respond with a mock response
     * @param httpCode as HTTP response code
     * @param resourceName the response body contents
     * */
    private fun getMockDataResponse(httpCode: Int, resourceName: String): MockResponse? {
        val responseBody = readResourceOnClasspath(resourceName)
        return responseBody?.let {
            MockResponse()
                .setResponseCode(httpCode)
                .setBody(it)
        }
    }

    /**
     * url match with original & decode
     */
    private fun String.matchPath(encodedPath: String) =
        encodedPath.contains(this, ignoreCase = true) ||
                URLDecoder.decode(encodedPath, "UTF-8").contains(this, ignoreCase = true)

    private fun String?.matchMethod(method: String) =
        this.isNullOrEmpty() || method.equals(this, ignoreCase = true)

    /**
     * The method reads and returns the contents of
     * @param resourceName file name for the response contents
     * */
    private fun readResourceOnClasspath(resourceName: String): String? {
        this.javaClass.getResourceAsStream(resourceName).use { return it?.reader()?.readText() }
    }
}
