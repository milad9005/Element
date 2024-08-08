plugins {
    id("io.element.android-compose-library")
    id("kotlin-parcelize")
    alias(libs.plugins.anvil)
}

android {
    namespace = "io.element.android.libraries.veromatrix.api"
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
    implementation(libs.coroutines.core)
    api(libs.vero.chat.libraries.session.storage.api)
    api(libs.vero.chat.libraries.vero.api)
    api(libs.vero.chat.libraries.matrix.api)

    testImplementation(libs.test.junit)
    testImplementation(libs.test.truth)
    testImplementation(libs.vero.chat.libraries.matrix.test)
}
