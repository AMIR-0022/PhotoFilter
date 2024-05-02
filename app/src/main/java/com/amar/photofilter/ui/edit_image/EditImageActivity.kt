package com.amar.photofilter.ui.edit_image

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.amar.photofilter.R
import com.amar.photofilter.constants.Constants
import com.amar.photofilter.databinding.ActivityEditImageBinding
import com.amar.photofilter.ui.filtered_image.FilteredImageActivity
import com.amar.photofilter.utils.displayToast
import jp.co.cyberagent.android.gpuimage.GPUImage

class EditImageActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityEditImageBinding

    private lateinit var viewModel: EditImageVM
    private lateinit var repository: EditImageRP

    private lateinit var gpuImage: GPUImage
    private lateinit var originalBitmap: Bitmap
    private var filteredBitmap = MutableLiveData<Bitmap>()

    private val adapter: EditImageAdapter by lazy {
        EditImageAdapter { position, imageFilter ->
            onFilterImageClick(position, imageFilter)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_image)

        repository = EditImageImpRP(this)
        viewModel = ViewModelProvider(this, EditImageFactory(repository))[EditImageVM::class.java]

        // set custom toolbar
        setSupportActionBar(binding.mainToolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // display selected image
        prepareImagePreview()

        // observe the data
        setUpObserver()

        // show original image on long press
        binding.ivSelectedImage.setOnLongClickListener {
            binding.ivSelectedImage.setImageBitmap(originalBitmap)
            return@setOnLongClickListener false
        }
        binding.ivSelectedImage.setOnClickListener{
            binding.ivSelectedImage.setImageBitmap(filteredBitmap.value)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.opt_save) {
            filteredBitmap.value?.let { bitmap ->  
                viewModel.saveFilteredImage(bitmap)
            }
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
        gpuImage = GPUImage(applicationContext)
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
                // for original image preview
                originalBitmap = bitmap
                filteredBitmap.value = bitmap
                with(originalBitmap) {
                    gpuImage.setImage(this)
                    viewModel.loadingImageFilters(this)
                }
            } ?: run {
                dataState.error?.let { error ->
                    displayToast(error)
                }
            }

            filteredBitmap.observe(this) {
                binding.ivSelectedImage.setImageBitmap(it)
            }

            viewModel.saveFilteredImageUiState.observe(this) {
                val saveFilteredImageDatState = it ?: return@observe

                saveFilteredImageDatState.uri?.let { savedImageUri ->
                    val intent = Intent(applicationContext, FilteredImageActivity::class.java)
                    intent.putExtra(Constants.KEY_FILTERED_IMAGE_URI, savedImageUri.toString())
                    startActivity(intent)
                } ?: run {
                    dataState.error?.let { error ->
                        displayToast(error)
                    }
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

    private fun onFilterImageClick(position: Int, imageFilter: ImageFilter){
        with(imageFilter) {
            with(gpuImage) {
                setFilter(filter)
                filteredBitmap.value = bitmapWithFilterApplied
            }
        }
    }
    
}