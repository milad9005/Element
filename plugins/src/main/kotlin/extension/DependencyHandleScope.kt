/*
 * Copyright (c) 2022 New Vector Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package extension

import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Action
import org.gradle.api.artifacts.ExternalModuleDependency
import org.gradle.api.logging.Logger
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.closureOf
import org.gradle.kotlin.dsl.exclude
import org.gradle.kotlin.dsl.project
import java.io.File

private fun DependencyHandlerScope.implementation(dependency: Any) = dependencies.add("implementation", dependency)

// Implementation + config block
private fun DependencyHandlerScope.implementation(
    dependency: Any,
    config: Action<ExternalModuleDependency>
) = dependencies.add("implementation",  dependency, closureOf<ExternalModuleDependency> { config.execute(this) })

private fun DependencyHandlerScope.androidTestImplementation(dependency: Any) = dependencies.add("androidTestImplementation", dependency)

private fun DependencyHandlerScope.debugImplementation(dependency: Any) = dependencies.add("debugImplementation", dependency)

/**
 * Dependencies used by all the modules
 */
fun DependencyHandlerScope.commonDependencies(libs: LibrariesForLibs) {
    implementation(libs.timber)
}

/**
 * Dependencies used by all the modules with composable items
 */
fun DependencyHandlerScope.composeDependencies(libs: LibrariesForLibs) {
    val composeBom = platform(libs.androidx.compose.bom)
    implementation(composeBom)
    androidTestImplementation(composeBom)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.activity.compose)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    implementation(libs.showkase)
    implementation(libs.kotlinx.collections.immutable)
}

private fun DependencyHandlerScope.addImplementationProjects(
    directory: File,
    path: String,
    nameFilter: String,
    logger: Logger,
) {
    directory.listFiles().orEmpty().also { it.sort() }.forEach { file ->
        if (file.isDirectory) {
            val newPath = "$path:${file.name}"
            val buildFile = File(file, "build.gradle.kts")
            if (buildFile.exists() && file.name == nameFilter) {
                implementation(project(newPath))
                logger.lifecycle("Added implementation(project($newPath))")
            } else {
                addImplementationProjects(file, newPath, nameFilter, logger)
            }
        }
    }
}

fun DependencyHandlerScope.allLibrariesImpl(libs: LibrariesForLibs) {
    implementation(libs.vero.chat.libraries.androidutils)
    implementation(libs.vero.chat.libraries.deeplink)
    implementation(libs.vero.chat.libraries.designsystem)
    implementation(libs.vero.chat.libraries.matrix.impl)
    implementation(libs.vero.chat.libraries.matrixui)
    implementation(libs.vero.chat.libraries.network)
    implementation(libs.vero.chat.libraries.core)
    implementation(libs.vero.chat.libraries.eventformatter.impl)
    implementation(libs.vero.chat.libraries.indicator.impl)
    implementation(libs.vero.chat.libraries.permissions.impl)
    implementation(libs.vero.chat.libraries.push.impl)
    implementation(libs.vero.chat.libraries.push.impl)
    implementation(libs.vero.chat.libraries.featureflag.impl)
    implementation(libs.vero.chat.libraries.pushstore.impl)
    implementation(libs.vero.chat.libraries.preferences.impl)
    implementation(libs.vero.chat.libraries.architecture)
    implementation(libs.vero.chat.libraries.dateformatter.impl)
    implementation(libs.vero.chat.libraries.di)
    implementation(libs.vero.chat.libraries.session.storage.impl)
    implementation(libs.vero.chat.libraries.mediapickers.impl)
    implementation(libs.vero.chat.libraries.mediaupload.impl)
    implementation(libs.vero.chat.libraries.usersearch.impl)
    implementation(libs.vero.chat.libraries.textcomposer.impl)
    implementation(libs.vero.chat.libraries.roomselect.impl)
    implementation(libs.vero.chat.libraries.cryptography.impl)
    implementation(libs.vero.chat.libraries.voicerecorder.impl)
    implementation(libs.vero.chat.libraries.mediaplayer.impl)
    implementation(libs.vero.chat.libraries.mediaviewer.impl)
    implementation(libs.vero.chat.libraries.troubleshoot.impl)
    implementation(libs.vero.chat.libraries.fullscreenintent.impl)
    implementation(libs.vero.chat.libraries.vero.impl)
    implementation(libs.vero.chat.libraries.matrixnetwork.impl)
    implementation(libs.vero.chat.libraries.veromatrix.impl)
    implementation(libs.vero.chat.libraries.worker)
}

