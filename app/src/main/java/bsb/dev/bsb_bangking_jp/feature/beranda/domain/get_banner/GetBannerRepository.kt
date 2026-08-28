package bsb.dev.bsb_bangking_jp.feature.beranda.domain.get_banner

interface GetBannerRepository {
    val hasData: Boolean
    val cachedBanners: List<BannerItem>?
    suspend fun getBanner(forceRefresh: Boolean = false): List<BannerItem>
}