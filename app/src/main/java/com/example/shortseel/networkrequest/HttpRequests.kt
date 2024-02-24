package com.example.shortseel.networkrequest

import com.example.shortseel.model.VideoItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface HttpRequests {
    @GET("/videos")
    suspend fun getVideos(@Query("page")pageNumber: Int):Response<VideoItem>
}