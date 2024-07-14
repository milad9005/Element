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

package io.element.android.libraries.vero.impl.profile

import com.squareup.anvil.annotations.ContributesBinding
import io.element.android.libraries.di.AppScope
import io.element.android.libraries.network.util.ApiResponse
import io.element.android.libraries.network.util.safeExecute
import io.element.android.libraries.vero.api.contact.VeroProfile
import io.element.android.libraries.vero.api.contact.VeroProfiles
import io.element.android.libraries.vero.api.profile.VeroProfileService
import io.element.android.libraries.vero.impl.profile.api.VeroProfileAPI
import io.element.android.libraries.vero.impl.profile.store.VeroProfileDataStore
import io.element.android.libraries.vero.impl.util.toBearerToken
import javax.inject.Inject

@ContributesBinding(AppScope::class)
class VeroProfileServiceImpl @Inject constructor(
    private val veroProfileAPI: VeroProfileAPI,
    private val veroProfileDataStore: VeroProfileDataStore
) : VeroProfileService {

    override suspend fun fetchNullProfileData(token: String, vararg ids: String): VeroProfiles {
        val cacheIds = veroProfileDataStore.getNullProfile().map { it.id }.toTypedArray()
        return when (val response = veroProfileAPI.getProfiles(token.toBearerToken(), *cacheIds, *ids).safeExecute()) {
            is ApiResponse.Error -> throw response.error
            is ApiResponse.Exception -> throw response.throwable
            is ApiResponse.Success -> {
                response.data.veroContacts.also { veroProfileDataStore.insertProfiles(it) }
            }
        }
    }

    override suspend fun addNewProfiles(veroProfiles: VeroProfiles) {
        veroProfileDataStore.insertProfiles(veroProfiles)
    }

    override suspend fun getProfileById(id: String): VeroProfile? {
        return veroProfileDataStore.getProfileById(id)
    }
}
