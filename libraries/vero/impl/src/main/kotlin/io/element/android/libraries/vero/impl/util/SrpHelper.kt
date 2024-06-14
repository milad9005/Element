package io.element.android.libraries.vero.impl.util

import org.spongycastle.crypto.CryptoException
import org.spongycastle.crypto.Digest
import org.spongycastle.crypto.agreement.srp.SRP6Util
import org.spongycastle.crypto.digests.SHA256Digest
import org.spongycastle.util.BigIntegers
import org.spongycastle.util.encoders.Hex
import java.math.BigInteger
import java.security.SecureRandom
import javax.inject.Inject

/**
 *  Performs the low-level cryptography for Client. Abstracts the details from its host [ClientSecurityManager].
 *
 * A. registration:
 * SRPClient.generateSaltAndVerifier("four-digits-code","seed");
 *
 * B. auth:req:
 * 1. Generate A: A = Hex.toHexString(BigIntegers.asUnsignedByteArray(SRPClient.generateClientPublic()));
 * 2. Get a: a = Hex.toHexString(BigIntegers.asUnsignedByteArray(SRPClient.a))
 * 3. Send A and a
 *
 * C. auth:
 * 1. Generate X: X = SRPClient.generateX(Hex.decode("salt-string"), SrpHelper.asBytes("username-string"), SrpHelper.asBytes("pwd-string"));
 * 2. Get serverPublic as BigInteger: serverPublic = new BigInteger("token-string",16);
 * 3. Generate clientSecret: clientSecret = SRPClient.calculateSecret(serverPublic);
 * 4. Generate M: M = SRPClient.calculateCheck("username-string",Hex.decode("salt-string"));
 * 5. Return M: Hex.toHexString(BigIntegers.asUnsignedByteArray(M));
 * 6. Optional but recommended -> calculate check and make sure it equals the server response: clientCheck = SrpHelper.hash(SRPClient.A,M,SrpHelper.hash(clientSecret));
 *
 */
@Suppress(
    "PrivatePropertyName",
    "VariableNaming",
    "ConstructorParameterNaming"
) // Crypto naming conventions: 'N', 'G', etc.
class SrpHelper @Inject constructor() {

    private val secureRandom = SecureRandom()

    // Large safe 1024 bit prime number N to server as seed for Secure Remote Password cryptography.
    private val N = BigInteger(
        "eeaf0ab9adb38dd69c33f80afa8fc5e86072618775ff3c0b9ea2314c9c256576d674df7496ea81d3383b4813d692c6e0e0d5d8e250b98be48e495c1d6089dad15dc7d7b46154d6b6ce8ef4ad69b15d4982559b297bcf1885c529f566660e57ec68edbc3c05726cc02fd4cbf4976eaa9afd5138fe8376435b9fc61d2fc0eb06e3",
        16
    )

    private val G = BigInteger("02", 16)

    private val digest: Digest
        get() = SHA256Digest()

    val srpClient: SrpClient by lazy {
        SrpClient(N, G, digest, secureRandom)
    }

    class SrpClient(
        private val N: BigInteger,
        private val g: BigInteger,
        private var digest: Digest,
        private var random: SecureRandom
    ) {
        private lateinit var a: BigInteger
        private lateinit var A: BigInteger
        private lateinit var B: BigInteger
        private lateinit var x: BigInteger
        private lateinit var u: BigInteger
        private lateinit var S: BigInteger
        private lateinit var K: BigInteger
        private lateinit var M: BigInteger

        val kString: String
            get() {
                val bytes = BigIntegers.asUnsignedByteArray(K)
                return Hex.toHexString(bytes)
            }

        fun generateX(salt: ByteArray, username: ByteArray, password: ByteArray) {
            x = calculateX(salt, username, password)
        }

        private fun calculateX(salt: ByteArray, username: ByteArray, password: ByteArray): BigInteger {
            val digestX = digest
            val output = ByteArray(digestX.digestSize)

            digestX.update(username, 0, username.size)
            digestX.update(":".toByteArray(), 0, 1)
            digestX.update(password, 0, password.size)
            digestX.doFinal(output, 0)

            digestX.update(salt, 0, salt.size)
            digestX.update(output, 0, output.size)
            digestX.doFinal(output, 0)

            return BigInteger(1, output)
        }

        fun generateVerifier(salt: ByteArray, username: ByteArray, password: ByteArray): BigInteger {
            return g.modPow(calculateX(salt, username, password), N)
        }

        fun generateClientPublic(): BigInteger {
            a = selectPrivateValue()
            A = g.modPow(a, N)
            return A
        }

        @Throws(CryptoException::class)
        fun calculateSecret(serverB: BigInteger): BigInteger {
            B = SRP6Util.validatePublicValue(N, serverB)
            u = SRP6Util.calculateU(digest, N, A, B)
            S = calculateS()
            return S
        }

        fun calculateCheck(clientId: String, salt: ByteArray): BigInteger {
            K = hash(S)
            M = hash(
                hash(N).xor(hash(g)),
                hashString(clientId),
                BigIntegers.fromUnsignedByteArray(salt),
                A,
                B,
                K
            ).mod(N)
            return M
        }

        private fun selectPrivateValue(): BigInteger {
            return SRP6Util.generatePrivateValue(digest, N, g, random)
        }

        private fun calculateS(): BigInteger {
            val k = SRP6Util.calculateK(digest, N, g)
            val exp = u.multiply(x).add(a)
            val tmp = g.modPow(x, N).multiply(k).mod(N)
            return B.subtract(tmp).mod(N).modPow(exp, N)
        }

        fun hash(vararg args: BigInteger): BigInteger {
            val digest = digest
            // final int padLength = (N.bitLength() + 7) / 8;

            for (i in args.indices) {
                val bytes = BigIntegers.asUnsignedByteArray(args[i]) // getPadded(args[i], padLength);
                digest.update(bytes, 0, bytes.size)
            }

            val output = ByteArray(digest.digestSize)
            digest.doFinal(output, 0)

            return BigInteger(1, output)
        }

        private fun hashString(vararg args: String): BigInteger {
            return BigInteger(1, hash(*args))
        }

        private fun hash(vararg args: String): ByteArray {
            val digest = digest
            for (i in args.indices) {
                val bytes = args[i].toByteArray()
                digest.update(bytes, 0, bytes.size)
            }
            val output = ByteArray(digest.digestSize)
            digest.doFinal(output, 0)
            return output
        }
    }
}
