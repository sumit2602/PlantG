package com.example.plantg

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.AsyncTask
import android.os.Bundle
import android.provider.MediaStore
import android.view.Gravity
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.io.IOException
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var mClassifier: Classifier
    private lateinit var mBitmap: Bitmap

    private val mCameraRequestCode = 0
    private val mGalleryRequestCode = 2

    private val mInputSize = 224
    private val mModelPath = "model.tflite"
    private val mLabelPath = "labels.txt"
    private val mSamplePath = "wind.json"

    //for showing weather info

    val CITY: String = "delhi ,in"
    val API: String = "06c921750b9a82d8f5d1294e1586276f" // Use API key

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        mClassifier = Classifier(assets, mModelPath, mLabelPath, mInputSize)

//        resources.assets.open(mSamplePath).use {
//            mBitmap = BitmapFactory.decodeStream(it)
//            mBitmap = Bitmap.createScaledBitmap(mBitmap, mInputSize, mInputSize, true)
//            mPhotoImageView.setImageBitmap(mBitmap)
//        }

        webview.setOnClickListener {
            val i = Intent(applicationContext, YoutubeActivityWeb::class.java)
            startActivity(i)
        }

        temp1.setOnClickListener {
            val i  = Intent(this, tempdetail::class.java)
            startActivity(i)
        }
        mCameraButton.setOnClickListener {
            val callCameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(callCameraIntent, mCameraRequestCode)
        }
        home.setOnClickListener {
            val i = Intent(applicationContext, MainActivity::class.java)
            startActivity(i)
        }
        user.setOnClickListener {
            val i  = Intent(this , ProfileActivity::class.java)
            startActivity(i)
        }
        backBtn.setOnClickListener {
            val  i = Intent(this,  ProfileActivity::class.java)
            startActivity(i)
        }
        mGalleryButton.setOnClickListener {
            val callGalleryIntent = Intent(Intent.ACTION_PICK)
            callGalleryIntent.type = "image/*"
            startActivityForResult(callGalleryIntent, mGalleryRequestCode)
        }
        mDetectButton.setOnClickListener {
            val results = mClassifier.recognizeImage(mBitmap).firstOrNull()
            mResultTextView.text = results?.title + "\n Confidence:" + results?.confidence

        }
        weatherTask().execute()
        // BottomNavigation()
    }

    inner class weatherTask() : AsyncTask<String, Void, String>() {
        override fun onPreExecute() {
            super.onPreExecute()
            /* Showing the ProgressBar, Making the main design GONE */
//            findViewById<ProgressBar>(R.id.loader).visibility = View.VISIBLE
//            findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.GONE
//            findViewById<TextView>(R.id.errorText).visibility = View.GONE
        }

        override fun doInBackground(vararg params: String?): String? {
            var response: String?
            try {
                response =
                    URL("https://api.openweathermap.org/data/2.5/weather?q=$CITY&units=metric&appid=$API").readText(
                        Charsets.UTF_8
                    )
            } catch (e: Exception) {
                response = null
            }
            return response
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                /* Extracting JSON returns from the API */
                val jsonObj = JSONObject(result)
                val main = jsonObj.getJSONObject("main")
                val sys = jsonObj.getJSONObject("sys")
                val wind = jsonObj.getJSONObject("wind")
                val weather = jsonObj.getJSONArray("weather").getJSONObject(0)

                val updatedAt: Long = jsonObj.getLong("dt")
                val updatedAtText =
                    "Updated at: " + SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(
                        Date(updatedAt * 1000)
                    )
                val temp = main.getString("temp") + "°C"
                val tempMin = "Min Temp: " + main.getString("temp_min") + "°C"
                val tempMax = "Max Temp: " + main.getString("temp_max") + "°C"
                val pressure = main.getString("pressure")
                val humidity = main.getString("humidity")

                val sunrise: Long = sys.getLong("sunrise")
                val sunset: Long = sys.getLong("sunset")
                val windSpeed = wind.getString("speed")
                val weatherDescription = weather.getString("description")

                val address = jsonObj.getString("name") + ", " + sys.getString("country")

                /* Populating extracted data into our views */

                findViewById<TextView>(R.id.temp).text = temp
//                findViewById<TextView>(R.id.temp_min).text = tempMin
//                findViewById<TextView>(R.id.temp_max).text = tempMax
//                findViewById<TextView>(R.id.sunrise).text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(
//                    Date(sunrise*1000)
//                )
//                findViewById<TextView>(R.id.sunset).text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(
//                    Date(sunset*1000)
//                )
//                findViewById<TextView>(R.id.wind).text = windSpeed
//                findViewById<TextView>(R.id.pressure).text = pressure
//                findViewById<TextView>(R.id.humidity).text = humidity
//
//                /* Views populated, Hiding the loader, Showing the main design */
//                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
//                findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.VISIBLE
//
            } catch (e: Exception) {
                // findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                //findViewById<TextView>(R.id.errorText).visibility = View.VISIBLE
            }

        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == mCameraRequestCode){
            //Considérons le cas de la caméra annulée
            if(resultCode == Activity.RESULT_OK && data != null) {
                mBitmap = data.extras!!.get("data") as Bitmap
                mBitmap = scaleImage(mBitmap)
                val toast = Toast.makeText(this, ("Image crop to: w= ${mBitmap.width} h= ${mBitmap.height}"), Toast.LENGTH_LONG)
                toast.setGravity(Gravity.BOTTOM, 0, 20)
                toast.show()
                mPhotoImageView.setImageBitmap(mBitmap)
                mResultTextView.text= "Your photo image set now."
            } else {
                Toast.makeText(this, "Camera cancel..", Toast.LENGTH_LONG).show()
            }
        } else if(requestCode == mGalleryRequestCode) {
            if (data != null) {
                val uri = data.data

                try {
                    mBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                println("Success!!!")
                mBitmap = scaleImage(mBitmap)
                mPhotoImageView.setImageBitmap(mBitmap)

            }
        } else {
            Toast.makeText(this, "Unrecognized request code", Toast.LENGTH_LONG).show()

        }
    }


    fun scaleImage(bitmap: Bitmap?): Bitmap {
        val orignalWidth = bitmap!!.width
        val originalHeight = bitmap.height
        val scaleWidth = mInputSize.toFloat() / orignalWidth
        val scaleHeight = mInputSize.toFloat() / originalHeight
        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)
        return Bitmap.createBitmap(bitmap, 0, 0, orignalWidth, originalHeight, matrix, true)
    }


}

