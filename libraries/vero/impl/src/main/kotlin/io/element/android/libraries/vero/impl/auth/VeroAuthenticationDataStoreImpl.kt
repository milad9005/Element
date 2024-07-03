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

package io.element.android.libraries.vero.impl.auth

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.squareup.anvil.annotations.ContributesBinding
import io.element.android.libraries.di.AppScope
import io.element.android.libraries.vero.api.auth.VeroAuthenticationDataSource
import io.element.android.libraries.vero.api.auth.VeroCredential
import javax.inject.Inject

@ContributesBinding(AppScope::class)
class VeroAuthenticationDataStoreImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences // todo -> Use secure :D
) : VeroAuthenticationDataSource {

    @SuppressLint("ApplySharedPref")
    override suspend fun setCredential(veroCredential: VeroCredential?) {
        sharedPreferences.edit()
            .putString(PREF_KEY_VERO_USERNAME, veroCredential?.username)
            .putString(PREF_KEY_VERO_PASSWORD, veroCredential?.password)
            .commit()
    }

    override suspend fun getCredential(): VeroCredential? {
        val username = sharedPreferences.getString(PREF_KEY_VERO_USERNAME, null)
        val password = sharedPreferences.getString(PREF_KEY_VERO_PASSWORD, null)
        return VeroCredential(username = username ?: return null, password = password ?: return null)
    }

    companion object {
        private const val PREF_KEY_VERO_USERNAME = "vero-username"
        private const val PREF_KEY_VERO_PASSWORD = "vero-password"
    }
}
