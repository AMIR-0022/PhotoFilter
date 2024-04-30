package com.amar.photofilter.ui.edit_image

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amar.photofilter.databinding.ListItemFilterBinding

class ImageAdapter(private val callback: (position: Int, imageFilter: ImageFilter) -> Unit):
    RecyclerView.Adapter<ImageAdapter.ViewHolder>(){

    private var imageFilterList: List<ImageFilter> = arrayListOf()
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageAdapter.ViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(context)
        val binding = ListItemFilterBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageAdapter.ViewHolder, position: Int) {
        holder.bind(imageFilterList[position])
    }

    override fun getItemCount(): Int {
        return imageFilterList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<ImageFilter>){
        imageFilterList = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ListItemFilterBinding):
        RecyclerView.ViewHolder(binding.root) {
            fun bind(imageFilter: ImageFilter) {
                binding.ivItemFilter.setImageBitmap(imageFilter.filterPrev)
                binding.tvItemFilter.text = imageFilter.name
            }
        }

}