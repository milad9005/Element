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
import io.element.android.libraries.core.mimetype.MimeTypes
import io.element.android.libraries.di.AppScope
import io.element.android.libraries.matrix.api.MatrixClientProvider
import io.element.android.libraries.matrix.api.auth.MatrixAuthenticationService
import io.element.android.libraries.matrix.api.core.UserId
import io.element.android.libraries.vero.api.contact.VeroProfile
import io.element.android.libraries.vero.api.contact.getDisplayName
import io.element.android.libraries.vero.api.profile.VeroProfileService
import io.element.android.libraries.veromatrix.api.SyncMatrixProfileWithVero
import javax.inject.Inject

@ContributesBinding(AppScope::class)
class SyncMatrixProfileWithVeroImpl @Inject constructor(
    private val authenticationService: MatrixAuthenticationService,
    private val matrixClientProvider: MatrixClientProvider,
    private val veroProfileService: VeroProfileService
) : SyncMatrixProfileWithVero {

    override suspend fun sync(token: String): Result<VeroProfile> {
        return kotlin.runCatching {
            val currentSession = authenticationService.getLatestSessionId()
            if (currentSession != null) {
                val client = matrixClientProvider.getOrRestore(currentSession).getOrThrow()
                val veroId = client.sessionId.extractVeroId()
                veroProfileService.fetchProfilesById(token, veroId).first().also { profile ->
                    profile.getDisplayName()?.let { client.setDisplayName(it) }
                    profile.picture?.let { pictureUrl ->
                        //client.mediaLoader.downloadExternalMediaFile(MediaSource(url = pictureUrl), null)
                        val byteArrayPicture = veroProfileService.fetchProfilePicture(pictureUrl)
                        client.uploadAvatar(MimeTypes.Jpeg, byteArrayPicture)

                    }
                }
            } else {
                error("No session")
            }
        }.mapFailure { it }
    }
}

private fun UserId.extractVeroId(): String {
    return value.replace("@", "").split(":")[0]
}
