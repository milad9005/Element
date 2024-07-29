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

package io.element.android.libraries.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.squareup.anvil.annotations.ContributesBinding
import dagger.MapKey
import dagger.internal.Provider
import io.element.android.libraries.di.AppScope
import javax.inject.Inject
import kotlin.reflect.KClass

@ContributesBinding(AppScope::class)
@JvmSuppressWildcards
class DaggerWorkerFactory @Inject constructor(
    private val workerFactories: Map<Class<out CoroutineWorker>, @JvmSuppressWildcards ChildWorkerFactory>,
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        val foundEntry = workerFactories.entries.find {
            Class.forName(workerClassName).isAssignableFrom(it.key)
        }
        val factoryProvider = foundEntry?.value
        return factoryProvider?.create(appContext, workerParameters)
    }
}


interface ChildWorkerFactory {
    fun create(appContext: Context, workerParameters: WorkerParameters): ListenableWorker
}

@MapKey
annotation class WorkerBindingKey(val value: KClass<out CoroutineWorker>)


