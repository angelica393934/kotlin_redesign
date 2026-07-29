package bsb.dev.bsb_bangking_jp.core.util

/**
 * Wrapper generik untuk hasil pemanggilan API, dipakai Repository -> ViewModel
 * supaya lapisan atas tidak perlu tahu detail Retrofit/HttpException.
 */
sealed class ApiResult<out T> {
    data object Loading : ApiResult<Nothing>()
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(val message: String) : ApiResult<Nothing>()
}
