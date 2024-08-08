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
    id("kotlin-parcelize")
    alias(libs.plugins.anvil)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "io.element.android.libraries.matrixnetwork.impl"

    buildFeatures {
        buildConfig = true
    }
}

anvil {
    generateDaggerFactories.set(true)
}

dependencies {
    api(libs.vero.chat.libraries.matrixnetwork.api)
    implementation(libs.vero.chat.appconfig)
    implementation(libs.vero.chat.libraries.di)
    implementation(libs.dagger)
    implementation(libs.vero.chat.libraries.androidutils)
    implementation(libs.vero.chat.libraries.core)
    implementation(libs.vero.chat.services.analytics.api)
    implementation(libs.serialization.json)
    implementation(libs.vero.chat.libraries.network)

    implementation(platform(libs.network.okhttp.bom))
    implementation(libs.network.okhttp)
    implementation(libs.network.okhttp.logging)
    implementation(platform(libs.network.retrofit.bom))
    implementation(libs.network.retrofit)
    implementation(libs.network.retrofit.converter.serialization)
    implementation(libs.serialization.json)

    testImplementation(libs.test.junit)
    testImplementation(libs.test.truth)
    testImplementation(libs.vero.chat.libraries.matrix.test)
}
