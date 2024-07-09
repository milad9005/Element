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

package io.element.android.libraries.vero.impl.util

import VeroApiErrorResponse
import VeroApiException

internal fun String.toBearerToken(): String {
    return if (startsWith("Bearer ")) this else "Bearer $this"
}

internal fun String?.toVeroSession(): String {
    return "vero-session=${this};"
}

internal fun String.parseVeroSession(): String? {
    return split(";").find { it.contains("vero-session") }?.split("=")?.get(1)
}

internal fun VeroApiErrorResponse.toVeroException(): Throwable {
    return when (status) {
        400 -> VeroApiException.BadRequest(error.code, error.description)
        403 -> VeroApiException.Forbidden(error.code, error.description)
        404 -> VeroApiException.NotFound(error.code, error.description)
        429 -> VeroApiException.TooManyRequests(error.code, error.description)
        500 -> VeroApiException.InternalServerError(error.code, error.description)
        503 -> VeroApiException.ServiceUnavailable(error.code, error.description)
        504 -> VeroApiException.GatewayTimeout(error.code, error.description)
        else -> VeroApiException.Unknown(error.code, error.description)
    }
}

internal fun Iterable<Pair<String, String>>.cookies(): String? {
    return find { it.first == "set-cookie" }?.second
}
