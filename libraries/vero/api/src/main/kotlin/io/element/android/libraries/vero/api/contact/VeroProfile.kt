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

package io.element.android.libraries.vero.api.contact

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

typealias VeroProfiles = Collection<VeroProfile>

@Serializable data class VeroProfile(
    @SerialName("id") val id: String?,
    @SerialName("firstname") val firstname: String? = null,
    @SerialName("lastname") val lastname: String? = null,
    @SerialName("username") val username: String? = null,
    //@SerialName("loop")  val loop: String,
    @SerialName("picture") val picture: String? = null
)

fun VeroProfile.getDisplayName(): String? {
    return username ?: "$firstname $lastname".takeIf { it.isNotBlank() }
}
