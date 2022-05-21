package com.example.plantg

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_youtube_web.*

class YoutubeActivityWeb : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtube_web)


        webView.webViewClient = WebViewClient()

        // this will load the url of the website
        webView.loadUrl("https://www.youtube.com/watch?v=SGhIRCsE8U8&list=PLc4C-ydJtfjk3nkpzh2_b8EefhV62zGck/")

        // this will enable the javascript settings
        webView.settings.javaScriptEnabled = true

        // if you want to enable zoom feature
        webView.settings.setSupportZoom(true)

    }
}