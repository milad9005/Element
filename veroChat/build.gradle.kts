
@file:Suppress("UnstableApiUsage")

import com.android.build.api.variant.FilterConfiguration.FilterType.ABI
import extension.allFeaturesImpl
import extension.allLibrariesImpl
import extension.allServicesImpl
import extension.koverDependencies
import extension.locales
import extension.setupKover

plugins {
    id("io.element.android-compose-library")
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.anvil)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kapt)
    alias(libs.plugins.knit)
    id("kotlin-parcelize")


}

setupKover()

android {
    namespace = "io.element.android.x"

    defaultConfig {

        ndk {
            abiFilters += listOf("armeabi-v7a", "x86", "arm64-v8a", "x86_64")
        }

        buildConfigField("String", "GIT_REVISION", "\"\"")
        buildConfigField("String", "GIT_BRANCH_NAME", "\"\"")
        buildConfigField("String", "SHORT_FLAVOR_DESCRIPTION", "\"G\"")
        buildConfigField("String", "FLAVOR_DESCRIPTION", "\"GooglePlay\"")

        splits {
            abi {
                isEnable = true
                reset()
                include("armeabi-v7a", "x86", "arm64-v8a", "x86_64")
                isUniversalApk = true
            }
        }

        defaultConfig {
            resourceConfigurations += locales
        }
    }

    signingConfigs {
        getByName("debug") {
            keyAlias = "androiddebugkey"
            keyPassword = "android"
            storeFile = file("./signature/debug.keystore")
            storePassword = "android"
        }
        register("nightly") {
            keyAlias = System.getenv("ELEMENT_ANDROID_NIGHTLY_KEYID")
                ?: project.property("signing.element.nightly.keyId") as? String?
            keyPassword = System.getenv("ELEMENT_ANDROID_NIGHTLY_KEYPASSWORD")
                ?: project.property("signing.element.nightly.keyPassword") as? String?
            storeFile = file("./signature/nightly.keystore")
            storePassword = System.getenv("ELEMENT_ANDROID_NIGHTLY_STOREPASSWORD")
                ?: project.property("signing.element.nightly.storePassword") as? String?
        }
    }

    buildTypes {
        getByName("debug") {
            resValue("string", "app_name", "Element X dbg")
        }

        getByName("release") {
            resValue("string", "app_name", "Vero chat")

            postprocessing {
                isRemoveUnusedCode = true
                isObfuscate = false
                isOptimizeCode = true

                proguardFiles("proguard-rules.pro")
            }
        }

        register("nightly") {
            val release = getByName("release")
            initWith(release)
            resValue("string", "app_name", "Element X nightly")
            matchingFallbacks += listOf("release")
            signingConfig = signingConfigs.getByName("nightly")

            postprocessing {
                initWith(release.postprocessing)
            }
        }
    }

    buildFeatures {
        buildConfig = true
    }
}

apply {
    plugin("kotlinx-knit")
}

knit {
    files = fileTree(project.rootDir) {
        include(
            "**/*.md",
            "**/*.kt",
            "*/*.kts",
        )
        exclude(
            "**/build/**",
            "*/.gradle/**",
            "*/towncrier/template.md",
            "**/CHANGES.md",
        )
    }
}

dependencies {

    implementation(kotlin("stdlib"))

    allLibrariesImpl()
    allServicesImpl()
    allFeaturesImpl(rootDir, logger)
    implementation(projects.features.migration.api)
    implementation(projects.anvilannotations)
    implementation(projects.appnav)
    implementation(projects.appconfig)
    anvil(projects.anvilcodegen)

//    "gplayImplementation"(projects.libraries.pushproviders.firebase)
    implementation(projects.libraries.pushproviders.unifiedpush)

    implementation(libs.appyx.core)
    implementation(libs.androidx.splash)
    implementation(libs.androidx.core)
    implementation(libs.androidx.corektx)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.lifecycle.process)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.startup)
    implementation(libs.androidx.preference)
    implementation(libs.coil)

    implementation(platform(libs.network.okhttp.bom))
    implementation(libs.network.okhttp.logging)
    implementation(libs.serialization.json)

    implementation(libs.matrix.emojibase.bindings)

    implementation(libs.dagger)
    kapt(libs.dagger.compiler)

    testImplementation(libs.test.junit)
    testImplementation(libs.test.robolectric)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.molecule.runtime)
    testImplementation(libs.test.truth)
    testImplementation(libs.test.turbine)
    testImplementation(projects.libraries.matrix.test)

    ksp(libs.showkase.processor)
    koverDependencies()
}

//publishing {
//    publications {
//        create<MavenPublication>("DebugAar") {
//            groupId = "co.vero.chat"
//            artifactId = "vero-chat"
//            version = "1.0.0-SNAPSHOT"
//            afterEvaluate {
//                artifact(tasks.getByName("bundleDebugAar"))
//            }
//
//            pom.withXml {
//                val dependenciesNode = asNode().appendNode("dependencies")
//
//                configurations["implementation"].allDependencies.forEach { dependency ->
//                    val dependencyNode = dependenciesNode.appendNode("dependency")
//                    dependencyNode.appendNode("groupId", dependency.group)
//                    dependencyNode.appendNode("artifactId", dependency.name)
//                    dependencyNode.appendNode("version", dependency.version)
//                }
//            }
//        }
//    }
//
//    repositories {
//        mavenLocal()
//    }
//}
