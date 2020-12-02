package com.example.postslist

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import retrofit2.http.*

public interface MyApi {
    @GET("/posts")
    fun get() : Call<List<Post>>

    @POST("/posts")
    @FormUrlEncoded
    fun doPost(@Field("title") title : String, @Field("body") body : String, @Field("userId") userId : Long) : Call<Post>

    @DELETE("/posts/{id}")
    fun delete(@Path("id") id : Long) : Call<Any>
}

fun makeApi() : MyApi {
    val url = "https://jsonplaceholder.typicode.com"
    val retrofit : Retrofit = Retrofit.Builder().baseUrl(url).addConverterFactory(MoshiConverterFactory.create()).build()
    val api : MyApi = retrofit.create(MyApi::class.java)
    return api
}