package bsb.dev.bsb_bangking_jp.core.crypto

import android.util.Base64
import org.bouncycastle.crypto.generators.Ed25519KeyPairGenerator
import org.bouncycastle.crypto.params.Ed25519KeyGenerationParameters
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters
import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters
import org.bouncycastle.crypto.util.SubjectPublicKeyInfoFactory
import java.security.SecureRandom

object Ed25519KeyUtils {

    data class GeneratedKeyPair(
        val privateKeyRawBase64: String, // disimpan lokal, dipakai untuk X-Signature nanti
        val publicKeyPem: String,        // dikirim ke server
    )

    fun generateKeyPair(): GeneratedKeyPair {
        val generator = Ed25519KeyPairGenerator().apply {
            init(Ed25519KeyGenerationParameters(SecureRandom()))
        }
        val keyPair = generator.generateKeyPair()

        val privateKey = keyPair.private as Ed25519PrivateKeyParameters
        val publicKey = keyPair.public as Ed25519PublicKeyParameters

        val privateBase64 = Base64.encodeToString(privateKey.encoded, Base64.NO_WRAP)

        val spkiDer = SubjectPublicKeyInfoFactory.createSubjectPublicKeyInfo(publicKey).encoded
        val publicPem = toPem("PUBLIC KEY", spkiDer)

        return GeneratedKeyPair(privateBase64, publicPem)
    }

    private fun toPem(label: String, der: ByteArray): String {
        val b64 = Base64.encodeToString(der, Base64.NO_WRAP)
        return buildString {
            append("-----BEGIN $label-----\n")
            b64.chunked(64).forEach { append(it).append('\n') }
            append("-----END $label-----")
        }
    }
}