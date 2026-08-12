// feature/beranda/domain/RekeningLainnyaRepository.kt
package bsb.dev.bsb_bangking_jp.feature.beranda.domain

import bsb.dev.bsb_bangking_jp.feature.beranda.data.RekeningItem

interface RekeningLainnyaRepository {
    val hasData: Boolean
    val cachedData: List<RekeningItem>?
    suspend fun getRekeningLainnya(forceRefresh: Boolean = false): List<RekeningItem>
    suspend fun setPrimaryAccount(accountNumber: String): Result<Unit>
}