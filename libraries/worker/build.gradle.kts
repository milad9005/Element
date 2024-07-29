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
    anvil(projects.anvilcodegen)
    implementation(projects.appconfig)
    implementation(libs.dagger)
    implementation(projects.libraries.core)
    implementation(projects.libraries.di)
    api(libs.androidx.work.runtime.ktx)
}
