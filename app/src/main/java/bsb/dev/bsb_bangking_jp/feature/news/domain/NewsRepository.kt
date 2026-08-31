package bsb.dev.bsb_bangking_jp.feature.news.domain

data class NewsItem(val pathImage: String) {
    val hasImage: Boolean get() = pathImage.isNotEmpty()
}

data class AllNewsItem(
    val id: String,
    val name: String,
    val date: String,
    val pathImage: String,
)

interface NewsRepository {
    suspend fun getNews(forceRefresh: Boolean = false): List<NewsItem>
}

interface AllNewsRepository {
    suspend fun getAllNews(forceRefresh: Boolean = false): List<AllNewsItem>
}