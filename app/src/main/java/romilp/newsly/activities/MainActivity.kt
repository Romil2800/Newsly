package romilp.newsly.activities

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import romilp.newsly.adapter.NewsAdapter
import romilp.newsly.databinding.ActivityMainBinding
import romilp.newsly.models.Article
import romilp.newsly.models.News
import com.littlemango.stacklayoutmanager.StackLayoutManager
import romilp.newsly.NewsService
import romilp.newsly.R
import romilp.newsly.Utils.ColorPicker


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var adapter: NewsAdapter
    private var articles = mutableListOf<Article>()
    var pageNum = 1
    private var mInterstitialAd: InterstitialAd? = null
    private var mTAG = "MainActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        adMob()

        adapter = NewsAdapter(this@MainActivity, articles)
        binding.newsList.adapter = adapter

        stackLayoutManager()
    }

    private fun adMob() {
        MobileAds.initialize(this) {}

        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            this,
            "ca-app-pub-3940256099942544/1033173712",
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                  //  Log.d(mTAG, adError?.message)
                    mInterstitialAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    Log.d(mTAG, "Ad was loaded.")
                    mInterstitialAd = interstitialAd
                }


            })

    }

    private fun stackLayoutManager() {
        val layoutManager = StackLayoutManager(StackLayoutManager.ScrollOrientation.BOTTOM_TO_TOP)
        layoutManager.setPagerMode(true)
        layoutManager.setPagerFlingVelocity(1000)

        layoutManager.setItemChangedListener(object : StackLayoutManager.ItemChangedListener {
            override fun onItemChanged(position: Int) {
                binding.cardContainer.setBackgroundColor(Color.parseColor(ColorPicker.getColor()))
                if (layoutManager.getFirstVisibleItemPosition() >= layoutManager.itemCount - 5) {
                    pageNum++
                    getNews()
                }

                if (position % 5 == 0) {
                    if (mInterstitialAd != null) {
                        mInterstitialAd?.show(this@MainActivity)
                    } else {
                        Log.d(mTAG, "The interstitial ad wasn't ready yet.")
                    }
                    adMob()
                }

            }
        })
        binding.newsList.layoutManager = layoutManager
        getNews()
    }

    private fun getNews() {
        Log.d(mTAG, "Request sent for $pageNum")
        val news = NewsService.newsInstance.getHeadlines("in", pageNum)
        news.enqueue(object : Callback<News> {
            override fun onResponse(call: Call<News>, response: Response<News>) {
                val newsData = response.body()
                if (newsData != null) {
                    articles.addAll(newsData.articles)
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<News>, t: Throwable) {
                Log.d(mTAG, "Error", t)
            }

        })
    }
}