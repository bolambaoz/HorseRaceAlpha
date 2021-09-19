package com.horserace.ui.videostream

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebViewClient
import com.horserace.R
import com.horserace.utils.getIpAddres
import kotlinx.android.synthetic.main.activity_video_stream.*
import java.io.IOException

class VideoStreamActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_stream)

        val intent = intent
        val link = intent.getStringExtra("link")
        webview.webViewClient = WebViewClient()
        getStreamChannel("1014" ,getIpAddres(),this)

    }

    override fun onDestroy() {
        super.onDestroy()
        webview.removeAllViews();
        webview.destroy()
        webview.clearCache(true);
        webview.clearHistory()
    }

    private fun getStreamChannel(channel: String?, userIp: String?, context: Context){
        //show the loading screen in main activity
//        val baseURL = "https://3webasketball.com/ninety-six-group-api/public/latest/stream/${channel}/${userIp}/geth5link"
//        val request = Request.Builder().url(baseURL).build()
//        val client = OkHttpClient()
//
//        client.newCall(request).enqueue(object : Callback {
//
//            override fun onResponse(call: Call, response: Response) {
//                val body = response.body?.string()
//
//                val gson = GsonBuilder().create()
//
//                val video = gson.fromJson(body, videoModel::class.java)
//                val link = video.H5LINKROW
//                runOnUiThread{
//                    webview.apply {
//                        link?.let { loadUrl(it) }
//                        settings.javaScriptEnabled = true
//                    }
//                }
//
//
//            }
//
//            override fun onFailure(call: Call, e: IOException) {
//                println("Failed to request")
//            }
//
//        })
    }
}