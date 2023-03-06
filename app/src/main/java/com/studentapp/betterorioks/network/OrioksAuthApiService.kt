package com.studentapp.betterorioks.network

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface OrioksAuthApiService {
    @POST("user/login")
    @FormUrlEncoded
    suspend fun auth(
        @Header("cookie")cookies:String,
        @Field("_csrf")csrf: String,
        @Field("LoginForm[login]")login:String,
        @Field("LoginForm[password]")password:String,
        @Field("LoginForm[rememberMe]")rememberMe:Int = 1
    ): Response<ResponseBody>
}