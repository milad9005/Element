package io.element.android.libraries.vero.impl.auth

import com.squareup.anvil.annotations.ContributesBinding
import io.element.android.libraries.core.coroutine.CoroutineDispatchers
import io.element.android.libraries.core.extensions.mapFailure
import io.element.android.libraries.di.AppScope
import io.element.android.libraries.network.util.ApiResponse
import io.element.android.libraries.network.util.NetworkException
import io.element.android.libraries.network.util.safeExecute
import io.element.android.libraries.vero.api.auth.VeroAuthenticationService
import io.element.android.libraries.vero.api.auth.VeroCredential
import io.element.android.libraries.vero.api.auth.VeroUser
import io.element.android.libraries.vero.impl.auth.api.VeroAuthenticationAPI
import io.element.android.libraries.vero.impl.auth.api.model.ChallengeRequest
import io.element.android.libraries.vero.impl.auth.api.model.ChallengeResponse
import io.element.android.libraries.vero.impl.auth.api.model.CompleteRequest
import io.element.android.libraries.vero.impl.auth.api.model.CompleteResponse
import io.element.android.libraries.vero.impl.util.ClientSecurityManager
import io.element.android.libraries.vero.impl.util.VeroApiErrorResponseConvertor
import io.element.android.libraries.vero.impl.util.cookies
import io.element.android.libraries.vero.impl.util.parseVeroSession
import io.element.android.libraries.vero.impl.util.toVeroException
import io.element.android.libraries.vero.impl.util.toVeroSession
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import javax.inject.Inject

@ContributesBinding(AppScope::class)
class VeroAuthenticationServiceImpl @Inject constructor(
    private val api: VeroAuthenticationAPI,
    private val manager: ClientSecurityManager,
    private val coroutineDispatchers: CoroutineDispatchers,
    private val convertor: VeroApiErrorResponseConvertor,
) : VeroAuthenticationService {

    override suspend fun login(veroCredential: VeroCredential): Result<VeroUser> {
        return kotlin.runCatching {
            withContext(coroutineDispatchers.io) {
                val challengeResponse = challengeLogin(veroCredential.username)
                val completeResponse = completeLogin(veroCredential, challengeResponse)
                VeroUser(
                    username = veroCredential.username,
                    userId = completeResponse.userId,
                    token = completeResponse.veroPass.jwt,
                    refreshToken = completeResponse.veroPass.refresh.tok
                )
            }
        }.mapFailure { it.mapVeroException() }
    }

    private suspend fun challengeLogin(email: String): ChallengeResponse {
        val clientPub = manager.authToken()
        when (val response = api.challenge(ChallengeRequest(email, clientPub)).safeExecute()) {
            is ApiResponse.Error -> throw response.error
            is ApiResponse.Exception -> throw response.throwable
            is ApiResponse.Success -> {
                val veroSession = response.headers.cookies()?.parseVeroSession()
                return response.data.copy(session = veroSession)
            }
        }
    }

    private suspend fun completeLogin(
        veroCredential: VeroCredential,
        challengeResponse: ChallengeResponse,
    ): CompleteResponse {
        val clientProof = manager.generateSrpAuthString(
            email = veroCredential.username,
            password = veroCredential.password,
            salt = challengeResponse.salt,
            token = challengeResponse.serverPub
        )
        val cookie = challengeResponse.session.toVeroSession()
        val request = CompleteRequest(login = veroCredential.username, clientProof = clientProof)
        when (val response = api.complete(request, veroSession = cookie).safeExecute()) {
            is ApiResponse.Error -> throw response.error
            is ApiResponse.Exception -> throw response.throwable
            is ApiResponse.Success -> return response.data
        }
    }

    private fun Throwable.mapVeroException(): Throwable {
        return try {
            when (this) {
                is HttpException -> convertor.convert(this)?.toVeroException() ?: return this
                else -> this
            }
        } catch (e: Exception) {
            NetworkException.MalformedJsonException(e.message.toString())
        }
    }
}
