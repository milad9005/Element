package io.element.android.libraries.vero.impl.auth.api

import com.squareup.anvil.annotations.ContributesBinding
import io.element.android.libraries.core.uri.ensureTrailingSlash
import io.element.android.libraries.di.AppScope
import io.element.android.libraries.network.RetrofitFactory
import io.element.android.libraries.vero.impl.auth.api.model.ChallengeRequest
import io.element.android.libraries.vero.impl.auth.api.model.CompleteRequest
import io.element.android.libraries.vero.impl.auth.api.model.ChallengeResponse
import io.element.android.libraries.vero.impl.auth.api.model.CompleteResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import javax.inject.Inject
import javax.inject.Provider

interface PushGatewayApiFactory {
    fun create(baseUrl: String): VeroAuthenticationAPI
}

@ContributesBinding(AppScope::class)
class DefaultPushGatewayApiFactory @Inject constructor(
    private val retrofitFactory: RetrofitFactory,
) : PushGatewayApiFactory {
    override fun create(baseUrl: String): VeroAuthenticationAPI {
        return retrofitFactory.create(baseUrl).create(VeroAuthenticationAPI::class.java)
    }
}
