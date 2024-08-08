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
}

android {
    namespace = "io.element.android.features.migration.impl"
}

dependencies {
    implementation(libs.vero.chat.features.migration.api)
    implementation(libs.vero.chat.libraries.architecture)
    implementation(libs.vero.chat.libraries.preferences.impl)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.vero.chat.features.rageshake.api)
    implementation(libs.vero.chat.libraries.designsystem)
    implementation(libs.vero.chat.libraries.matrix.api)
    implementation(libs.vero.chat.libraries.session.storage.api)
    implementation(libs.vero.chat.libraries.ui.strings)

    ksp(libs.showkase.processor)

    testImplementation(libs.test.junit)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.molecule.runtime)
    testImplementation(libs.test.robolectric)
    testImplementation(libs.test.truth)
    testImplementation(libs.test.turbine)
    testImplementation(libs.vero.chat.libraries.session.storage.impl.memory)
    testImplementation(libs.vero.chat.libraries.session.storage.test)
    testImplementation(libs.vero.chat.libraries.preferences.test)
    testImplementation(libs.vero.chat.tests.testutils)
    testImplementation(libs.vero.chat.features.rageshake.test)
}
