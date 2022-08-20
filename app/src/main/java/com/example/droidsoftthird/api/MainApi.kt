package com.example.droidsoftthird.api

import com.example.droidsoftthird.model.json.UserJson
import com.example.droidsoftthird.model.request.PostSignUp
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface MainApi {
    @POST("/users/login")
    fun postTokenId(data: String)

    @POST("/users")
    fun postNewUser(@Body request: PostSignUp.Request): Response<UserJson>
}
