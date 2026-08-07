package bsb.dev.bsb_bangking_jp.feature.init.data

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.HeaderMap
import retrofit2.http.POST

interface InitApiService {
    @POST("v1/init")
    suspend fun initDevice(
        @HeaderMap headers: Map<String, String>,
        @Body body: InitDeviceRequest,
    ): Response<InitDeviceResponse>
}