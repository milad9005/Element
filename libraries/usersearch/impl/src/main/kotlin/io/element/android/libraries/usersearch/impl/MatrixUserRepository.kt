/*
 * Copyright (c) 2023 New Vector Ltd
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

package io.element.android.libraries.usersearch.impl

import com.squareup.anvil.annotations.ContributesBinding
import io.element.android.libraries.di.SessionScope
import io.element.android.libraries.matrix.api.MatrixClient
import io.element.android.libraries.matrix.api.core.UserId
import io.element.android.libraries.matrix.api.user.MatrixUser
import io.element.android.libraries.usersearch.api.UserListDataSource
import io.element.android.libraries.usersearch.api.UserRepository
import io.element.android.libraries.usersearch.api.UserSearchResult
import io.element.android.libraries.usersearch.api.UserSearchResultState
import io.element.android.libraries.vero.api.auth.VeroAuthenticationDataSource
import io.element.android.libraries.vero.api.auth.VeroAuthenticationService
import io.element.android.libraries.vero.api.contact.VeroContact
import io.element.android.libraries.vero.api.contact.VeroContactService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ContributesBinding(SessionScope::class)
class MatrixUserRepository @Inject constructor(
    private val client: MatrixClient,
    private val dataSource: UserListDataSource,
    private val veroContactService: VeroContactService,
    private val veroAuthenticationService: VeroAuthenticationService,
    private val veroAuthenticationDataSource: VeroAuthenticationDataSource
) : UserRepository {

    override fun search(query: String): Flow<UserSearchResultState> = flow {
        val users = veroContactService.getContact(query).map { it.toUserSearchResult() }
        emit(UserSearchResultState(isSearching = false, results = users))
    }

    override fun sync(): Flow<UserSearchResultState> = flow { // todo -> temp fix !!!
        kotlin.runCatching {
            var list = veroContactService.getContact(null).map { it.toUserSearchResult() }
            emit(UserSearchResultState(isSearching = true, results = list))
            //val token = veroAuthenticationDataSource.getCredential()?.let { veroAuthenticationService.login(it).token }
           // token?.let { veroContactService.syncContact(it) }
            list = veroContactService.getContact(null).map { it.toUserSearchResult() }
            emit(UserSearchResultState(isSearching = false, results = list))
        }
    }

    private suspend fun fetchSearchResults(query: String, shouldQueryProfile: Boolean): UserSearchResultState {
        // Debounce
        delay(DEBOUNCE_TIME_MILLIS)
        val results = dataSource
            .search(query, MAXIMUM_SEARCH_RESULTS)
            .filter { !client.isMe(it.userId) }
            .map { UserSearchResult(it) }
            .toMutableList()

        // If the query is another user's MXID and the result doesn't contain that user ID, query the profile information explicitly
        if (shouldQueryProfile && results.none { it.matrixUser.userId.value == query }) {
            results.add(
                0,
                dataSource.getProfile(UserId(query))
                    ?.let { UserSearchResult(it) }
                    ?: UserSearchResult(MatrixUser(UserId(query)), isUnresolved = true)
            )
        }

        return UserSearchResultState(results = results, isSearching = false)
    }

    companion object {
        private const val DEBOUNCE_TIME_MILLIS = 250L
        private const val MINIMUM_SEARCH_LENGTH = 0
        private const val MAXIMUM_SEARCH_RESULTS = 10L
    }
}

private fun VeroContact.toUserSearchResult(): UserSearchResult {
    return UserSearchResult(
        MatrixUser(
            userId = UserId("@$id:matrix.metapolitan.io"),
            displayName = username ?: "$firstname $lastname",
            avatarUrl = picture
        )
    )
}
