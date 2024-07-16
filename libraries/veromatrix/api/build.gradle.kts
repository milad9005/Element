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
    implementation(projects.libraries.di)
    implementation(libs.dagger)
    implementation(projects.libraries.androidutils)
    implementation(projects.libraries.core)
    implementation(projects.services.analytics.api)
    implementation(libs.serialization.json)
    implementation(libs.coroutines.core)
    api(projects.libraries.sessionStorage.api)
    api(projects.libraries.vero.api)
    api(projects.libraries.matrix.api)

    testImplementation(libs.test.junit)
    testImplementation(libs.test.truth)
    testImplementation(projects.libraries.matrix.test)
}
