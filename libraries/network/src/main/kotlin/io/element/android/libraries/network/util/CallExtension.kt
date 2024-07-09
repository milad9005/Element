/*
 * Copyright (c) 2024 New Vector Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.element.android.libraries.network.util

import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend fun <T> Call<T>.safeExecute(): ApiResponse<T> = suspendCoroutine { continuation ->
    enqueue(object : Callback<T> {
        override fun onResponse(p0: Call<T>, response: Response<T>) {
            if (response.isSuccessful) {
                continuation.resume(ApiResponse.Success(response.body()!!, response.headers()))
            } else {
                continuation.resume(ApiResponse.Error(HttpException(response)))
            }
        }

        override fun onFailure(p0: Call<T>, p1: Throwable) {
            val networkException = when (p1) {
                is HttpException -> NetworkException.HttpException(p1.message())
                is java.net.ConnectException -> NetworkException.ConnectException(p1.message ?: "Connection error")
                is java.net.UnknownHostException -> NetworkException.UnknownHostException(p1.message ?: "Unknown host")
                is java.net.SocketTimeoutException -> NetworkException.SocketTimeoutException(p1.message ?: "Socket timeout")
                is javax.net.ssl.SSLHandshakeException -> NetworkException.SSLHandshakeException(p1.message ?: "SSL handshake failed")
                is javax.net.ssl.SSLPeerUnverifiedException -> NetworkException.SSLPeerUnverifiedException(p1.message ?: "SSL peer unverified")
                is java.io.IOException -> NetworkException.IOException(p1.message ?: "I/O error")
                else -> NetworkException.Unknown(p1.message ?: "An unknown error occurred")
            }
            continuation.resume(ApiResponse.Exception(networkException))
        }
    })
}

sealed interface ApiResponse<T> {
    data class Success<T>(val data: T, val headers: Iterable<Pair<String, String>>) : ApiResponse<T>
    data class Error<T>(val error: HttpException) : ApiResponse<T>
    data class Exception<T>(val throwable: Throwable) : ApiResponse<T>
}
