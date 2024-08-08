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
    namespace = "io.element.android.features.messages.impl"
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
    api(libs.vero.chat.features.messages.api)
    implementation(libs.vero.chat.appconfig)
    implementation(libs.vero.chat.features.call.api)
    implementation(libs.vero.chat.features.location.api)
    implementation(libs.vero.chat.features.poll.api)
    implementation(libs.vero.chat.libraries.androidutils)
    implementation(libs.vero.chat.libraries.core)
    implementation(libs.vero.chat.libraries.architecture)
    implementation(libs.vero.chat.libraries.matrix.api)
    implementation(libs.vero.chat.libraries.matrixui)
    implementation(libs.vero.chat.libraries.designsystem)
    implementation(libs.vero.chat.libraries.textcomposer.impl)
    implementation(libs.vero.chat.libraries.ui.strings)
    implementation(libs.vero.chat.libraries.dateformatter.api)
    implementation(libs.vero.chat.libraries.eventformatter.api)
    implementation(libs.vero.chat.libraries.mediapickers.api)
    implementation(libs.vero.chat.libraries.mediaviewer.api)
    implementation(libs.vero.chat.libraries.featureflag.api)
    implementation(libs.vero.chat.libraries.mediaupload.api)
    implementation(libs.vero.chat.libraries.permissions.api)
    implementation(libs.vero.chat.libraries.preferences.api)
    implementation(libs.vero.chat.libraries.roomselect.api)
    implementation(libs.vero.chat.libraries.voicerecorder.api)
    implementation(libs.vero.chat.libraries.mediaplayer.api)
    implementation(libs.vero.chat.libraries.ui.utils)
    implementation(libs.vero.chat.libraries.testtags)
    implementation(libs.vero.chat.features.networkmonitor.api)
    implementation(libs.vero.chat.services.analytics.api)
    implementation(libs.vero.chat.services.toolbox.api)
    implementation(libs.coil.compose)
    implementation(libs.datetime)
    implementation(libs.jsoup)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.constraintlayout.compose)
    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.ui)
    implementation(libs.vanniktech.blurhash)
    implementation(libs.telephoto.zoomableimage)
    implementation(libs.matrix.emojibase.bindings)

    testImplementation(libs.test.junit)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.molecule.runtime)
    testImplementation(libs.test.truth)
    testImplementation(libs.test.turbine)
    testImplementation(libs.vero.chat.libraries.matrix.test)
    testImplementation(libs.vero.chat.libraries.dateformatter.test)
    testImplementation(libs.vero.chat.features.networkmonitor.test)
    testImplementation(libs.vero.chat.features.messages.test)
    testImplementation(libs.vero.chat.services.analytics.test)
    testImplementation(libs.vero.chat.services.toolbox.test)
    testImplementation(libs.vero.chat.tests.testutils)
    testImplementation(libs.vero.chat.libraries.featureflag.test)
    testImplementation(libs.vero.chat.libraries.mediaupload.test)
    testImplementation(libs.vero.chat.libraries.mediapickers.test)
    testImplementation(libs.vero.chat.libraries.permissions.test)
    testImplementation(libs.vero.chat.libraries.preferences.test)
    testImplementation(libs.vero.chat.libraries.voicerecorder.test)
    testImplementation(libs.vero.chat.libraries.mediaplayer.test)
    testImplementation(libs.vero.chat.libraries.mediaviewer.test)
    testImplementation(libs.vero.chat.libraries.testtags)
    testImplementation(libs.test.mockk)
    testImplementation(libs.test.robolectric)
    testImplementation(libs.vero.chat.features.poll.test)
    testImplementation(libs.vero.chat.features.poll.impl)
    testImplementation(libs.androidx.compose.ui.test.junit)
    testReleaseImplementation(libs.androidx.compose.ui.test.manifest)

    ksp(libs.showkase.processor)
}
