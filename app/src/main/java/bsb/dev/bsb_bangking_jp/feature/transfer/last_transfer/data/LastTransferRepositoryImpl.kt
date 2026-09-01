package bsb.dev.bsb_bangking_jp.feature.transfer.last_transfer.data

import bsb.dev.bsb_bangking_jp.core.network.ApiException
import bsb.dev.bsb_bangking_jp.core.network.GetWithBodyApiHelper
import bsb.dev.bsb_bangking_jp.core.network.NetworkErrorMapper
import bsb.dev.bsb_bangking_jp.core.session.ClearableRepository
import bsb.dev.bsb_bangking_jp.core.util.retry
import bsb.dev.bsb_bangking_jp.feature.beranda.domain.RekeningLainnyaRepository
import bsb.dev.bsb_bangking_jp.feature.transfer.last_transfer.domain.LastTransferItem
import bsb.dev.bsb_bangking_jp.feature.transfer.last_transfer.domain.LastTransferRepository

private const val SUCCESS_CODE = "0000"

class LastTransferRepositoryImpl(
    private val apiHelper: GetWithBodyApiHelper,
    private val rekeningRepository: RekeningLainnyaRepository,
) : LastTransferRepository, ClearableRepository {

    private var cache: List<LastTransferItem>? = null

    override suspend fun getLastTransfer(forceRefresh: Boolean): List<LastTransferItem> {
        if (!forceRefresh && cache != null) {
            return cache!!
        }
        val fresh = retry { fetchLastTransfer() }
        cache = fresh
        return fresh
    }

    private suspend fun fetchLastTransfer(): List<LastTransferItem> {
        try {
            // 🔹 Padanan: ambil rekening primary dari RekeningLainnyaRepository (sudah punya cache sendiri)
            val rekeningList = rekeningRepository.getRekeningLainnya()
            val primary = rekeningList.firstOrNull { it.isPrimary }
                ?: rekeningList.firstOrNull()
                ?: throw ApiException(null, "Data rekening tidak ditemukan.")

            val body = GetLastTransferRequest(accountNumber = primary.number)

            val response = apiHelper.execute(
                path = "v1/dashboard/getlasttransfer1",
                body = body,
                responseType = LastTransferResponse::class.java,
            )

            if (response.respCode != SUCCESS_CODE) {
                throw ApiException(response.respCode, response.respMessage ?: "Gagal load last transfer")
            }

            return response.data.map { it.toDomain() }
        } catch (e: ApiException) {
            throw e
        } catch (e: Exception) {
            throw ApiException(null, NetworkErrorMapper.toUserMessage(e))
        }
    }

    override fun clear() {
        cache = null
    }
}