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

sealed class NetworkException(message: String) : Exception(message) {
    class HttpException(message: String) : NetworkException("HTTP error: $message")
    class IOException(message: String) : NetworkException("Network I/O error: $message")
    class ConnectException(message: String) : NetworkException("Connection error: $message")
    class UnknownHostException(message: String) : NetworkException("Unknown host: $message")
    class SocketTimeoutException(message: String) : NetworkException("Socket timeout: $message")
    class SSLHandshakeException(message: String) : NetworkException("SSL handshake failed: $message")
    class SSLPeerUnverifiedException(message: String) : NetworkException("SSL peer unverified: $message")
    class MalformedJsonException(message: String) : NetworkException("Malformed JSON: $message")
    class JsonSyntaxException(message: String) : NetworkException("JSON syntax error: $message")
    class ConversionException(message: String) : NetworkException("Conversion error: $message")
    class Unknown(message: String) : NetworkException("An unknown error occurred: $message")
}
