package bsb.dev.bsb_bangking_jp.feature.message.data

import bsb.dev.bsb_bangking_jp.core.network.BaseRespCodeResponse
import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName

data class GetMessageByIdRequest(
    @SerializedName("id") val id: Int,
)

/**
 * Sengaja "data" ditangkap sebagai JsonElement mentah -- struktur asli belum diketahui.
 * Setelah kita lihat hasil log-nya, ganti `data` ini jadi DTO yang sesuai lalu buat
 * mapper toDomain() seperti model lain di project.
 */
data class MessageDetailRawResponse(
    @SerializedName("respCode") override val respCode: String? = null,
    @SerializedName("respMessage") val respMessage: String? = null,
    @SerializedName("data") val data: JsonElement? = null,
) : BaseRespCodeResponse