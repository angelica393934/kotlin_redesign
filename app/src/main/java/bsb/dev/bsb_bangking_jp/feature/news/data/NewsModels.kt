package bsb.dev.bsb_bangking_jp.feature.news.data

import bsb.dev.bsb_bangking_jp.feature.news.domain.NewsItem
import com.google.gson.annotations.SerializedName

data class GetNewsResponse(
    @SerializedName("respCode") val respCode: String? = null,
    @SerializedName("respMessage") val respMessage: String? = null,
    @SerializedName("data") val data: GetNewsData = GetNewsData(),
)

data class GetNewsData(
    @SerializedName("berita") val berita: List<NewsItemDto> = emptyList(),
)

data class NewsItemDto(
    @SerializedName("pathimage") val pathImage: String = "",
)

fun NewsItemDto.toDomain(): NewsItem = NewsItem(pathImage = pathImage)