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
    namespace = "io.element.android.libraries.worker"
}

dependencies {
    anvil(libs.vero.chat.anvilcodegen)
    implementation(libs.vero.chat.appconfig)
    implementation(libs.dagger)
    implementation(libs.vero.chat.libraries.core)
    implementation(libs.vero.chat.libraries.di)
    api(libs.androidx.work.runtime.ktx)
}
