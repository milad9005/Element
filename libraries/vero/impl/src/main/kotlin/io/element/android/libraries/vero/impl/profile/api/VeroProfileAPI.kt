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

package io.element.android.libraries.vero.impl.profile.api

import io.element.android.libraries.vero.impl.contact.api.VeroContacts
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import retrofit2.http.Url

interface VeroProfileAPI {

    @GET("api/profiles/list")
    fun getProfiles(@Header("Authorization") token: String, @Query("ids") vararg ids: String): Call<VeroProfileListResponse>

    @GET
    fun getProfilePicture(@Url url: String): Call<ResponseBody>
}

@Serializable
data class VeroProfileListResponse(@SerialName("items") val veroContacts: VeroContacts)
