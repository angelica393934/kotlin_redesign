package bsb.dev.bsb_bangking_jp.core.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import bsb.dev.bsb_bangking_jp.core.theme.extendedColors
import androidx.compose.material3.MaterialTheme
import kotlinx.coroutines.delay

enum class ToastType { SUCCESS, ERROR }

data class ToastMessage(
    val message: String,
    val type: ToastType,
    val id: Long = System.currentTimeMillis(),
)

class ToastState {
    var current by mutableStateOf<ToastMessage?>(null)
        private set

    fun showSuccess(message: String) {
        current = ToastMessage(message, ToastType.SUCCESS)
    }

    fun showError(message: String) {
        current = ToastMessage(message, ToastType.ERROR)
    }

    fun dismiss() {
        current = null
    }
}

@Composable
fun rememberToastState(): ToastState = remember { ToastState() }
@Composable
fun ToastHost(
    state: ToastState,
    modifier: Modifier = Modifier,
) {
    val toast = state.current

    AnimatedVisibility(
        visible = toast != null,
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(top = 10.dp, start = 24.dp, end = 24.dp),
        enter = slideInVertically(animationSpec = tween(300)) { -it },
        exit = slideOutVertically(animationSpec = tween(600)) { -it },
    ) {
        toast?.let { current ->
            LaunchedEffect(current.id) {
                delay(2000)
                state.dismiss()
            }

            when (current.type) {
                ToastType.SUCCESS -> SuccessToastContent(current.message)
                ToastType.ERROR -> ErrorToastContent(current.message)
            }
        }
    }
}

@Composable
private fun SuccessToastContent(message: String) {
    Row(
        modifier = Modifier
            .background(
                color = MaterialTheme.extendedColors.success.copy(alpha = 0.12f),
                shape = RoundedCornerShape(10.dp),
            )
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Filled.CheckCircle,
            contentDescription = null,
            tint = MaterialTheme.extendedColors.success,
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = message,
            color = MaterialTheme.extendedColors.success,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(1f),
        )
    }
}

@Composable
private fun ErrorToastContent(message: String) {
    Row(
        modifier = Modifier
            .background(
                color = MaterialTheme.extendedColors.danger,
                shape = RoundedCornerShape(10.dp),
            )
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Filled.Error,
            contentDescription = null,
            tint = Color(0xFFFFBBBB),
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = message,
            color = MaterialTheme.extendedColors.onDanger,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(1f),
        )
    }
}