fun DependencyHandlerScope.allServicesImpl(libs: LibrariesForLibs) {
    // For analytics configuration, either use noop, or use the impl, with at least one analyticsproviders implementation

//    implementation(libs.vero.chat.services.analytics.noop)
    implementation(libs.vero.chat.services.analytics.impl)
    implementation(libs.vero.chat.services.analyticsproviders.posthog)
    implementation(libs.vero.chat.services.analyticsproviders.sentry)
    implementation(libs.vero.chat.services.apperror.impl)
    implementation(libs.vero.chat.services.appnavstate.impl)
    implementation(libs.vero.chat.services.toolbox.impl)
}

fun DependencyHandlerScope.allFeaturesApi(libs: LibrariesForLibs) {
    implementation(libs.vero.chat.features.analytics.api)

    implementation(libs.vero.chat.features.cachecleaner.api)

    implementation(libs.vero.chat.features.call.api)

    implementation(libs.vero.chat.features.createroom.api)

    implementation(libs.vero.chat.features.ftue.api)
    implementation(libs.vero.chat.features.lockscreen.api)

    implementation(libs.vero.chat.features.invite.api)

    implementation(libs.vero.chat.features.joinroom.api)

    implementation(libs.vero.chat.features.location.api)

    implementation(libs.vero.chat.features.login.api)

    implementation(libs.vero.chat.features.logout.api)

    implementation(libs.vero.chat.features.messages.api)

    implementation(libs.vero.chat.features.migration.api)

    implementation(libs.vero.chat.features.networkmonitor.api)

    implementation(libs.vero.chat.features.onboarding.api)

    implementation(libs.vero.chat.features.poll.api)

    implementation(libs.vero.chat.features.preferences.api)

    implementation(libs.vero.chat.features.rageshake.api)

    implementation(libs.vero.chat.features.roomaliasresolver.api)

    implementation(libs.vero.chat.features.roomdetails.api)

    implementation(libs.vero.chat.features.roomdirectory.api)

    implementation(libs.vero.chat.features.roomlist.api)

    implementation(libs.vero.chat.features.securebackup.api)

    implementation(libs.vero.chat.features.share.api)

    implementation(libs.vero.chat.features.signedout.api)

    implementation(libs.vero.chat.features.userprofile.api)
    implementation(libs.vero.chat.features.leaveroom.api)

    implementation(libs.vero.chat.features.verifysession.api)

    implementation(libs.vero.chat.features.viewfolder.api)
//    addImplementationProjects(featuresDir, ":features", "api", logger)
}

fun DependencyHandlerScope.allFeaturesImpl(libs: LibrariesForLibs) {
//    val featuresDir = File(rootDir, "features")
//    addImplementationProjects(featuresDir, ":features", "impl", logger)

    implementation(libs.vero.chat.features.analytics.impl)
    implementation(libs.vero.chat.features.cachecleaner.impl)
    implementation(libs.vero.chat.features.call.impl)
    implementation(libs.vero.chat.features.createroom.impl)
    implementation(libs.vero.chat.features.leaveroom.impl)
    implementation(libs.vero.chat.features.ftue.impl)
    implementation(libs.vero.chat.features.invite.impl)
    implementation(libs.vero.chat.features.joinroom.impl)
    implementation(libs.vero.chat.features.location.impl)
    implementation(libs.vero.chat.features.login.impl)
    implementation(libs.vero.chat.features.logout.impl)
    implementation(libs.vero.chat.features.messages.impl)
    implementation(libs.vero.chat.features.migration.impl)
    implementation(libs.vero.chat.features.networkmonitor.impl)
    implementation(libs.vero.chat.features.onboarding.impl)
    implementation(libs.vero.chat.features.lockscreen.impl)
    implementation(libs.vero.chat.features.poll.impl)
    implementation(libs.vero.chat.features.preferences.impl)
    implementation(libs.vero.chat.features.rageshake.impl)
    implementation(libs.vero.chat.features.roomaliasresolver.impl)
    implementation(libs.vero.chat.features.roomdetails.impl)
    implementation(libs.vero.chat.features.roomdirectory.impl)
    implementation(libs.vero.chat.features.roomlist.impl)
    implementation(libs.vero.chat.features.securebackup.impl)
    implementation(libs.vero.chat.features.share.impl)
    implementation(libs.vero.chat.features.signedout.impl)
    implementation(libs.vero.chat.features.userprofile.impl)
    implementation(libs.vero.chat.features.verifysession.impl)
    implementation(libs.vero.chat.features.viewfolder.impl)


}
