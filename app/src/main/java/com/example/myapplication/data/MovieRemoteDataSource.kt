package com.example.myapplication.data

import com.example.myapplication.model.MovieList
import com.example.myapplication.model.Result
import com.example.myapplication.network.services.MovieService
import com.example.myapplication.util.ErrorUtils
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

/**
 * fetches data from remote source
 */
class MovieRemoteDataSource @Inject constructor(private val retrofit: Retrofit) {

    suspend fun searchMovie(queryText: String?, page: Int?): Result<MovieList?> {
        val movieService = retrofit.create(MovieService::class.java);
        return getResponse(
            request = { movieService.searchMovie(queryText, "movie", page) },
            defaultErrorMessage = "Error fetching Movie list"
        )

    }

    private suspend fun <T> getResponse(
        request: suspend () -> Response<T>,
        defaultErrorMessage: String
    ): Result<T> {
        return try {
            val result = request.invoke()
            if (result.isSuccessful) {
                return Result.success(result.body())
            } else {
                val errorResponse = ErrorUtils.parseError(result, retrofit)
                Result.error(errorResponse?.status_message ?: defaultErrorMessage, errorResponse)
            }
        } catch (e: Throwable) {
            Result.error("Unknown Error", null)
        }
    }
}