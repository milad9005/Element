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

package io.element.android.libraries.vero.api.auth

/**
 *  Vero social authentication service
 */
interface VeroAuthenticationService {

    /**
     * Vero login via username and password
     *
     * @param username vero user email
     * @param password
     *
     * @return return [VeroUser] if the credential is valid
     */
    suspend fun login(username: String, password: String): Result<VeroUser>

}
