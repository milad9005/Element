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

package io.element.android.libraries.vero.impl.di

import com.squareup.anvil.annotations.ContributesTo
import dagger.Module
import dagger.Provides
import io.element.android.libraries.di.AppScope
import io.element.android.libraries.vero.impl.auth.api.VeroAuthenticationAPI
import io.element.android.libraries.vero.impl.contact.api.VeroContactAPI
import io.element.android.libraries.vero.impl.network.VeroRetrofit
import io.element.android.libraries.vero.impl.profile.api.VeroProfileAPI

@Module
@ContributesTo(AppScope::class)
object VeroNetworkModule {

    @Provides
    fun providesVeroContactAPI(veroRetrofit: VeroRetrofit): VeroContactAPI {
        return veroRetrofit.create(VeroContactAPI::class.java)
    }

    @Provides
    fun providesVeroAuthenticationAPI(veroRetrofit: VeroRetrofit): VeroAuthenticationAPI {
        return veroRetrofit.create(VeroAuthenticationAPI::class.java)
    }

    @Provides
    fun providesVeroProfileAPI(veroRetrofit: VeroRetrofit): VeroProfileAPI {
        return veroRetrofit.create(VeroProfileAPI::class.java)
    }
}
