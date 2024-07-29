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
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.squareup.anvil.annotations.ContributesMultibinding
import io.element.android.libraries.di.AppScope
import io.element.android.libraries.vero.api.auth.VeroAuthenticationDataSource
import io.element.android.libraries.vero.api.auth.VeroAuthenticationService
import io.element.android.libraries.vero.api.contact.VeroContactService
import io.element.android.libraries.vero.api.profile.VeroProfileService
import io.element.android.libraries.veromatrix.api.SyncMatrixProfileWithVero
import io.element.android.libraries.worker.ChildWorkerFactory
import io.element.android.libraries.worker.WorkerBindingKey
import javax.inject.Inject

@ContributesMultibinding(AppScope::class)
@WorkerBindingKey(VeroSyncWorker::class)
class ChildWorkerFactoryFactory @Inject constructor(
    private val syncMatrixProfileWithVero: SyncMatrixProfileWithVero,
    private val veroContactService: VeroContactService,
    private val veroProfileService: VeroProfileService,
    private val veroAuthenticationService: VeroAuthenticationService,
    private val veroAuthenticationDataSource: VeroAuthenticationDataSource
) : ChildWorkerFactory {
    override fun create(appContext: Context, workerParameters: WorkerParameters): ListenableWorker {
        return VeroSyncWorker(
            appContext = appContext,
            workerParams = workerParameters,
            syncMatrixProfileWithVero = syncMatrixProfileWithVero,
            veroContactService = veroContactService,
            veroProfileService = veroProfileService,
            veroAuthenticationService = veroAuthenticationService,
            veroAuthenticationDataSource = veroAuthenticationDataSource
        )
    }
}
