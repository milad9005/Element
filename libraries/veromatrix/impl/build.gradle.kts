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
    anvil(projects.anvilcodegen)
    implementation(projects.appconfig)
    implementation(libs.dagger)
    implementation(libs.kotlinx.collections.immutable)
    implementation(projects.libraries.core)
    implementation(projects.libraries.di)
    implementation(projects.libraries.vero.impl)
    implementation(projects.libraries.matrix.impl)
    implementation(projects.libraries.worker)
    api(projects.libraries.veromatrix.api)
}
