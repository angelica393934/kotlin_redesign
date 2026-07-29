package bsb.dev.bsb_bangking_jp.feature.Navbar.viewmodel

import androidx.lifecycle.ViewModel
import bsb.dev.bsb_bangking_jp.core.session.SessionManager
import bsb.dev.bsb_bangking_jp.core.session.UserProfile

/**
 * Contoh ViewModel Dashboard: ambil profil user yang sudah tersimpan
 * di SessionManager sejak login, tanpa perlu hit API lagi.
 *
 * Cara pakai di Composable (mis. BerandaPage):
 *   val viewModel: DashboardViewModel = viewModel()
 *   val profile = viewModel.profile
 *   Text(text = "Halo, ${profile?.firstName}")
 */
class DashboardViewModel : ViewModel() {

    val profile: UserProfile? = SessionManager.getUserProfile()

    fun logout() {
        SessionManager.clearSession()
        // TODO: dari Composable, setelah manggil logout() ini,
        // navController.navigate("portal") { popUpTo(0) }
    }
}
