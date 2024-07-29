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
import io.element.android.libraries.di.AppScope
import io.element.android.libraries.matrix.api.core.UserId
import io.element.android.libraries.matrix.api.room.MatrixRoomInfo
import io.element.android.libraries.matrix.api.room.RoomMember
import io.element.android.libraries.vero.api.contact.VeroProfile
import io.element.android.libraries.vero.api.profile.VeroProfileService
import io.element.android.libraries.veromatrix.api.VeroMapper
import javax.inject.Inject

@ContributesBinding(AppScope::class)
class VeroMapperImpl @Inject constructor(
    private val veroProfileService: VeroProfileService,
) : VeroMapper {

    override suspend fun mapRoomMember(roomMember: RoomMember): RoomMember {
        val veroId = roomMember.userId.extractVeroId()
        val veroProfile = veroProfileService.getProfileById(veroId ?: return roomMember)
        if (veroProfile == null && veroId.isUUID()) {
            veroProfileService.addNewProfiles(listOf(VeroProfile(id = veroId)))
        }
        return roomMember.copy(
            avatarUrl = veroProfile?.picture ?: roomMember.avatarUrl,
            displayName = veroProfile?.username ?: roomMember.displayName
        )
    }

    override suspend fun mapMatrixRoomInfo(matrixRoomInfo: MatrixRoomInfo): MatrixRoomInfo {
        val possibleVeroId = matrixRoomInfo.name
        if (possibleVeroId == null || !matrixRoomInfo.isDirect)
            return matrixRoomInfo
        veroProfileService.getProfileById(possibleVeroId)?.let {
            return matrixRoomInfo.copy(
                name = it.username,
                rawName = it.username,
                avatarUrl = it.picture,
            )
        }
        return matrixRoomInfo
    }

    private fun UserId.extractVeroId(): String? {
        return value.replace("@", "").split(":").getOrNull(0)
    }

    private fun String?.isUUID(): Boolean {
        return Regex(UUID_REGEX).matches(this ?: return false)
    }

    private fun String.extractVeroId(): String? {
        return replace("@", "").split(":").getOrNull(0)
    }

    companion object {
        private const val UUID_REGEX = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}\$"
    }
}
