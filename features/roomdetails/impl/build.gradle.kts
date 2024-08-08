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
    id("kotlin-parcelize")
}

android {
    namespace = "io.element.android.features.roomdetails.impl"
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
    implementation(libs.vero.chat.libraries.designsystem)
    implementation(libs.vero.chat.libraries.ui.strings)
    implementation(libs.vero.chat.libraries.androidutils)
    implementation(libs.vero.chat.libraries.mediapickers.api)
    implementation(libs.vero.chat.libraries.mediaupload.api)
    implementation(libs.vero.chat.libraries.mediaviewer.api)
    implementation(libs.vero.chat.libraries.featureflag.api)
    implementation(libs.vero.chat.libraries.permissions.api)
    implementation(libs.vero.chat.libraries.preferences.api)
    implementation(libs.vero.chat.libraries.testtags)
    api(libs.vero.chat.features.roomdetails.api)
    api(libs.vero.chat.libraries.usersearch.api)
    api(libs.vero.chat.services.apperror.api)
    implementation(libs.coil.compose)
    implementation(libs.vero.chat.features.call.api)
    implementation(libs.vero.chat.features.createroom.api)
    implementation(libs.vero.chat.features.leaveroom.api)
    implementation(libs.vero.chat.features.userprofile.shared)
    implementation(libs.vero.chat.services.analytics.api)
    implementation(libs.vero.chat.features.poll.api)

    testImplementation(libs.test.junit)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.molecule.runtime)
    testImplementation(libs.test.truth)
    testImplementation(libs.test.turbine)
    testImplementation(libs.test.mockk)
    testImplementation(libs.test.robolectric)
    testImplementation(libs.vero.chat.libraries.matrix.test)
    testImplementation(libs.vero.chat.libraries.mediaupload.test)
    testImplementation(libs.vero.chat.libraries.mediapickers.test)
    testImplementation(libs.vero.chat.libraries.permissions.test)
    testImplementation(libs.vero.chat.libraries.usersearch.test)
    testImplementation(libs.vero.chat.libraries.featureflag.test)
    testImplementation(libs.vero.chat.tests.testutils)
    testImplementation(libs.vero.chat.features.leaveroom.test)
    testImplementation(libs.vero.chat.features.createroom.test)
    testImplementation(libs.vero.chat.services.analytics.test)
    testImplementation(libs.androidx.compose.ui.test.junit)
    testReleaseImplementation(libs.androidx.compose.ui.test.manifest)

    ksp(libs.showkase.processor)
}
