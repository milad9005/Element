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

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import io.element.android.libraries.vero.api.auth.VeroAuthenticationDataSource
import io.element.android.libraries.vero.api.auth.VeroAuthenticationService
import io.element.android.libraries.vero.api.contact.VeroContactService
import io.element.android.libraries.vero.api.profile.VeroProfileService
import io.element.android.libraries.veromatrix.api.SyncMatrixProfileWithVero

class VeroSyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val syncMatrixProfileWithVero: SyncMatrixProfileWithVero,
    private val veroContactService: VeroContactService,
    private val veroProfileService: VeroProfileService,
    private val veroAuthenticationService: VeroAuthenticationService,
    private val veroAuthenticationDataSource: VeroAuthenticationDataSource,
) : CoroutineWorker(appContext, workerParams) {

    private val token = workerParams.inputData.getString("token")
    override suspend fun doWork(): Result {
        try {
            val veroToken = token?.takeIf { it.isNotBlank() } ?: login()
            syncMatrixProfileWithVero.sync(veroToken)
            veroContactService.syncContact(veroToken)
            veroProfileService.fetchNullProfileData(veroToken)
        } catch (e: Exception) {
            Result.failure()
        }
        return Result.success()
    }

    private suspend fun login(): String {
        val credential = veroAuthenticationDataSource.getCredential() ?: throw Exception()
        return veroAuthenticationService.login(credential).getOrThrow().token
    }

    companion object {
        fun run(context: Context, token: String? = null) {
            val request: OneTimeWorkRequest = OneTimeWorkRequestBuilder<VeroSyncWorker>()
                .setInputData(Data.Builder().putString("token", token).build())
                .build()
            WorkManager.getInstance(context).enqueueUniqueWork("request", ExistingWorkPolicy.KEEP, request)
        }
    }
}
