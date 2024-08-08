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

@file:Suppress("UnstableApiUsage")

import extension.allFeaturesApi

plugins {
    id("io.element.android-compose-library")
    alias(libs.plugins.anvil)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kapt)
    id("kotlin-parcelize")
}

android {
    namespace = "io.element.android.appnav"
}

dependencies {
    implementation(libs.vero.chat.anvilannotations)
    anvil(libs.vero.chat.anvilcodegen)
    implementation(libs.dagger)
    kapt(libs.dagger.compiler)

    allFeaturesApi(libs)

    implementation(libs.vero.chat.libraries.core)
    implementation(libs.vero.chat.libraries.androidutils)
    implementation(libs.vero.chat.libraries.architecture)
    implementation(libs.vero.chat.libraries.deeplink)
    implementation(libs.vero.chat.libraries.matrix.api)
    implementation(libs.vero.chat.libraries.push.api)
    implementation(libs.vero.chat.libraries.pushproviders.api)
    implementation(libs.vero.chat.libraries.designsystem)
    implementation(libs.vero.chat.libraries.matrixui)
    implementation(libs.vero.chat.libraries.ui.strings)

    implementation(libs.coil)

    implementation(libs.vero.chat.features.ftue.api)
    implementation(libs.vero.chat.features.share.api)
    implementation(libs.vero.chat.features.viewfolder.api)

    implementation(libs.vero.chat.services.apperror.impl)
    implementation(libs.vero.chat.services.appnavstate.api)
    implementation(libs.vero.chat.services.analytics.api)

    testImplementation(libs.test.junit)
    testImplementation(libs.test.robolectric)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.molecule.runtime)
    testImplementation(libs.test.truth)
    testImplementation(libs.test.turbine)
    testImplementation(libs.vero.chat.libraries.matrix.test)
    testImplementation(libs.vero.chat.libraries.push.test)
    testImplementation(libs.vero.chat.libraries.pushproviders.test)
    testImplementation(libs.vero.chat.features.networkmonitor.test)
    testImplementation(libs.vero.chat.features.login.impl)
    testImplementation(libs.vero.chat.tests.testutils)
    testImplementation(libs.vero.chat.features.rageshake.test)
    testImplementation(libs.vero.chat.features.rageshake.impl)
    testImplementation(libs.vero.chat.features.share.test)
    testImplementation(libs.vero.chat.services.appnavstate.test)
    testImplementation(libs.vero.chat.services.analytics.test)
    testImplementation(libs.test.appyx.junit)
    testImplementation(libs.test.arch.core)

    ksp(libs.showkase.processor)
}
