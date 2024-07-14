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

package io.element.android.libraries.vero.impl.contact

import com.squareup.anvil.annotations.ContributesBinding
import io.element.android.libraries.di.AppScope
import io.element.android.libraries.network.util.safeExecute
import io.element.android.libraries.vero.api.contact.VeroContactService
import io.element.android.libraries.vero.api.contact.VeroProfiles
import io.element.android.libraries.vero.impl.contact.api.VeroContactAPI
import io.element.android.libraries.vero.impl.contact.api.VeroContacts
import io.element.android.libraries.vero.impl.contact.store.VeroContactDataStore
import io.element.android.libraries.vero.impl.util.toBearerToken
import javax.inject.Inject

@ContributesBinding(AppScope::class)
class VeroContactServiceImpl @Inject constructor(
    private val veroContactAPI: VeroContactAPI,
    private val veroContactDataStore: VeroContactDataStore
) : VeroContactService {

    override suspend fun syncContact(token: String): VeroContacts {
        return when (val response = veroContactAPI.getContact(token.toBearerToken()).safeExecute()) {
            is io.element.android.libraries.network.util.ApiResponse.Error -> throw response.error
            is io.element.android.libraries.network.util.ApiResponse.Exception -> throw response.throwable
            is io.element.android.libraries.network.util.ApiResponse.Success -> {
                veroContactDataStore.deleteAllContact()
                veroContactDataStore.insertContacts(response.data)
                response.data
            }
        }
    }

    override suspend fun getContact(query: String?): VeroProfiles {
        return veroContactDataStore.getContacts(query)
    }

    override suspend fun deleteAllContact() {
        veroContactDataStore.deleteAllContact()
    }
}
