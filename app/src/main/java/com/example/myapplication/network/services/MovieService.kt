package com.example.myapplication.network.services

import com.example.myapplication.model.MovieList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit API Service
 */
interface MovieService {

    @GET("/")
    suspend fun searchMovie(@Query("s")  query: String?, @Query("type")  type: String?, @Query("page")  page: Int?) : Response<MovieList?>

}