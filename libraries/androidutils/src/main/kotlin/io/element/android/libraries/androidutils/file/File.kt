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

package io.element.android.libraries.androidutils.file

import android.content.Context
import androidx.annotation.WorkerThread
import timber.log.Timber
import java.io.File
import java.util.UUID

fun File.safeDelete() {
    if (exists().not()) return
    try {

        if (delete().not()) {
            Timber.w("Warning, unable to delete file $path")
        }
    } catch (e: Exception) {
        Timber.e("Error, unable to delete file $path")
        }
}

fun File.safeRenameTo(dest: File) {
    try {
        if (renameTo(dest).not()) {
            Timber.w("Warning, unable to rename file $path to ${dest.path}")
        }
    } catch (e: Exception) {
        Timber.e("Error, unable to rename file $path to ${dest.path}")
    }
}

fun Context.createTmpFile(baseDir: File = cacheDir, extension: String? = null): File {
    val suffix = extension?.let { ".$extension" }
    return File.createTempFile(UUID.randomUUID().toString(), suffix, baseDir).apply { mkdirs() }
}

/* ==========================================================================================
 * Size
 * ========================================================================================== */

@WorkerThread
fun File.getSizeOfFiles(): Long {
    return walkTopDown()
        .onEnter {
            Timber.v("Get size of ${it.absolutePath}")
            true
        }
        .sumOf { it.length() }
}
