package com.example.myapplication.util

import retrofit2.Response
import retrofit2.Retrofit
import com.example.myapplication.model.Error
import java.io.IOException

/**
 * parses error response body
 */
object ErrorUtils {

    fun parseError(response: Response<*>, retrofit: Retrofit): Error? {
        val converter = retrofit.responseBodyConverter<Error>(Error::class.java, arrayOfNulls(0))
        return try {
            converter.convert(response.errorBody()!!)
        } catch (e: IOException) {
            Error()
        }
    }
}