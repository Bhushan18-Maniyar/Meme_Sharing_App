package com.example.dell.meme

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_meme.*

class MEME : AppCompatActivity() {

    private var currentMemeUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meme)

        loadMeme()

    }

    fun share(view: View) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_TEXT, "Look at this meme $currentMemeUrl")
        intent.type = "text/plain"
        val chooser = Intent.createChooser(intent, "Share MEME via...")
        startActivity(chooser)
    }

    fun next(view: View) {
        loadMeme()
    }

    private fun loadMeme() {
// Instantiate the RequestQueue.
        progressBar.visibility = View.VISIBLE

        val url = "https://meme-api.herokuapp.com/gimme"

// Request a string response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
                Response.Listener { response ->
                    val imageUrl = response.getString("url")
                    currentMemeUrl = imageUrl
                    Glide.with(this).load(currentMemeUrl).listener(object : RequestListener<Drawable> {
                        override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean): Boolean {

                            progressBar.visibility = View.GONE
                            return false

                        }

                        override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean): Boolean {
                            progressBar.visibility = View.GONE
                            return false
                        }
                    })
                            .into(memeImage)
                    progressBar.visibility = View.GONE
                    },
                Response.ErrorListener { Toast.makeText(this, "Error", Toast.LENGTH_LONG).show() })
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)

    }
}
