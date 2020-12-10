package com.example.dell.meme


import android.R.layout
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions


class MemeRVAdapter(private val context: Context, private val listener: IMemeRVAdapter) : RecyclerView.Adapter<MemeRVAdapter.MemeViewHolder>() {

    private val allMeme = ArrayList<MemeTable>()

    inner class MemeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView = itemView.findViewById<ImageView>(R.id.imageView)
        val deleteButton = itemView.findViewById<ImageButton>(R.id.delete)
        val share = itemView.findViewById<ImageButton>(R.id.share)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemeViewHolder {
        val viewHolder = MemeViewHolder(LayoutInflater.from(context).inflate(R.layout.meme_list_item, parent, false))
        viewHolder.deleteButton.setOnClickListener {
            listener.onDeleteClick(allMeme[viewHolder.adapterPosition])
        }
        viewHolder.share.setOnClickListener {
            listener.onShareClick(allMeme[viewHolder.adapterPosition])
        }
        return viewHolder

    }

    override fun onBindViewHolder(holder: MemeViewHolder, position: Int) {
        val currentMeme = allMeme[position].text
        Glide.with(this.context)
                .load(currentMeme)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(
                        Glide.with(context)
                                .load(R.drawable.ic_baseline_error_outline_24)).apply( RequestOptions().override(400, 400))
                .into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return allMeme.size
    }

    fun updateList(list: List<MemeTable>) {
        allMeme.clear()
        allMeme.addAll(list)
        notifyDataSetChanged()
    }

}

interface IMemeRVAdapter {
    fun onDeleteClick(memeTable: MemeTable)

    fun onShareClick(memeTable: MemeTable)
}