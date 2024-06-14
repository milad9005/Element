package io.element.android.libraries.vero.impl.auth

import com.squareup.anvil.annotations.ContributesBinding
import io.element.android.appconfig.AuthenticationConfig
import io.element.android.appconfig.VeroConfiguration
import io.element.android.libraries.core.extensions.mapFailure
import io.element.android.libraries.di.AppScope
import io.element.android.libraries.vero.api.auth.VeroAuthenticationService
import io.element.android.libraries.vero.api.auth.VeroUser
import io.element.android.libraries.vero.impl.auth.api.PushGatewayApiFactory
import io.element.android.libraries.vero.impl.auth.api.model.ChallengeRequest
import io.element.android.libraries.vero.impl.auth.api.model.ChallengeResponse
import io.element.android.libraries.vero.impl.auth.api.model.CompleteRequest
import io.element.android.libraries.vero.impl.auth.api.model.CompleteResponse
import io.element.android.libraries.vero.impl.util.ClientSecurityManager
import retrofit2.Response
import javax.inject.Inject

@ContributesBinding(AppScope::class)
class VeroAuthenticationServiceImpl @Inject constructor(
    private val factory: PushGatewayApiFactory,
    private val manager: ClientSecurityManager
) : VeroAuthenticationService {

    private val api = factory.create(VeroConfiguration.VERO_AUTH_URL)

    /*
     *  todo ->
     *      Handle Network exceptions
     *      Handle Network errors
     *      Convert exceptions and errors
     */
    override suspend fun login(username: String, password: String): Result<VeroUser> {
        return runCatching {
            val challengeLoginResult = challengeLogin(username)
            val challengeResponsePair = challengeLoginResult.getOrThrow()
            val completeLoginResult = completeLogin(
                session = challengeResponsePair.first,
                email = username,
                password = password,
                salt = challengeResponsePair.second.salt,
                serverPub = challengeResponsePair.second.serverPub,
            )
            val completeResponse = completeLoginResult.getOrThrow()
            return Result.success(
                VeroUser(
                    username = username,
                    userId = completeResponse.userId,
                    token = completeResponse.veroPass.jwt,
                    refreshToken = completeResponse.veroPass.refresh.tok
                )
            )
        }.mapFailure { it }
    }

    private suspend fun challengeLogin(email: String): Result<Pair<String?, ChallengeResponse>> {
        return runCatching {
            val clientPub = manager.authToken()
            val challenge = api.challenge(ChallengeRequest(email, clientPub))
            val veroSession: String? = challenge.getVeroSession()
            return Result.success(veroSession to challenge.body()!!)
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
            return@runCatching response.body()!!
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
