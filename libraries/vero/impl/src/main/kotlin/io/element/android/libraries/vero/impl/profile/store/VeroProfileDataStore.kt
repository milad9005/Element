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

package io.element.android.libraries.vero.impl.profile.store

import io.element.android.libraries.vero.api.contact.VeroProfile
import io.element.android.libraries.vero.api.contact.VeroProfiles
import io.element.android.libraries.vero.impl.VeroDatabase
import javax.inject.Inject
import io.element.android.libraries.matrix.session.VeroProfile as VeroProfileEntity

class VeroProfileDataStore @Inject constructor(
    private val database: VeroDatabase,
) {
    fun insertProfiles(veroProfiles: VeroProfiles) {
        database.transaction {
            veroProfiles.forEach { veroContact ->
                veroContact.takeIf { !it.id.isNullOrBlank() }
                    ?.toDatabaseModel()
                    ?.let { database.veroDataQueries.insertVeroProfile(it) }
            }
        }
    }

    fun insertProfile(veroProfile: VeroProfile) {
        veroProfile.takeIf { !it.id.isNullOrBlank() }
            ?.toDatabaseModel()
            ?.let { database.veroDataQueries.insertVeroProfile(it) }
    }

    fun getProfileById(id: String): VeroProfile? {
        return database.veroDataQueries.selectProfileById(id = id).executeAsOneOrNull()?.toVeroProfile()
    }

    fun getNullProfile(): List<VeroProfileEntity> {
        return database.veroDataQueries.selectNullProfile().executeAsList()
    }
}

private fun VeroProfileEntity.toVeroProfile(): VeroProfile {
    return VeroProfile(
        id = id,
        firstname = firstname ?: "",
        lastname = lastname ?: "",
        username = username ?: "",
        picture = picture,
    )
}

private fun VeroProfile.toDatabaseModel(): VeroProfileEntity {
    return VeroProfileEntity(
        id = id ?: "",
        firstname = firstname ?: "",
        lastname = lastname ?: "",
        username = username ?: "",
        picture = picture,
    )
}
