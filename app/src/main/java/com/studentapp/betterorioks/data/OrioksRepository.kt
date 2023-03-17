package com.studentapp.betterorioks.data

import com.studentapp.betterorioks.model.subjectsFromSite.ResourceCategory
import com.studentapp.betterorioks.model.subjectsFromSite.Resource
import com.studentapp.betterorioks.model.subjectsFromSite.SubjectsData
import com.studentapp.betterorioks.model.subjectsFromSite.SubjectsData2
import com.studentapp.betterorioks.network.OrioksAuthApiService
import com.studentapp.betterorioks.network.OrioksAuthInfoApiService
import com.studentapp.betterorioks.network.OrioksSubjectsApiService
import com.studentapp.betterorioks.network.ResourcesApiService
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody
import org.jsoup.Jsoup
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

class NetworkOrioksRepository
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
        .writeTimeout(10, TimeUnit.SECONDS)
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

    private val orioksSubjectsRetrofitService: OrioksSubjectsApiService by lazy{
        retrofitRequest.create(OrioksSubjectsApiService::class.java)
    }

    private val orioksResourcesRetrofitService: ResourcesApiService by lazy {
        retrofitRequest.create(ResourcesApiService::class.java)
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

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        isLenient = true
    }

    private fun findCsrf(page: String): String{
        val start = page.indexOf("csrf-token") + 21
        val end = page.indexOf(startIndex = start, string = "\">")
        return page.slice(start until end)
    }
    suspend fun auth(login: String, password: String, setCookies: (String, String) -> Unit):String {

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
        setCookies("",csrf)
        val authInfo = orioksAuthRetrofitService.auth(cookies = authCookies, csrf = csrf, login = login, password = password)
        val cookies = getCookies(authInfo)
        return (cookies)
    }

    private fun parseString(s: String): SubjectsData{
        return if (s[s.indexOf(":")+1] == "{".toCharArray()[0]){
            val temp = json.decodeFromString<SubjectsData2>(s)
            SubjectsData(subjects = temp.subjects.values.toList(), debts = temp.debts)
        } else json.decodeFromString(s)
    }

    suspend fun getSubjects(cookies: String, setCookies: (String, String) -> Unit = { _: String, _: String -> } ):SubjectsData{
        val response = orioksSubjectsRetrofitService.getSubjects(cookies = cookies)
        val subjectsHtml = response.body()?.string() ?: ""
        if("{\"dises\":" !in subjectsHtml) throw Throwable("Auth Error")
        val start = subjectsHtml.indexOf("{\"dises\":")
        val end = subjectsHtml.indexOf(string = "</",startIndex = start)
        val subjectsString = subjectsHtml.slice(start until end)
        val subjects = parseString(subjectsString)
        val newCookies = getCookies(response)
        val csrf = findCsrf(subjectsHtml)
        setCookies(newCookies, csrf)
        return (subjects)
    }

    suspend fun getResources(disciplineId: Int, scienceId: Int, cookies: String): List<ResourceCategory>{
        val response = orioksResourcesRetrofitService.getResources(
            cookies = cookies,
            query = mapOf("id_dis" to disciplineId.toString(), "id_science" to scienceId.toString()),
        ).string()
        val result = mutableListOf<ResourceCategory>()
        Jsoup.parse(response).body().getElementsByClass("list-group").forEach{
            val categoryName = it.getElementsByClass("list-group-item bg-grey pointer").text()
            val resources = mutableListOf<Resource>()
            val divElement = it.getElementsByClass("panel-collapse collapse").first()
            divElement?.children()?.forEach{element ->
                resources.add(
                    Resource(
                        name = element.getElementsByTag("a").text(),
                        link = element.getElementsByTag("a").attr("href"),
                        type = element.getElementsByClass("label").text()
                        )
                )
            }
            result.add(
                ResourceCategory(
                    name = categoryName,
                    resources = resources
                )
            )
        }
        return result
    }
}