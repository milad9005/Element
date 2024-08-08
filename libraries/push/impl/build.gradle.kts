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
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "io.element.android.libraries.push.impl"

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
    implementation(libs.dagger)
    implementation(libs.androidx.corektx)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.security.crypto)
    val retrofit_bom= platform(libs.network.retrofit.bom)
    implementation(retrofit_bom)
    implementation(libs.network.retrofit)
    implementation(libs.serialization.json)
    implementation(libs.coil)

    implementation(libs.vero.chat.appconfig)
    implementation(libs.vero.chat.libraries.architecture)
    implementation(libs.vero.chat.libraries.core)
    implementation(libs.vero.chat.libraries.designsystem)
    implementation(libs.vero.chat.libraries.di)
    implementation(libs.vero.chat.libraries.androidutils)
    implementation(libs.vero.chat.libraries.network)
    implementation(libs.vero.chat.libraries.matrix.api)
    implementation(libs.vero.chat.libraries.matrixui)
    implementation(libs.vero.chat.libraries.preferences.api)
    implementation(libs.vero.chat.libraries.ui.strings)
    implementation(libs.vero.chat.libraries.troubleshoot.api)
    implementation(libs.vero.chat.features.call.api)
    api(libs.vero.chat.libraries.pushproviders.api)
    api(libs.vero.chat.libraries.pushstore.api)
    api(libs.vero.chat.libraries.push.api)

    implementation(libs.vero.chat.services.analytics.api)
    implementation(libs.vero.chat.services.appnavstate.api)
    implementation(libs.vero.chat.services.toolbox.api)

    testImplementation(libs.test.junit)
    testImplementation(libs.test.mockk)
    testImplementation(libs.test.robolectric)
    testImplementation(libs.test.truth)
    testImplementation(libs.test.turbine)
    testImplementation(libs.coil.test)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.vero.chat.libraries.matrix.test)
    testImplementation(libs.vero.chat.libraries.preferences.test)
    testImplementation(libs.vero.chat.libraries.push.test)
    testImplementation(libs.vero.chat.libraries.pushproviders.test)
    testImplementation(libs.vero.chat.libraries.pushstore.test)
    testImplementation(libs.vero.chat.tests.testutils)
    testImplementation(libs.vero.chat.features.call.test)
    testImplementation(libs.vero.chat.services.appnavstate.test)
    testImplementation(libs.vero.chat.services.toolbox.impl)
    testImplementation(libs.vero.chat.services.toolbox.test)
}
