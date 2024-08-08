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
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "io.element.android.libraries.troubleshoot.impl"

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
    implementation(libs.vero.chat.anvilannotations)
    anvil(libs.vero.chat.anvilcodegen)
    implementation(libs.dagger)
    implementation(libs.vero.chat.libraries.architecture)
    implementation(libs.vero.chat.libraries.designsystem)
    implementation(libs.vero.chat.libraries.di)
    api(libs.vero.chat.libraries.troubleshoot.api)
    api(libs.vero.chat.libraries.push.api)
    implementation(libs.vero.chat.services.analytics.api)
    ksp(libs.showkase.processor)

    testImplementation(libs.test.junit)
    testImplementation(libs.test.robolectric)
    testImplementation(libs.molecule.runtime)
    testImplementation(libs.test.truth)
    testImplementation(libs.test.turbine)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.vero.chat.services.analytics.test)
    testImplementation(libs.vero.chat.tests.testutils)
    testImplementation(libs.vero.chat.libraries.push.test)
    testImplementation(libs.androidx.compose.ui.test.junit)
    testReleaseImplementation(libs.androidx.compose.ui.test.manifest)
}
