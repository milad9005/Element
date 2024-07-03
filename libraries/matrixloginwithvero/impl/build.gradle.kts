plugins {
    id("io.element.android-library")
    alias(libs.plugins.anvil)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.sqldelight)
}

anvil {
    generateDaggerFactories.set(true)
}

android {
    namespace = "io.element.android.libraries.matrixloginwithvero.impl"
}

dependencies {
    anvil(projects.anvilcodegen)
    implementation(projects.appconfig)
    implementation(libs.dagger)
    implementation(projects.libraries.core)
    implementation(projects.libraries.di)
    implementation(projects.libraries.vero.impl)
    implementation(projects.libraries.matrix.impl)
    api(projects.libraries.matrixloginwithvero.api)
}

