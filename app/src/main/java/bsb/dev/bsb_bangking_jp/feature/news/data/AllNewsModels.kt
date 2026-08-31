package bsb.dev.bsb_bangking_jp.feature.news.data

import bsb.dev.bsb_bangking_jp.feature.news.domain.AllNewsItem
import com.google.gson.annotations.SerializedName

data class GetAllNewsResponse(
    @SerializedName("respCode") val respCode: String? = null,
    @SerializedName("respMessage") val respMessage: String? = null,
    @SerializedName("data") val data: List<AllNewsItemDto> = emptyList(),
)

data class AllNewsItemDto(
    @SerializedName("id") val id: String = "",
    @SerializedName("name") val name: String = "",
    @SerializedName("date") val date: String = "",
    @SerializedName("pathimage") val pathImage: String = "",
)

fun AllNewsItemDto.toDomain(): AllNewsItem = AllNewsItem(id, name, date, pathImage)