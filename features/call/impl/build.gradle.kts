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

plugins {
    id("io.element.android-compose-library")
    alias(libs.plugins.anvil)
    alias(libs.plugins.ksp)
    id("kotlin-parcelize")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "io.element.android.features.call.impl"

    buildFeatures {
        buildConfig = true
    }

    testOptions {
        unitTests.isIncludeAndroidResources = true
    }
}

anvil {
    generateDaggerFactories.set(true)
}

dependencies {
    implementation(libs.vero.chat.appconfig)
    implementation(libs.vero.chat.anvilannotations)
    implementation(libs.vero.chat.libraries.architecture)
    implementation(libs.vero.chat.libraries.core)
    implementation(libs.vero.chat.libraries.designsystem)
    implementation(libs.vero.chat.libraries.matrix.impl)
    implementation(libs.vero.chat.libraries.matrixui)
    implementation(libs.vero.chat.libraries.network)
    implementation(libs.vero.chat.libraries.preferences.api)
    implementation(libs.vero.chat.libraries.push.api)
    implementation(libs.vero.chat.libraries.ui.strings)
    implementation(libs.vero.chat.services.analytics.api)
    implementation(libs.vero.chat.services.toolbox.api)
    implementation(libs.androidx.webkit)
    implementation(libs.coil.compose)
    implementation(libs.network.retrofit)
    implementation(libs.serialization.json)
    api(libs.vero.chat.features.call.api)
    ksp(libs.showkase.processor)

    testImplementation(libs.coroutines.test)
    testImplementation(libs.molecule.runtime)
    testImplementation(libs.test.truth)
    testImplementation(libs.test.turbine)
    testImplementation(libs.test.robolectric)
    testImplementation(libs.test.mockk)
    testImplementation(libs.vero.chat.features.call.test)
    testImplementation(libs.vero.chat.libraries.featureflag.test)
    testImplementation(libs.vero.chat.libraries.preferences.test)
    testImplementation(libs.vero.chat.libraries.matrix.test)
    testImplementation(libs.vero.chat.libraries.push.test)
    testImplementation(libs.vero.chat.services.analytics.test)
    testImplementation(libs.vero.chat.tests.testutils)
}
