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

package io.element.android.libraries.vero.impl.di

import android.content.Context
import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides
import io.element.android.libraries.di.AppScope
import io.element.android.libraries.di.ApplicationContext
import io.element.android.libraries.di.SingleIn
import io.element.android.libraries.vero.impl.VeroDatabase
import io.element.encrypteddb.SqlCipherDriverFactory
import io.element.encrypteddb.passphrase.RandomSecretPassphraseProvider

@Module
@ContributesTo(AppScope::class)
object VeroStorageModule {

    @Provides
    @SingleIn(AppScope::class)
    fun provideMatrixDatabase(
        @ApplicationContext context: Context,
    ): VeroDatabase {
        val name = "vero_database"
        val secretFile = context.getDatabasePath("$name.key")

        // Make sure the parent directory of the key file exists, otherwise it will crash in older Android versions
        val parentDir = secretFile.parentFile
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs()
        }

        val passphraseProvider = RandomSecretPassphraseProvider(context, secretFile)
        val driver = SqlCipherDriverFactory(passphraseProvider).create(VeroDatabase.Schema, "$name.db", context)
        return VeroDatabase(driver)
    }

}
