package bsb.dev.bsb_bangking_jp.feature.init.data

import bsb.dev.bsb_bangking_jp.core.crypto.Ed25519KeyUtils
import bsb.dev.bsb_bangking_jp.core.device.AppPreferences
import bsb.dev.bsb_bangking_jp.core.device.DeviceContext
import bsb.dev.bsb_bangking_jp.core.device.SecureStorageService
import bsb.dev.bsb_bangking_jp.core.network.ApiException
import bsb.dev.bsb_bangking_jp.core.network.NetworkErrorMapper
import bsb.dev.bsb_bangking_jp.core.network.header.ApiHeaders
import bsb.dev.bsb_bangking_jp.feature.init.domain.InitRepository

class InitRepositoryImpl(
    private val api: InitApiService,
    private val secureStorage: SecureStorageService, // rahasia -> private key
    private val appPreferences: AppPreferences,       // flag biasa -> init success
) : InitRepository {

    override suspend fun initDevice(): Result<Unit> {
        return try {
            // 1. Generate keypair Ed25519
            val keyPair = Ed25519KeyUtils.generateKeyPair()
            secureStorage.savePrivateKey(keyPair.privateKeyRawBase64)

            // 2. Susun body -- iccid/imei/imsi dikosongkan dulu
            val request = InitDeviceRequest(
                deviceName = DeviceContext.deviceName,
                os = DeviceContext.os,
                publicKey = keyPair.publicKeyPem,
            )

            // 3. Hit API pakai header lengkap
            val response = api.initDevice(headers = ApiHeaders.full(), body = request)

            if (response.isSuccessful) {
                appPreferences.saveInitSuccess(true)
                Result.success(Unit)
            } else {
                Result.failure(Exception("Init gagal: HTTP ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(ApiException(null, NetworkErrorMapper.toUserMessage(e)))
        }
    }
}