package com.amar.photofilter.ui.edit_image

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.amar.photofilter.R
import com.amar.photofilter.constants.Constants
import com.amar.photofilter.databinding.ActivityEditImageBinding
import com.amar.photofilter.utils.displayToast

class EditImageActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityEditImageBinding

    private lateinit var viewModel: EditImageVM
    private lateinit var repository: EditImageRP

    private val adapter: ImageAdapter by lazy {
        ImageAdapter {position, imageFilter ->
            Toast.makeText(this, "$position\n${imageFilter.name}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_image)

        repository = EditImageImpRP(this)
        viewModel = ViewModelProvider(this, ImageFactory(repository))[EditImageVM::class.java]

        // set custom toolbar
        setSupportActionBar(binding.mainToolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // display selected image
        prepareImagePreview()

        // observe the data
        setUpObserver()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.opt_save) {
            Toast.makeText(this, "saving in process", Toast.LENGTH_SHORT).show()
        } else if (item.itemId==android.R.id.home){
            onBackPressedDispatcher.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun displayImagePreview() {
        intent.getStringExtra(Constants.KEY_IMAGE_URI)?.let { imageUri ->
            val inputStream = contentResolver.openInputStream(Uri.parse(imageUri))
            val bitmap = BitmapFactory.decodeStream(inputStream)
            binding.ivSelectedImage.setImageBitmap(bitmap)
        }
    }

    private fun prepareImagePreview() {
        intent.getStringExtra(Constants.KEY_IMAGE_URI)?.let { imageUri ->
            viewModel.prepareImagePreview(Uri.parse(imageUri))
        }
    }

    private fun setUpObserver() {
        viewModel.imagePreviewUiState.observe(this) {
            val dataState = it ?: return@observe
            binding.pbPreviewProgress.visibility =
                if (dataState.isLoading) View.VISIBLE else View.GONE

            dataState.bitmap?.let { bitmap ->
                binding.ivSelectedImage.setImageBitmap(bitmap)
                viewModel.loadingImageFilters(bitmap)
            } ?: run {
                dataState.error?.let { error ->
                    displayToast(error)
                }
            }
        }

        viewModel.imageFilterUiState.observe(this) {
            val dataState = it ?: return@observe
            binding.pbFilterProgress.visibility =
                if (dataState.isLoading) View.VISIBLE else View.GONE

            binding.rvFilter.adapter = adapter
            dataState.imageFilter?.let { imageFilterList ->
                adapter.setData(imageFilterList)
            } ?: run {
                dataState.error?.let { error ->
                    displayToast(error)
                }
            }
        }
    }
    
}