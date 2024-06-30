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

package io.element.android.libraries.matrix.network.api.auth

import com.squareup.anvil.annotations.ContributesBinding
import io.element.android.appconfig.AuthenticationConfig
import io.element.android.libraries.di.AppScope
import javax.inject.Inject

@ContributesBinding(AppScope::class)
class MatrixLoginWithTokenServiceImpl @Inject constructor(
    factory: MatrixLoginWithTokenApiFactory
) : MatrixLoginWithTokenService {

    private val api = factory.create(AuthenticationConfig.VERO_CHAT_URL)

    override suspend fun login(token: String): Result<MatrixUserLoginWithToken> {
        return runCatching {
            api.login(MatrixLoginTokenRequest(token = token, type = "org.matrix.login.jwt"))
        }
    }
}
