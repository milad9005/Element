/*
 * Copyright (c) 2022 New Vector Ltd
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
    namespace = "io.element.android.features.preferences.impl"
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
    implementation(libs.vero.chat.libraries.androidutils)
    implementation(libs.vero.chat.appconfig)
    implementation(libs.vero.chat.libraries.core)
    implementation(libs.vero.chat.libraries.architecture)
    implementation(libs.vero.chat.libraries.matrix.api)
    implementation(libs.vero.chat.libraries.designsystem)
    implementation(libs.vero.chat.libraries.featureflag.api)
    implementation(libs.vero.chat.libraries.featureflag.ui)
    implementation(libs.vero.chat.libraries.network)
    implementation(libs.vero.chat.libraries.pushstore.api)
    implementation(libs.vero.chat.libraries.indicator.api)
    implementation(libs.vero.chat.libraries.preferences.api)
    implementation(libs.vero.chat.libraries.troubleshoot.api)
    implementation(libs.vero.chat.libraries.testtags)
    implementation(libs.vero.chat.libraries.ui.strings)
    implementation(libs.vero.chat.libraries.matrixui)
    implementation(libs.vero.chat.libraries.mediapickers.api)
    implementation(libs.vero.chat.libraries.mediaupload.api)
    implementation(libs.vero.chat.libraries.permissions.api)
    implementation(libs.vero.chat.libraries.push.api)
    implementation(libs.vero.chat.libraries.pushproviders.api)
    implementation(libs.vero.chat.libraries.fullscreenintent.api)
    implementation(libs.vero.chat.features.rageshake.api)
    implementation(libs.vero.chat.features.lockscreen.api)
    implementation(libs.vero.chat.features.analytics.api)
    implementation(libs.vero.chat.features.ftue.api)
    implementation(libs.vero.chat.features.logout.api)
    implementation(libs.vero.chat.features.roomlist.api)
    implementation(libs.vero.chat.services.analytics.api)
    implementation(libs.vero.chat.services.toolbox.api)
    implementation(libs.datetime)
    implementation(libs.coil.compose)
    implementation(libs.androidx.browser)
    implementation(libs.androidx.datastore.preferences)
    api(libs.vero.chat.features.preferences.api)
    ksp(libs.showkase.processor)

    testImplementation(libs.test.junit)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.molecule.runtime)
    testImplementation(libs.test.truth)
    testImplementation(libs.test.turbine)
    testImplementation(libs.test.mockk)
    testImplementation(libs.test.robolectric)
    testImplementation(libs.vero.chat.libraries.matrix.test)
    testImplementation(libs.vero.chat.libraries.featureflag.test)
    testImplementation(libs.vero.chat.libraries.mediapickers.test)
    testImplementation(libs.vero.chat.libraries.mediaupload.test)
    testImplementation(libs.vero.chat.libraries.permissions.test)
    testImplementation(libs.vero.chat.libraries.preferences.test)
    testImplementation(libs.vero.chat.libraries.push.test)
    testImplementation(libs.vero.chat.libraries.pushstore.test)
    testImplementation(libs.vero.chat.features.ftue.test)
    testImplementation(libs.vero.chat.features.rageshake.test)
    testImplementation(libs.vero.chat.features.rageshake.impl)
    testImplementation(libs.vero.chat.features.roomlist.test)
    testImplementation(libs.vero.chat.libraries.indicator.impl)
    testImplementation(libs.vero.chat.libraries.pushproviders.test)
    testImplementation(libs.vero.chat.libraries.fullscreenintent.test)
    testImplementation(libs.vero.chat.features.logout.impl)
    testImplementation(libs.vero.chat.services.analytics.test)
    testImplementation(libs.vero.chat.services.toolbox.test)
    testImplementation(libs.vero.chat.features.analytics.impl)
    testImplementation(libs.vero.chat.tests.testutils)
    testImplementation(libs.androidx.compose.ui.test.junit)
    testReleaseImplementation(libs.androidx.compose.ui.test.manifest)
}
