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
    id("io.element.android-compose-library")
    alias(libs.plugins.anvil)
}

android {
    namespace = "io.element.android.libraries.eventformatter.impl"

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

anvil {
    generateDaggerFactories.set(true)
}

dependencies {
    anvil(libs.vero.chat.anvilcodegen)
    implementation(libs.vero.chat.anvilannotations)

    implementation(libs.vero.chat.libraries.core)
    implementation(libs.vero.chat.libraries.architecture)
    implementation(libs.vero.chat.libraries.matrix.api)
    implementation(libs.vero.chat.libraries.matrixui)
    implementation(libs.vero.chat.libraries.ui.strings)
    implementation(libs.vero.chat.services.toolbox.api)
    api(libs.vero.chat.libraries.eventformatter.api)

    testImplementation(libs.vero.chat.services.toolbox.impl)
    testImplementation(libs.test.junit)
    testImplementation(libs.test.robolectric)
    testImplementation(libs.test.truth)
    testImplementation(libs.vero.chat.libraries.matrix.test)
}
