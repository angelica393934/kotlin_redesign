package bsb.dev.bsb_bangking_jp.feature.news.data

import bsb.dev.bsb_bangking_jp.core.network.token.TokenPhaseTag
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Tag

interface NewsApiService {
    // Endpoint 1 -- ringkasan utk Beranda, tanpa tokenPhase khusus (padanan Flutter, no extra).
    @GET("v1/info/geturlnews")
    suspend fun getNews(
        @HeaderMap headers: Map<String, String>,
    ): Response<GetNewsResponse>

    // Endpoint 2 -- daftar lengkap utk BeritaListPage.
    @GET("v1/info/getallnews")
    suspend fun getAllNews(
        @HeaderMap headers: Map<String, String>,
        @Tag tokenPhase: TokenPhaseTag,
    ): Response<GetAllNewsResponse>
}