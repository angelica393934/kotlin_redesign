package bsb.dev.bsb_bangking_jp.core.session
/**
 * Registry semua repository yang perlu dibersihkan saat logout/sesi berakhir.
 * Daftarkan repository baru di sini via Koin (get<List<ClearableRepository>>()).
 */
class SessionClearer(private val repositories: List<ClearableRepository>) {
    fun clearAll() {
        repositories.forEach { it.clear() }
    }
}