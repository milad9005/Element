/*
 * Copyright (c) 2024 New Vector Ltd
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

import kotlinx.kover.gradle.plugin.dsl.AggregationType
import kotlinx.kover.gradle.plugin.dsl.CoverageUnit
import kotlinx.kover.gradle.plugin.dsl.GroupingEntityType
import kotlinx.kover.gradle.plugin.dsl.KoverProjectExtension
import kotlinx.kover.gradle.plugin.dsl.KoverVariantCreateConfig
import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.configurationcache.extensions.capitalized
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.assign

enum class KoverVariant(val variantName: String) {
    Presenters("presenters"),
    States("states"),
    Views("views"),
}

val koverVariants = KoverVariant.values().map { it.variantName }

val localAarProjects = listOf(
    ":libraries:rustsdk",
    ":libraries:textcomposer:lib"
)

val excludedKoverSubProjects = listOf(
    ":app",
    ":samples",
    "co.vero.chat:anvilannotations:1.0.0-SNAPSHOT",
    "co.vero.chat:anvilcodegen:1.0.0-SNAPSHOT",
    ":samples:minimal",
    "co.vero.chat:tests-testutils:1.0.0-SNAPSHOT",
    // Exclude `:libraries:matrix:impl` module, it contains only wrappers to access the Rust Matrix
    // SDK api, so it is not really relevant to unit test it: there is no logic to test.
    "co.vero.chat:libraries-matrix-impl:1.0.0-SNAPSHOT",
    // Exclude modules which are not Android libraries
    // See https://github.com/Kotlin/kotlinx-kover/issues/312
    "co.vero.chat:appconfig:1.0.0-SNAPSHOT",
    "co.vero.chat:libraries-core:1.0.0-SNAPSHOT",
    "co.vero.chat:libraries-coroutines:1.0.0-SNAPSHOT",
    "co.vero.chat:libraries-di:1.0.0-SNAPSHOT",
) + localAarProjects

private fun Project.kover(action: Action<KoverProjectExtension>) {
    (this as org.gradle.api.plugins.ExtensionAware).extensions.configure("kover", action)
}

fun Project.setupKover() {
    // Create verify all task joining all existing verification tasks
    task("koverVerifyAll") {
        group = "verification"
        description = "Verifies the code coverage of all sublibs.vero.chat."
        val dependencies = listOf(":app:koverVerifyGplayDebug") + koverVariants.map { ":app:koverVerify${it.capitalized()}" }
        dependsOn(dependencies)

    }
    // https://kotlin.github.io/kotlinx-kover/
    // Run `./gradlew :app:koverHtmlReport` to get report at ./app/build/reports/kover
    // Run `./gradlew :app:koverXmlReport` to get XML report
    kover {
        reports {
            filters {
                excludes {
                    classes(
                        // Exclude generated classes.
                        "*_ModuleKt",
                        "anvil.hint.binding.io.element.*",
                        "anvil.hint.merge.*",
                        "anvil.hint.multibinding.io.element.*",
                        "anvil.module.*",
                        "com.airbnb.android.showkase*",
                        "io.element.android.libraries.designsystem.showkase.*",
                        "io.element.android.x.di.DaggerAppComponent*",
                        "*_Factory",
                        "*_Factory_Impl",
                        "*_Factory$*",
                        "*_Module",
                        "*_Module$*",
                        "*Module_Provides*",
                        "Dagger*Component*",
                        "*ComposableSingletons$*",
                        "*_AssistedFactory_Impl*",
                        "*BuildConfig",
                        // Generated by Showkase
                        "*Ioelementandroid*PreviewKt$*",
                        "*Ioelementandroid*PreviewKt",
                        // Other
                        // We do not cover Nodes (normally covered by maestro, but code coverage is not computed with maestro)
                        "*Node",
                        "*Node$*",
                        "*Presenter\$present\$*",
                        // Forked from compose
                        "io.element.android.libraries.designsystem.theme.components.bottomsheet.*",
                        // Test presenters
                        "io.element.android.features.leaveroom.fake.FakeLeaveRoomPresenter"
                    )
                    annotatedBy(
                        "androidx.compose.ui.tooling.preview.Preview",
                        "io.element.android.libraries.architecture.coverage.ExcludeFromCoverage",
                        "io.element.android.libraries.designsystem.preview.PreviewsDayNight",
                        "io.element.android.libraries.designsystem.preview.PreviewWithLargeHeight",
                    )
                }
            }

            total {
                verify {
                    // General rule: minimum code coverage.
                    rule("Global minimum code coverage.") {
                        groupBy = GroupingEntityType.APPLICATION
                        bound {
                            minValue = 70
                            // Setting a max value, so that if coverage is bigger, it means that we have to change minValue.
                            // For instance if we have minValue = 20 and maxValue = 30, and current code coverage is now 31.32%, update
                            // minValue to 25 and maxValue to 35.
                            maxValue = 80
                            coverageUnits = CoverageUnit.INSTRUCTION
                            aggregationForGroup = AggregationType.COVERED_PERCENTAGE
                        }
                    }
                }
            }
            variant(KoverVariant.Presenters.variantName) {
                verify {
                    // Rule to ensure that coverage of Presenters is sufficient.
                    rule("Check code coverage of presenters") {
                        groupBy = GroupingEntityType.CLASS

                        bound {
                            minValue = 85
                            coverageUnits = CoverageUnit.INSTRUCTION
                            aggregationForGroup = AggregationType.COVERED_PERCENTAGE
                        }
                    }
                }
                filters {
                    includes {
                        classes(
                            "*Presenter",
                        )
                    }
                    excludes {
                        classes(
                            "*Fake*Presenter*",
                            "io.element.android.appnav.loggedin.LoggedInPresenter$*",
                            // Some options can't be tested at the moment
                            "io.element.android.features.preferences.impl.developer.DeveloperSettingsPresenter$*",
                            // Need an Activity to use rememberMultiplePermissionsState
                            "io.element.android.features.location.impl.common.permissions.DefaultPermissionsPresenter",
                            "*Presenter\$present\$*",
                            // Too small to be > 85% tested
                            "io.element.android.libraries.fullscreenintent.impl.DefaultFullScreenIntentPermissionsPresenter",
                        )
                    }
                }
            }
            variant(KoverVariant.States.variantName) {
                verify {
                    // Rule to ensure that coverage of States is sufficient.
                    rule("Check code coverage of states") {
                        groupBy = GroupingEntityType.CLASS
                        bound {
                            minValue = 90
                            coverageUnits = CoverageUnit.INSTRUCTION
                            aggregationForGroup = AggregationType.COVERED_PERCENTAGE
                        }
                    }
                }
                filters {
                    includes {
                        classes(
                            "^*State$",
                        )
                    }
                    excludes {
                        classes(
                            "io.element.android.appnav.root.RootNavState*",
                            "io.element.android.libraries.matrix.api.timeline.item.event.OtherState$*",
                            "io.element.android.libraries.matrix.api.timeline.item.event.EventSendState$*",
                            "io.element.android.libraries.matrix.api.room.RoomMembershipState*",
                            "io.element.android.libraries.matrix.api.room.MatrixRoomMembersState*",
                            "io.element.android.libraries.push.impl.notifications.NotificationState*",
                            "io.element.android.features.messages.impl.media.local.pdf.PdfViewerState",
                            "io.element.android.features.messages.impl.media.local.LocalMediaViewState",
                            "io.element.android.features.location.impl.map.MapState*",
                            "io.element.android.libraries.matrix.api.timeline.item.event.LocalEventSendState*",
                            "io.element.android.libraries.designsystem.swipe.SwipeableActionsState*",
                            "io.element.android.features.messages.impl.timeline.components.ExpandableState*",
                            "io.element.android.features.messages.impl.timeline.model.bubble.BubbleState*",
                            "io.element.android.libraries.maplibre.compose.CameraPositionState*",
                            "io.element.android.libraries.maplibre.compose.SaveableCameraPositionState",
                            "io.element.android.libraries.maplibre.compose.SymbolState*",
                            "io.element.android.features.ftue.api.state.*",
                            "io.element.android.features.ftue.impl.welcome.state.*",
                        )
                    }
                }
            }
            variant(KoverVariant.Views.variantName) {
                verify {
                    // Rule to ensure that coverage of Views is sufficient (deactivated for now).
                    rule("Check code coverage of views") {
                        groupBy = GroupingEntityType.CLASS
                        bound {
                            // TODO Update this value, for now there are too many missing tests.
                            minValue = 0
                            coverageUnits = CoverageUnit.INSTRUCTION
                            aggregationForGroup = AggregationType.COVERED_PERCENTAGE
                        }
                    }
                }
                filters {
                    includes {
                        classes(
                            "*ViewKt",
                        )
                    }
                }
            }
        }
    }
}

fun Project.applyKoverPluginToAllSubProjects() = rootProject.subprojects {
    if (project.path !in localAarProjects) {
        apply(plugin = "org.jetbrains.kotlinx.kover")
        kover {
            currentProject {
                for (variant in koverVariants) {
                    createVariant(variant) {
                        defaultVariants()
                    }
                }
            }
        }
    }
}

fun KoverVariantCreateConfig.defaultVariants() {
    addWithDependencies("gplayDebug", "debug", optional = true)
}

fun Project.koverSubprojects() = project.rootProject.subprojects
    .filter {
        it.project.projectDir.resolve("build.gradle.kts").exists()
    }
    .map { it.path }
    .sorted()
    .filter {
        it !in excludedKoverSubProjects
    }

fun Project.koverDependencies() {
    project.koverSubprojects()
        .forEach {
            // println("Add $it to kover")
            dependencies.add("kover", project(it))
        }
}
