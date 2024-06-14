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
    implementation(projects.libraries.di)
    implementation(libs.dagger)
    implementation(projects.libraries.androidutils)
    implementation(projects.libraries.core)
    implementation(projects.services.analytics.api)
    implementation(libs.serialization.json)
    api(projects.libraries.sessionStorage.api)
    implementation(libs.coroutines.core)

    testImplementation(libs.test.junit)
    testImplementation(libs.test.truth)
    testImplementation(projects.libraries.matrix.test)
}
