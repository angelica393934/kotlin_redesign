package bsb.dev.bsb_bangking_jp.feature.beranda.domain.profile

import bsb.dev.bsb_bangking_jp.feature.beranda.data.profile.ProfileData

interface ProfileRepository {
    val hasProfile: Boolean
    val cachedProfile: ProfileData?
    suspend fun getProfile(forceRefresh: Boolean = false): ProfileData
}