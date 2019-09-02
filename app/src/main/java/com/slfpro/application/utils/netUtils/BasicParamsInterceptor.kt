package com.slfpro.application.utils.netUtils

import com.orhanobut.logger.Logger
import com.slfpro.application.utils.DateUtils
import com.slfpro.application.utils.empty
import com.slfpro.application.utils.printJsonData
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okio.Buffer

class BasicParamsInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val url = request.url().newBuilder()
        if (request.method() == "GET") {
            //添加get公共参数
            /*
            url.addQueryParameter("user_token", AppConfig.getUserToken())
            url.addQueryParameter("device", NetWorkConfig.DEVICE)
            url.addQueryParameter("version_code", NetWorkConfig.VERSION_CODE)
            */
        }

        //头部信息
        val authorization = StringBuffer()
        //添加头部参数
        /*
        authorization.append("Token token=\"${AppConfig.getUserToken()}\", ")
        authorization.append("device=\"${NetWorkConfig.DEVICE}\", version_code=\"${NetWorkConfig.VERSION_CODE}\"")
        */
        val requestBuilder = request.newBuilder()
        requestBuilder.addHeader("Content-Type", "application/json;charset=UTF-8")
        requestBuilder.addHeader("Authorization", authorization.toString())

        requestBuilder.url(url.build())
        request = requestBuilder.build()
        val startNs = DateUtils.instance.getNowMills()
        val response = chain.proceed(request)
        val tookMs = DateUtils.instance.getNowMills() - startNs
        getNetWorkInfo(request, response, tookMs)
        return response
    }

    private fun getNetWorkInfo(request: Request, response: Response, tookMs: Long) {
        val sb = StringBuffer()
        val url = request.url()
        //url
        sb.append("Request Url: \n$url ($tookMs-ms) \n \n")

        //requestQuery
        if (!url.encodedQuery().empty()) {
            val queryDataString = "{\"" +
                    url.encodedQuery()!!
                        .replace("=", "\":\"")
                        .replace("&", "\",\"") +
                    "\"}"
            sb.append().append("Query Data: ${queryDataString.printJsonData()} \n \n")
        }

        //requestBody
        val requestBody = request.body()
        var requestBodyString: String? = ""
        if (requestBody != null) {
            val requestBuffer = Buffer()
            requestBody.writeTo(requestBuffer)
            val contentType = requestBody.contentType()
            var charset = charset("UTF8")
            try {
                charset = contentType?.charset(charset)!!
            } catch (e: Exception) {
                Logger.e("charset error ${e.message}")
            }
            requestBodyString = requestBuffer.readString(charset)
            sb.append().append("Request Body: ${requestBodyString.printJsonData()} \n \n")
        }

        //responseBody
        val responseBody = response.body()
        var responseBodyString: String? = ""
        if (responseBody != null) {
            val source = responseBody.source()
            source.request(Long.MAX_VALUE) // Buffer the entire body.
            val responseBuffer = source.buffer()
            var charset = charset("UTF8")
            val contentType = responseBody.contentType()
            try {
                charset = contentType?.charset(charset)!!
            } catch (e: Exception) {
                Logger.e("charset error ${e.message}")
            }
            responseBodyString = responseBuffer?.clone()?.readString(charset)
            sb.append().append("Response Body: ${responseBodyString?.printJsonData()} \n")
        }
        Logger.d(sb.toString())
    }
}