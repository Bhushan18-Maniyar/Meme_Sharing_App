package com.example.dell.meme

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.activity_meme.*
import java.util.jar.Manifest

class MEME : AppCompatActivity() {

    private var currentMemeUrl: String? = null
    private lateinit var mInterstitialAd: InterstitialAd
    var isAdShown: Boolean = false
    var count: Int = 0
    var likedMeme = false


    private lateinit var viewModel: MemeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meme)


//        Permission


        MobileAds.initialize(this@MEME)
        mInterstitialAd = InterstitialAd(this)
        
        mInterstitialAd.adUnitId = "ca-app-pub-3940256099942544/1033173712"
        mInterstitialAd.loadAd(AdRequest.Builder().build())

        count = (1..10).random()

        list.setOnClickListener {
            startActivity(Intent(this, LikedMemeList::class.java))
//            finish()
        }

        viewModel = ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(MemeViewModel::class.java)

        loadMeme()

        like.setOnClickListener {
            likedMeme = if (!likedMeme) {
                if (currentMemeUrl?.isNotEmpty() == true) {
                    viewModel.insert(MemeTable(currentMemeUrl!!))
                }
                like.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_baseline_thumb_up_24))
                true
            } else {
                if (currentMemeUrl?.isNotEmpty() == true) {
                    viewModel.deleteMeme(MemeTable(currentMemeUrl!!))
                }
                like.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_outline_thumb_up_24))
                false
            }
        }

    }

    fun share(resource: Bitmap) {
        val intent = Intent(Intent.ACTION_SEND)
//        intent.putExtra(Intent.EXTRA_TEXT, "Look at this meme $currentMemeUrl")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        val bitmapPath = MediaStore.Images.Media.insertImage(contentResolver, resource, "title", null)
        val bitMapUri: Uri = Uri.parse(bitmapPath)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_STREAM, bitMapUri)
        val chooser = Intent.createChooser(intent, "Share MEME via...")
        startActivity(chooser)
    }

    fun next(view: View) {

        likedMeme = false
        like.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_outline_thumb_up_24))
        Toast.makeText(this, "NEXT", Toast.LENGTH_SHORT).show()
        if (!isAdShown && count == 0) {
            mInterstitialAd.show()
            isAdShown = true
            count = -1
            loadMeme()
        } else {
            if (count == -1) {
                loadMeme()
            } else {
                count--
                loadMeme()
            }
        }
    }

    private fun loadMeme() {
// Instantiate the RequestQueue.
        progressBar.visibility = View.VISIBLE

        val url = "https://meme-api.herokuapp.com/gimme"

// Request a string response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
                { response ->
                    val imageUrl = response.getString("url")

                    currentMemeUrl = imageUrl

                    Glide.with(this).asBitmap().load(currentMemeUrl).diskCacheStrategy(DiskCacheStrategy.ALL).into(object : SimpleTarget<Bitmap>() {
                        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                            memeImage.setImageBitmap(resource)
                            progressBar.visibility = View.GONE
                            share.setOnClickListener {
                                share(resource)
                            }
                        }
                    })
                },
                { Toast.makeText(this, "JSON Error", Toast.LENGTH_LONG).show() })
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)

    }
}
/*
//Glide.with(this).load(currentMemeUrl).listener(object : RequestListener<Drawable> {
//                        override fun onResourceReady(
//                                resource: Drawable?,
//                                model: Any?,
//                                target: Target<Drawable>?,
//                                dataSource: DataSource?,
//                                isFirstResource: Boolean): Boolean {
//
//                            progressBar.visibility = View.GONE
//                            return false
//
//                        }
//
//                        override fun onLoadFailed(
//                                e: GlideException?,
//                                model: Any?,
//                                target: Target<Drawable>?,
//                                isFirstResource: Boolean): Boolean {
//                            progressBar.visibility = View.GONE
//                            return false
//                        }
//                    }).into(memeImage)*/
