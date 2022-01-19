package romilp.newsly.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import romilp.newsly.R
import romilp.newsly.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    lateinit var binding: ActivityDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)

        binding.apply {
            val url = intent.getStringExtra("URL")
            if (url != null) {
                detailedWebView.settings.javaScriptEnabled = true
                detailedWebView.webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView?, url: String?) {
                        super.onPageFinished(view, url)
                        progressBar.visibility = View.GONE
                        detailedWebView.visibility = View.VISIBLE
                    }
                }
                detailedWebView.loadUrl(url)
            }
        }

    }
}