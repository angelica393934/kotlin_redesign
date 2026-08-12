// feature/beranda/domain/ProfileRepository.kt
package bsb.dev.bsb_bangking_jp.feature.beranda.domain

import bsb.dev.bsb_bangking_jp.feature.beranda.data.ProfileData

interface ProfileRepository {
    val hasProfile: Boolean
    val cachedProfile: ProfileData?
    suspend fun getProfile(forceRefresh: Boolean = false): ProfileData
}