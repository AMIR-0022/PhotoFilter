package com.amar.photofilter.ui.edit_image

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.amar.photofilter.R
import com.amar.photofilter.databinding.ListItemFilterBinding

class EditImageAdapter(private val callback: (position: Int, imageFilter: ImageFilter) -> Unit):
    RecyclerView.Adapter<EditImageAdapter.ViewHolder>(){

    private var imageFilterList: List<ImageFilter> = arrayListOf()
    private lateinit var context: Context

    private var currentPosition = 0
    private var previousPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditImageAdapter.ViewHolder {
        context = parent.context
        val inflater = LayoutInflater.from(context)
        val binding = ListItemFilterBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EditImageAdapter.ViewHolder, position: Int) {
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
                binding.tvItemFilter.setTextColor(ContextCompat.getColor(context,
                    if (currentPosition==adapterPosition)
                        R.color.color_primaryDark
                    else
                        R.color.color_primaryText
                ))

                itemView.apply {
                    setOnClickListener {
                        if (adapterPosition != currentPosition) {
                            callback.invoke(adapterPosition, imageFilter)
                            previousPosition = currentPosition
                            currentPosition = adapterPosition
                            
                            notifyItemChanged(currentPosition, Unit)
                            notifyItemChanged(previousPosition, Unit)
                        }
                    }
                }
            }
        }

}