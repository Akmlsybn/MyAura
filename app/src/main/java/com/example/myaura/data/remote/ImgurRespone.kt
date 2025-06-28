package com.example.myaura.data.remote

import com.google.gson.annotations.SerializedName

data class ImgurResponse(
    @SerializedName("data") val data: ImgurData,
    @SerializedName("success") val success: Boolean
)

data class ImgurData(
    @SerializedName("link") val link: String
)