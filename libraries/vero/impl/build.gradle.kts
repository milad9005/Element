plugins {
    id("io.element.android-library")
    alias(libs.plugins.anvil)
    alias(libs.plugins.kotlin.serialization)
}

anvil {
    generateDaggerFactories.set(true)
}

android {
    namespace = "io.element.android.libraries.vero.impl"
}

dependencies {
    api(projects.libraries.vero.api)
    anvil(projects.anvilcodegen)
    implementation(projects.appconfig)
    implementation(libs.dagger)
    implementation(projects.libraries.core)
    implementation(projects.libraries.di)
    implementation(platform(libs.network.okhttp.bom))
    implementation(libs.network.okhttp)
    implementation(libs.network.okhttp.logging)
    implementation(platform(libs.network.retrofit.bom))
    implementation(libs.network.retrofit)
    implementation(libs.network.retrofit.converter.serialization)
    implementation(projects.libraries.network)

    implementation("com.madgag.spongycastle:core:1.58.0.0")

}
