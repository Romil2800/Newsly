package romilp.newsly.models

data class News(
    val articles: List<Article>,
    val totalResults: Int
)