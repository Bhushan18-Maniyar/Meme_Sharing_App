package com.example.dell.meme

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.android.synthetic.main.activity_liked__meme_list.*
import kotlinx.android.synthetic.main.activity_meme.*

class LikedMemeList : AppCompatActivity(), IMemeRVAdapter {

    private lateinit var viewModel: MemeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_liked__meme_list)

        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = MemeRVAdapter(this, this)
        recyclerView.adapter = adapter

        viewModel = ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(MemeViewModel::class.java)

        viewModel.allMeme.observe(this, Observer { list ->
            list?.let {
                adapter.updateList(it)
            }
        })

    }

    override fun onDeleteClick(memeTable: MemeTable) {
        viewModel.deleteMeme(memeTable)
    }

    override fun onShareClick(memeTable: MemeTable) {

        Glide.with(this).asBitmap().load(memeTable.text).diskCacheStrategy(DiskCacheStrategy.ALL).into(object : SimpleTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    share(resource)
            }
        })

    }

    private fun share(bitmap: Bitmap){
        val intent = Intent(Intent.ACTION_SEND)
//        intent.putExtra(Intent.EXTRA_TEXT, "Look at this meme $currentMemeUrl")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        val bitmapPath = MediaStore.Images.Media.insertImage(contentResolver, bitmap, "title", null)
        val bitMapUri: Uri = Uri.parse(bitmapPath)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_STREAM, bitMapUri)
        val chooser = Intent.createChooser(intent, "Share MEME via...")
        startActivity(chooser)
    }
}