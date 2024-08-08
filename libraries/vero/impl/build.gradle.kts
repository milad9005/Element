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
    namespace = "io.element.android.libraries.vero.impl"
}

dependencies {
    api(libs.vero.chat.libraries.vero.api)
    anvil(libs.vero.chat.anvilcodegen)
    implementation(libs.vero.chat.appconfig)
    implementation(libs.dagger)
    implementation(libs.vero.chat.libraries.core)
    implementation(libs.vero.chat.libraries.di)
    implementation(platform(libs.network.okhttp.bom))
    implementation(libs.network.okhttp)
    implementation(libs.network.okhttp.logging)
    implementation(platform(libs.network.retrofit.bom))
    implementation(libs.network.retrofit)
    implementation(libs.network.retrofit.converter.serialization)
    implementation(libs.vero.chat.libraries.network)

    implementation(libs.sqldelight.driver.android)
    implementation(libs.sqldelight.coroutines)
    implementation(libs.vero.chat.libraries.encrypteddb)
    implementation(libs.sqlcipher)
    implementation(libs.sqlite)
    implementation(libs.androidx.security.crypto)

    implementation("com.madgag.spongycastle:core:1.58.0.0")

}

sqldelight {
    databases {
        create("VeroDatabase") {
            // https://cashapp.github.io/sqldelight/2.0.0/android_sqlite/migrations/
            // To generate a .db file from your latest schema, run this task
            // ./gradlew generateDebugSessionDatabaseSchema
            // Test migration by running
            // ./gradlew verifySqlDelightMigration
            packageName.set(android.namespace) // Package name used for the database class.
            schemaOutputDirectory = File("src/main/sqldelight/databases")
            verifyMigrations = true
        }
    }
}
