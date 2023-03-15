package com.example.myapplication.data

import com.example.myapplication.model.MovieList
import com.example.myapplication.model.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 * Repository which fetches data from Remote or Local data sources
 */
class MovieRepository @Inject constructor(
    private val movieRemoteDataSource: MovieRemoteDataSource,
) {

    suspend fun searchMovie(queryText: String, page: Int?): Flow<Result<MovieList?>?> {
        return flow {
            emit(Result.loading())
            val result = movieRemoteDataSource.searchMovie(queryText, page)
            emit(result)
        }.flowOn(Dispatchers.IO)
    }
}