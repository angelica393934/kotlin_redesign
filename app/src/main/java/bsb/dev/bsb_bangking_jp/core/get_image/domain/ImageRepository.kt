package bsb.dev.bsb_bangking_jp.core.get_image.domain

interface ImageRepository {
    /** null kalau path kosong, gagal fetch, atau body kosong -- JANGAN pernah throw. */
    suspend fun getImage(path: String?): ByteArray?
}