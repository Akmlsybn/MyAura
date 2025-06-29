package com.example.myaura.data.remote

import okhttp3.MultipartBody
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ImgurApiService {
    @Multipart
    @POST("3/image")
    suspend fun uploadImage(
        @Header("Authorization") clientId: String,
        @Part image: MultipartBody.Part
    ): ImgurResponse
}