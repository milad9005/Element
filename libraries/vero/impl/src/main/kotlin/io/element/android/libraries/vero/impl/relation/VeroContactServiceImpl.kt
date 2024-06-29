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

package io.element.android.libraries.vero.impl.relation

import com.squareup.anvil.annotations.ContributesBinding
import io.element.android.libraries.di.AppScope
import io.element.android.libraries.vero.api.relation.VeroContactService
import io.element.android.libraries.vero.api.relation.VeroContacts
import io.element.android.libraries.vero.impl.relation.api.VeroContactAPI
import io.element.android.libraries.vero.impl.util.toBearerToken
import javax.inject.Inject

@ContributesBinding(AppScope::class)
class VeroContactServiceImpl @Inject constructor(private val veroContactAPI: VeroContactAPI) : VeroContactService {

    override suspend fun getContact(token: String): Result<VeroContacts> =
        kotlin.runCatching {
            veroContactAPI.getContact(token.toBearerToken()).body()!!
        }
}
