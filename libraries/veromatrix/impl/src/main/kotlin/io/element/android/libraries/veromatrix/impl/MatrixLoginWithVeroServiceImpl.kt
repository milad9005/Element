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

package io.element.android.libraries.veromatrix.impl

import com.squareup.anvil.annotations.ContributesBinding
import io.element.android.libraries.core.extensions.mapFailure
import io.element.android.libraries.di.AppScope
import io.element.android.libraries.matrix.api.core.SessionId
import io.element.android.libraries.matrix.impl.auth.RustMatrixAuthenticationService
import io.element.android.libraries.vero.api.auth.VeroAuthenticationDataSource
import io.element.android.libraries.vero.api.auth.VeroAuthenticationService
import io.element.android.libraries.vero.api.auth.VeroCredential
import io.element.android.libraries.vero.api.contact.VeroContactService
import io.element.android.libraries.veromatrix.api.MatrixLoginWithVeroService
import io.element.android.libraries.veromatrix.api.SyncMatrixProfileWithVero
import javax.inject.Inject

@ContributesBinding(AppScope::class)
class MatrixLoginWithVeroServiceImpl @Inject constructor(
    private val rustMatrixAuthenticationService: RustMatrixAuthenticationService,
    private val veroAuthenticationService: VeroAuthenticationService,
    private val veroAuthenticationDataSource: VeroAuthenticationDataSource,
    private val veroContactService: VeroContactService,
    private val syncMatrixProfileWithVero: SyncMatrixProfileWithVero,
) : MatrixLoginWithVeroService {

    override suspend fun login(username: String, password: String): Result<SessionId> {
        return runCatching {
            val veroCredential = VeroCredential(username, password)
            val veroUser = veroAuthenticationService.login(veroCredential).onFailure { throw it }.getOrThrow()
            val result = rustMatrixAuthenticationService.loginWithToken(veroUser.token).onSuccess {
                veroContactService.deleteAllContact()
                veroAuthenticationDataSource.setCredential(veroCredential)
                syncMatrixProfileWithVero.sync(veroUser.token)
            }.onFailure {
                veroAuthenticationDataSource.setCredential(null)
                rustMatrixAuthenticationService.getLatestSessionId()
                throw it
            }
            result.getOrThrow()
        }.mapFailure { it }
    }
}
