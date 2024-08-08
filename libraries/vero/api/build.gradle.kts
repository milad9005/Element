plugins {
    id("io.element.android-compose-library")
    id("kotlin-parcelize")
    alias(libs.plugins.anvil)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "io.element.android.libraries.vero.api"
}

anvil {
    generateDaggerFactories.set(true)
}

dependencies {
    implementation(libs.vero.chat.libraries.di)
    implementation(libs.dagger)
    implementation(libs.vero.chat.libraries.androidutils)
    implementation(libs.vero.chat.libraries.core)
    implementation(libs.vero.chat.services.analytics.api)
    implementation(libs.serialization.json)
    api(libs.vero.chat.libraries.session.storage.api)
    implementation(libs.coroutines.core)

    testImplementation(libs.test.junit)
    testImplementation(libs.test.truth)
    testImplementation(libs.vero.chat.libraries.matrix.test)
}
