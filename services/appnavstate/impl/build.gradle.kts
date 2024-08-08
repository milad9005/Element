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
    id("io.element.android-library")
    alias(libs.plugins.ksp)
    alias(libs.plugins.anvil)
}

anvil {
    generateDaggerFactories.set(true)
}

android {
    namespace = "io.element.android.services.appnavstate.impl"
}

dependencies {
    anvil(libs.vero.chat.anvilcodegen)
    implementation(libs.dagger)
    implementation(libs.vero.chat.libraries.core)
    implementation(libs.vero.chat.libraries.di)
    implementation(libs.vero.chat.libraries.matrix.api)
    implementation(libs.vero.chat.anvilannotations)

    implementation(libs.coroutines.core)
    implementation(libs.androidx.corektx)
    implementation(libs.androidx.lifecycle.process)

    api(libs.vero.chat.services.appnavstate.api)

    testImplementation(libs.test.junit)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.test.truth)
    testImplementation(libs.vero.chat.libraries.matrix.test)
    testImplementation(libs.vero.chat.tests.testutils)
    testImplementation(libs.vero.chat.services.appnavstate.test)
}
