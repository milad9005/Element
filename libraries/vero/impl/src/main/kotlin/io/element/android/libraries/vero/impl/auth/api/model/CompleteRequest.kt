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

package io.element.android.libraries.vero.impl.auth.api.model

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class CompleteRequest(
    val login: String,
    val clientProof: String,
    val origin: String = "vero_social",
    val device: Device = Device(id = UUID.randomUUID().toString(), type = "android")
)

@Serializable
data class Device(
    val type: String,
    val id: String
)
