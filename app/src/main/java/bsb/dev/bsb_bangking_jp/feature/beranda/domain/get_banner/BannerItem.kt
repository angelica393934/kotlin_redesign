package bsb.dev.bsb_bangking_jp.feature.beranda.domain.get_banner

/** Padanan GetBannerModel di Flutter. */
data class BannerItem(
    val name: String,
    val description: String,
    val subtitle: String,
    val type: String,
)

/** Padanan extension `BannerX.isBanner`. */
val BannerItem.isBanner: Boolean
    get() = type.uppercase() == "BANNER"