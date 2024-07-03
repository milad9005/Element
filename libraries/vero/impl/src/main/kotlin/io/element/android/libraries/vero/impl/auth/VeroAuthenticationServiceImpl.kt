package io.element.android.libraries.vero.impl.auth

import com.squareup.anvil.annotations.ContributesBinding
import io.element.android.libraries.di.AppScope
import io.element.android.libraries.vero.api.auth.VeroAuthenticationService
import io.element.android.libraries.vero.api.auth.VeroCredential
import io.element.android.libraries.vero.api.auth.VeroUser
import io.element.android.libraries.vero.impl.auth.api.VeroAuthenticationAPI
import io.element.android.libraries.vero.impl.auth.api.model.ChallengeRequest
import io.element.android.libraries.vero.impl.auth.api.model.ChallengeResponse
import io.element.android.libraries.vero.impl.auth.api.model.CompleteRequest
import io.element.android.libraries.vero.impl.auth.api.model.CompleteResponse
import io.element.android.libraries.vero.impl.util.ClientSecurityManager
import retrofit2.Response
import javax.inject.Inject

@ContributesBinding(AppScope::class)
class VeroAuthenticationServiceImpl @Inject constructor(
    private val api: VeroAuthenticationAPI,
    private val manager: ClientSecurityManager
) : VeroAuthenticationService {

    /*
     *  todo ->
     *      Handle Network exceptions
     *      Handle Network errors
     *      Convert exceptions and errors
     */
    override suspend fun login(veroCredential: VeroCredential): VeroUser {
        val challengeLoginResult = challengeLogin(veroCredential.username)
        val challengeResponsePair = challengeLoginResult.onFailure { throw it }.getOrThrow()
        val completeLoginResult = completeLogin(
            session = challengeResponsePair.first,
            email = veroCredential.username,
            password = veroCredential.password,
            salt = challengeResponsePair.second.salt,
            serverPub = challengeResponsePair.second.serverPub,
        ).onFailure { throw it }
        val completeResponse = completeLoginResult.getOrThrow()
        return VeroUser(
            username = veroCredential.username,
            userId = completeResponse.userId,
            token = completeResponse.veroPass.jwt,
            refreshToken = completeResponse.veroPass.refresh.tok
        )
    }

    private suspend fun challengeLogin(email: String): Result<Pair<String?, ChallengeResponse>> {
        return runCatching {
            val clientPub = manager.authToken()
            val response = api.challenge(ChallengeRequest(email, clientPub))
            val veroSession: String? = response.getVeroSession()
            if (response.isSuccessful)
                veroSession to response.body()!!
            else
                throw Exception(response.errorBody().toString())
        }
    }

    private suspend fun completeLogin(
        session: String?,
        email: String,
        password: String,
        salt: String,
        serverPub: String
    ): Result<CompleteResponse> {
        return runCatching {
            val clientProof = manager.generateSrpAuthString(
                email = email,
                password = password,
                salt = salt,
                token = serverPub
            )
            val cookie = "vero-session=${session};"
            val request = CompleteRequest(login = email, clientProof = clientProof)
            val response = api.complete(request, veroSession = cookie)
            if (response.isSuccessful)
                response.body()!!
            else
                throw Exception(response.errorBody().toString())
        }
    }
}

fun <T> Response<T>.cookies(): Pair<String, String>? {
    return headers().find { it.first == "set-cookie" }
}

fun Response<ChallengeResponse>.getVeroSession(): String? {
    val cookies = cookies()
    return cookies?.second?.split(";")
        ?.find { it.contains("vero-session") }
        ?.split("=")
        ?.get(1)
}
