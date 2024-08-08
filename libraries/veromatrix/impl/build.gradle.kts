plugins {
    id("io.element.android-library")
    alias(libs.plugins.anvil)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.sqldelight)
    alias(libs.plugins.kotlin.android)
}

anvil {
    generateDaggerFactories.set(true)
}

android {
    namespace = "io.element.android.libraries.veromatrix.impl"
}

dependencies {
    anvil(libs.vero.chat.anvilcodegen)
    implementation(libs.vero.chat.appconfig)
    implementation(libs.dagger)
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.vero.chat.libraries.core)
    implementation(libs.vero.chat.libraries.di)
    implementation(libs.vero.chat.libraries.vero.impl)
    implementation(libs.vero.chat.libraries.matrix.impl)
    implementation(libs.vero.chat.libraries.worker)
    api(libs.vero.chat.libraries.veromatrix.api)
}
