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

package io.element.android.libraries.vero.impl.contact.store

import io.element.android.libraries.matrix.session.Contact
import io.element.android.libraries.vero.impl.VeroDatabase
import io.element.android.libraries.vero.impl.contact.api.VeroContact
import io.element.android.libraries.vero.impl.contact.api.VeroContacts
import javax.inject.Inject
import io.element.android.libraries.matrix.session.VeroProfile as VeroProfileEntity

class VeroContactDataStore @Inject constructor(
    private val database: VeroDatabase,
) {

    fun insertContacts(veroContacts: VeroContacts) {
        database.transaction {
            veroContacts.forEach { veroContact ->
                veroContact.takeIf { !it.id.isNullOrBlank() }
                    ?.toDatabaseModel()
                    ?.let {
                        database.veroDataQueries.insertVeroProfile(it)
                        database.veroDataQueries.insertVeroContact(Contact(it.id))
                    }
            }
        }
    }

    fun getContacts(query: String?): VeroContacts {
        return if (query.isNullOrBlank())
            database.veroDataQueries.selectAllContact().executeAsList().map { it.toVeroContact() }
        else
            database.veroDataQueries.selectContact(username = "%$query%").executeAsList().map { it.toVeroContact() }
    }

    fun deleteAllContact() {
        database.veroDataQueries.deleteAllContact()
    }
}

private fun VeroProfileEntity.toVeroContact(): VeroContact {
    return VeroContact(
        id = id,
        firstname = firstname ?: "",
        lastname = lastname ?: "",
        username = username ?: "",
        picture = picture,
    )
}

private fun VeroContact.toDatabaseModel(): VeroProfileEntity {
    return VeroProfileEntity(
        id = id ?: "",
        firstname = firstname ?: "",
        lastname = lastname ?: "",
        username = username ?: "",
        picture = picture,
    )
}
