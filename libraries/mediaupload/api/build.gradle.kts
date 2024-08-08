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

plugins {
    id("io.element.android-library")
    alias(libs.plugins.anvil)
    alias(libs.plugins.ksp)
}

android {
    namespace = "io.element.android.libraries.mediaupload.api"

    anvil {
        generateDaggerFactories.set(true)
    }

    dependencies {
        implementation(libs.vero.chat.anvilannotations)
        anvil(libs.vero.chat.anvilcodegen)

        implementation(libs.vero.chat.libraries.architecture)
        implementation(libs.vero.chat.libraries.androidutils)
        implementation(libs.vero.chat.libraries.core)
        implementation(libs.vero.chat.libraries.di)
        api(libs.vero.chat.libraries.matrix.api)
        implementation(libs.inject)
        implementation(libs.coroutines.core)

        testImplementation(libs.vero.chat.libraries.matrix.test)
        testImplementation(libs.vero.chat.libraries.mediaupload.test)
        testImplementation(libs.test.junit)
        testImplementation(libs.test.truth)
        testImplementation(libs.coroutines.test)
        testImplementation(libs.test.robolectric)
    }
}
