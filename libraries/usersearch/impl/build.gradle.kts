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
}

android {
    namespace = "io.element.android.libraries.usersearch.impl"
}

anvil {
    generateDaggerFactories.set(true)
}

dependencies {
    implementation(libs.vero.chat.libraries.core)
    implementation(libs.vero.chat.libraries.architecture)
    implementation(libs.vero.chat.libraries.di)
    implementation(libs.vero.chat.libraries.matrixui)
    implementation(libs.vero.chat.libraries.matrix.api)
    api(libs.vero.chat.libraries.usersearch.api)
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.vero.chat.libraries.vero.api)
    implementation(libs.vero.chat.libraries.veromatrix.api)

    testImplementation(libs.test.junit)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.molecule.runtime)
    testImplementation(libs.test.truth)
    testImplementation(libs.test.turbine)
    testImplementation(libs.vero.chat.libraries.matrix.test)
    testImplementation(libs.vero.chat.libraries.usersearch.test)
}
