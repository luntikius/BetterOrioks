package com.studentapp.betterorioks.data

import android.util.Log
import com.studentapp.betterorioks.network.OrioksAuthApiService
import com.studentapp.betterorioks.network.OrioksAuthInfoApiService
import com.studentapp.betterorioks.network.OrioksSubjectsApiService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

class NetworkSubjectsFromSiteRepository
{
    private val BASE_URL =
        "https://orioks.miet.ru"

    object RequestInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain
                .request()
                .newBuilder()
                .addHeader("Accept" ,"text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                .addHeader("User-Agent","${AppDetails.appName}/${AppDetails.version} (Android)")
                .build()
            return chain.proceed(request)
        }
    }

    private val okHttpClient = OkHttpClient()
        .newBuilder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .followRedirects(false)
        .followSslRedirects(false)
        .addInterceptor(RequestInterceptor)
        .build()

    private val retrofitString = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .build()

    private val retrofitRequest = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(BASE_URL)
        .build()

    private val orioksAuthInfoRetrofitService: OrioksAuthInfoApiService by lazy {
        retrofitString.create(OrioksAuthInfoApiService::class.java)
    }

    private val orioksAuthRetrofitService: OrioksAuthApiService by lazy {
        retrofitRequest.create(OrioksAuthApiService::class.java)
    }

    private val orioksSubjectsApiService: OrioksSubjectsApiService by lazy{
        retrofitRequest.create(OrioksSubjectsApiService::class.java)
    }

    private fun getCookies (response: retrofit2.Response<ResponseBody>):String{
        return response.headers().values("set-cookie")
            .joinToString(separator = "; ") { s ->
                s.substring(
                    startIndex = 0,
                    endIndex = s.indexOf(";")
                )
            }
    }

    suspend fun getSubjects() {
        val authScreen = orioksAuthInfoRetrofitService.getAuthInfo()
        val authCookies = getCookies(authScreen)
        val body = authScreen.body()?.string()

        val csrf = body?.substring(
            startIndex = body.indexOf("name=\"_csrf\" value=\"")+20,
            endIndex = body
                .indexOf(
                    string = "\">",
                    startIndex = body.indexOf("name=\"_csrf\" value=\"")
                )
        ) ?: ""
        val authInfo = orioksAuthRetrofitService.auth(cookies = authCookies, csrf = csrf, login = "8211720", password = "20030615")
        val cookies = getCookies(authInfo)
        val subjectsHtml = orioksSubjectsApiService.getSubjects(cookies = cookies).string()
        val start = subjectsHtml.indexOf("\"dises\":")
        val end = subjectsHtml.indexOf(string = "\n",startIndex = start)
        val subjects = subjectsHtml.slice(start until end)
        Log.d("OrioksAuthInfo", subjects)
    }


}