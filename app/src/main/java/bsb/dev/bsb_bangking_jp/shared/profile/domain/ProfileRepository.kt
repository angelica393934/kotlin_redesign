package bsb.dev.bsb_bangking_jp.shared.profile.domain

import bsb.dev.bsb_bangking_jp.shared.profile.data.ProfileData

interface ProfileRepository {
    val hasProfile: Boolean
    val cachedProfile: ProfileData?
    suspend fun getProfile(forceRefresh: Boolean = false): ProfileData
}