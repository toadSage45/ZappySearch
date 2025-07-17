package com.example.zappysearch.data.repository

import retrofit2.Response
import retrofit2.http.*

data class GeoPostBody(val postId: String, val lat: Double, val long: Double)
data class NearbyPostsResponse(val postIds: List<String>)

interface GeoPostApi {

    @POST("api/save-geo-post")
    suspend fun saveGeoPost(@Body body: GeoPostBody): Response<Unit>

    @GET("api/nearby")
    suspend fun getNearbyPosts(
        @Query("lat") lat: Double,
        @Query("long") long: Double,
        @Query("radius") radius: Double
    ): Response<NearbyPostsResponse>

    @DELETE("api/delete/{postId}")
    suspend fun deleteGeoPost(@Path("postId") postId: String): Response<Unit>
}
