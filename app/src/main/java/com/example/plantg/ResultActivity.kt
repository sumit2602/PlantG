package com.example.plantg

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebSettings
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.mResultTextView
import kotlinx.android.synthetic.main.activity_result.*

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)




            val results =
                MainActivity.mClassifier.recognizeImage(MainActivity.mBitmap).firstOrNull()


           mResultTextView.text = results?.title + "\n Confidence:" + results?.confidence

           if (results!!.title.equals("Tomato_Septoria_leaf_spot")) {

                    browser.loadUrl("https://www.pau.edu/potato/lb_disease.php#:~:text=Late%20blight%20caused%20by%20the,as%2080%25%20in%20epidemic%20years.");
                }

            else {

                    browser.loadUrl("https://www.pau.edu/potato/lb_disease.php#:~:text=Late%20blight%20caused%20by%20the,as%2080%25%20in%20epidemic%20years.");
               browser.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);

            }

        }
    }
