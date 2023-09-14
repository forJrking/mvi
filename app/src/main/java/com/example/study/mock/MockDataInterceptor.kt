//package com.example.study.mock
//
//import android.net.wifi.WifiConfiguration
//import android.util.Log
//import com.google.android.gms.common.api.Response
//import okhttp3.Interceptor
//import okhttp3.MediaType.Companion.toMediaTypeOrNull
//import okhttp3.Protocol
//import okhttp3.Response
//import okhttp3.ResponseBody.Companion.toResponseBody
//import java.net.HttpURLConnection
//import java.net.URLDecoder
//
///**
// * Mock data with json configs : assets/entityName/mock/mockConfig.json
// */
//class MockDataInterceptor(
//    private var mockResponseConfigList: MockResponseConfiguration? = null
//) : Interceptor {
//
//    companion object {
//        const val MOCK_HOST = "localhost:8000"
//        const val CONTENT_TYPE = "application/json"
//
//        /**
//         * url match with original & decode
//         */
//        fun String.matchPath(encodedPath: String) =
//            encodedPath.contains(this, ignoreCase = true) ||
//                    URLDecoder.decode(encodedPath, "UTF-8").contains(this, ignoreCase = true)
//
//        fun String?.matchMethod(method: String) =
//            this.isNullOrEmpty() || method.equals(this, ignoreCase = true)
//    }
//
//    override fun intercept(chain: Interceptor.Chain): Response {
//        return findMockResponse(chain) ?: chain.proceed(chain.request())
//    }
//
//    private fun findMockResponse(chain: Interceptor.Chain): Response? {
//        val request = chain.request()
//        return mockResponseConfigList?.responseList?.firstOrNull {
//            it.path.matchPath(request.url.encodedPath) && it.method.matchMethod(request.method)
//        }?.run {
//            val resourceName =
//                (if (code == HttpURLConnection.HTTP_OK) successResponsePath else errorResponsePath).orEmpty()
//            javaClass.getResourceAsStream(resourceName)?.use {
//                Log.i("okhttp.OkHttpClient", "mock data request success: $request")
//                Response.Builder()
//                    .code(code)
//                    .message(code.toString())
//                    .request(request)
//                    .protocol(WifiConfiguration.Protocol.HTTP_1_0)
//                    .body(it.readBytes().toResponseBody(CONTENT_TYPE.toMediaTypeOrNull()))
//                    .addHeader("content-type", CONTENT_TYPE)
//                    .build()
//            }
//        }
//    }
//}
