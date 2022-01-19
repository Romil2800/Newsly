package romilp.newsly

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import romilp.newsly.models.News

//https://newsapi.org/v2/top-headlines?country=in&apiKey=afde40c0c1534c4ba93a9a6fbc664ca7

const val BASE_URL = "https://newsapi.org/"
const val API_KEY = "afde40c0c1534c4ba93a9a6fbc664ca7"

interface NewsInterface {

    @GET("v2/top-headlines?country=in&apiKey=$API_KEY")
    fun getHeadlines(@Query("country") country: String, @Query("page") page: Int): Call<News>
}

object NewsService {
    val newsInstance: NewsInterface

    init {
        val retrofit =
            Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
                .build()

        newsInstance=retrofit.create(NewsInterface::class.java)
    }
}