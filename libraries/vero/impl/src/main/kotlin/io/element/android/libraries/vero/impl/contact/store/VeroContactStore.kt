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

import io.element.android.libraries.matrix.session.VeroContact
import io.element.android.libraries.vero.api.contact.VeroContacts
import io.element.android.libraries.vero.impl.VeroDatabase
import javax.inject.Inject

class VeroContactStore @Inject constructor(
    private val database: VeroDatabase,
) {

    suspend fun insertContacts(veroContacts: VeroContacts) {
        database.transaction {
            veroContacts.forEach { veroContact ->
                veroContact.takeIf { !it.id.isNullOrBlank() }
                    ?.toDatabaseModel()
                    ?.let { database.veroDataQueries.insertVeroContact(it) }
            }
        }
    }

    suspend fun getContacts(query: String?): VeroContacts {
        val veroContacts = if (query.isNullOrBlank())
            database.veroDataQueries.selectAll().executeAsList()
        else
            database.veroDataQueries.select(username = "%$query%").executeAsList()
        return veroContacts.map { it.toVeroContact() }
    }

    suspend fun deleteAllContact() {
        database.veroDataQueries.deleteAll()
    }
}

private fun VeroContact.toVeroContact(): io.element.android.libraries.vero.api.contact.VeroContact {
    return object : io.element.android.libraries.vero.api.contact.VeroContact {
        override val id: String
            get() = this@toVeroContact.id
        override val firstname: String
            get() = this@toVeroContact.firstname
        override val lastname: String
            get() = this@toVeroContact.lastname
        override val username: String
            get() = this@toVeroContact.username
        override val picture: String?
            get() = this@toVeroContact.picture
    }
}

private fun io.element.android.libraries.vero.api.contact.VeroContact.toDatabaseModel(): VeroContact {
    return VeroContact(
        id = id ?: "",
        firstname = firstname ?: "",
        lastname = lastname ?: "",
        username = username ?: "",
        picture = picture,
    )
}
