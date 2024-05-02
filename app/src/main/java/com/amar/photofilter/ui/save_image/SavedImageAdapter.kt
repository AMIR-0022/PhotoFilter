package com.amar.photofilter.ui.save_image

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amar.photofilter.databinding.ListItemImageBinding
import java.io.File

class SavedImageAdapter(private val callback: (position: Int, pair: Pair<File, Bitmap>) -> Unit) :
    RecyclerView.Adapter<SavedImageAdapter.ViewHolder>(){

    private var imageList: List<Pair<File, Bitmap>> = arrayListOf()
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedImageAdapter.ViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(context)
        val binding = ListItemImageBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SavedImageAdapter.ViewHolder, position: Int) {
        holder.bind(imageList[position])
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<Pair<File, Bitmap>>) {
        imageList = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ListItemImageBinding) :
        RecyclerView.ViewHolder(binding.root){
            fun bind(pair: Pair<File, Bitmap>) {
                binding.itemSavedImage.setImageBitmap(pair.second)

                itemView.apply {
                    setOnClickListener {
                        callback.invoke(adapterPosition, pair)
                    }
                }
            }
    }

}