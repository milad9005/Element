package io.element.android.libraries.vero.impl.util

import org.spongycastle.crypto.CryptoException
import org.spongycastle.util.BigIntegers
import org.spongycastle.util.encoders.Hex
import java.math.BigInteger
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Provides an API for high-level security tasks for main server Client.
 *
 * @param srpHelper SrpHelper class that does the low-level heavy lifting.
 */
class ClientSecurityManager @Inject constructor(
    private val srpHelper: SrpHelper
) {

    private val srpA: BigInteger by lazy { srpHelper.srpClient.generateClientPublic() }
    private lateinit var srpB: BigInteger
    private lateinit var clientSecret: BigInteger

    fun saltAndVerifier(seedAndCode: String, name: String, pass: String): Pair<String, String> {
        val userGenSalt = SecurityUtil.sha256(seedAndCode)
        val verifier = verifyPassword(
            userGenSalt,
            name,
            pass
        )
        return userGenSalt to verifier
    }

    fun verifyPassword(userSalt: String, name: String, password: String): String =
        srpHelper.srpClient.generateVerifier(
            Hex.decode(userSalt),
            name.toByteArray(),
            password.toByteArray()
        ).toString(16)

    // Srp stage A. Gen public token.
    fun authToken(): String = Hex.toHexString(
        BigIntegers.asUnsignedByteArray(srpA)
    )

    // Generate SRP stage B string for server (from salt/token received in response to stage a)
    fun generateSrpAuthString(email: String, password: String, salt: String, token: String): String {
        val saltDecoded = Hex.decode(salt)
        srpHelper.srpClient.generateX(
            saltDecoded,
            email.toByteArray(),
            password.toByteArray()
        )
        val serverPublic = BigInteger(token, 16)
        try {
            clientSecret = srpHelper.srpClient.calculateSecret(serverPublic)
        } catch (e: CryptoException) {
            // TODO - how handling exceptions in core?
            // log.w("generateSrpAuthString CryptoException: ${e.message}")
            return ""
        }
        srpB = srpHelper.srpClient.calculateCheck(email, saltDecoded)
        return Hex.toHexString(BigIntegers.asUnsignedByteArray(srpB))
    }

    /**
     * Last step in verification: Check that client and server checks are equal.
     * @param check string  check returned by UserAuthRequest.Response.
     */
    fun verifySrpAuthCheck(check: String): Boolean {
        val serverCheck = BigInteger(check, 16)
        val clientCheck = srpHelper.srpClient.hash(srpA, srpB, srpHelper.srpClient.hash(clientSecret))
        return clientCheck == serverCheck
    }

    // Null check in case of CryptoException. Had never happened, but good to have.
    // TODO: Use tryOrNull when added to main project?
    fun getClientSecret(): BigInteger? = if (::clientSecret.isInitialized) clientSecret else null

    // Password needed for uploading content: userId, session id, SRP K.
    fun getUploadPassword(id: String, token: String): String =
        SecurityUtil.sha256("${id}${token}${srpHelper.srpClient.kString}")
}
