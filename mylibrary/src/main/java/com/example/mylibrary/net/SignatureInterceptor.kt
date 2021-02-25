package com.example.mylibrary.net

import com.example.mylibrary.net.BaseApi
import com.example.mylibrary.net.HttpDesUtils
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Buffer
import org.json.JSONObject
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

class SignatureInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        if (!BaseApi.BASE_URL.contains(request.url.host)) {
            return chain.proceed(request)
        }
        if (request.body != null) {
            if (request.method == "POST" && request.body!!.contentType().toString()
                    .contains("application/json")
            ) {
                val buffer = Buffer()
                request.body!!.writeTo(buffer)
                val contentType = request.body!!.contentType()
                val charset: Charset =
                    contentType?.charset(StandardCharsets.UTF_8) ?: StandardCharsets.UTF_8
                val readString = buffer.readString(charset)
                request = request.newBuilder()
                    .post(HttpSignatureUtils.sign(readString, request.url.toString())).build()

            } else if (request.body is FormBody) {
                val body = request.body as FormBody
                val jsonObjects = JSONObject()
                for (i in 0 until body.size) {
                    jsonObjects.put(body.name(i), body.value(i))
                }
                request = request.newBuilder()
                    .post(HttpSignatureUtils.sign(jsonObjects.toString(), request.url.toString())).build()
            }
        }else if (request.body ==null){
            val jsonObjects = JSONObject()
            request = request.newBuilder()
                .post(HttpSignatureUtils.sign(jsonObjects.toString(), request.url.toString())).build()
        }
        var response = chain.proceed(request)
        if (response.body!=null&&response.body!!.contentLength()>0) {
            response= response.newBuilder().body(HttpDesUtils.decrypt(response.body!!.string()).toResponseBody(HttpSignatureUtils.JSON_MEDIA_TYPE)).build()
        }
        return response
    }
}