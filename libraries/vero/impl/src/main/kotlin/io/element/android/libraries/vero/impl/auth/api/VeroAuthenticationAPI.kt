package io.element.android.libraries.vero.impl.auth.api

import io.element.android.libraries.vero.impl.auth.api.model.ChallengeRequest
import io.element.android.libraries.vero.impl.auth.api.model.CompleteRequest
import io.element.android.libraries.vero.impl.auth.api.model.ChallengeResponse
import io.element.android.libraries.vero.impl.auth.api.model.CompleteResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface VeroAuthenticationAPI {

    @POST("api/auth/challenge")
    suspend fun challenge(@Body request: ChallengeRequest): Response<ChallengeResponse>

    @POST("api/auth/complete")
    suspend fun complete(
        @Body request: CompleteRequest,
        @Header("Cookie") veroSession: String
    ): Response<CompleteResponse>

}
