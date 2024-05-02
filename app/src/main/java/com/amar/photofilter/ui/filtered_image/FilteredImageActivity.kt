package com.amar.photofilter.ui.filtered_image

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.amar.photofilter.R
import com.amar.photofilter.constants.Constants
import com.amar.photofilter.databinding.ActivityFilteredImageBinding

class FilteredImageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFilteredImageBinding

    private lateinit var fileUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_filtered_image)

        displayFilteredImage()
        setListeners()

    }

    private fun displayFilteredImage() {
        intent.getStringExtra(Constants.KEY_FILTERED_IMAGE_URI)?.let {
            fileUri = Uri.parse(it)
            binding.imgFilteredBitmap.setImageURI(fileUri)
        }
    }

    private fun setListeners() {
        binding.btnShareImage.setOnClickListener{
            with(Intent(Intent.ACTION_SEND)) {
                putExtra(Intent.EXTRA_STREAM, fileUri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                type = "image/*"
                startActivity(this)
            }
        }
    }

}