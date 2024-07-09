import kotlinx.serialization.Serializable

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


@Serializable
data class VeroApiErrorResponse(val status: Int, val error: Error)

@Serializable
data class Error(val uri: String, val code: Int, val description: String)

sealed class VeroApiException(val code: Int, val description: String) : Exception("$description - $code") {
    class BadRequest(code: Int, description: String) : VeroApiException(code, description)
    class Forbidden(code: Int, description: String) : VeroApiException(code, description)
    class NotFound(code: Int, description: String) : VeroApiException(code, description)
    class TooManyRequests(code: Int, description: String) : VeroApiException(code, description)
    class InternalServerError(code: Int, description: String) : VeroApiException(code, description)
    class ServiceUnavailable(code: Int, description: String) : VeroApiException(code, description)
    class GatewayTimeout(code: Int, description: String) : VeroApiException(code, description)
    class Unknown(code: Int, description: String) : VeroApiException(code, description)
}
