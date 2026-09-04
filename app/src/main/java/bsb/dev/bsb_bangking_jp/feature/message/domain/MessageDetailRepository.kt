package bsb.dev.bsb_bangking_jp.feature.message.domain

interface MessageDetailRepository {
    /** Sementara mengembalikan JSON mentah (pretty-printed) -- belum di-parse ke model. */
    suspend fun getMessageDetailRaw(id: Int): String
}