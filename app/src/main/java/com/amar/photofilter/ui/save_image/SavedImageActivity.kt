package com.amar.photofilter.ui.save_image

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.amar.photofilter.R
import com.amar.photofilter.constants.Constants
import com.amar.photofilter.databinding.ActivitySavedImageBinding
import com.amar.photofilter.ui.filtered_image.FilteredImageActivity
import com.amar.photofilter.utils.displayToast
import java.io.File

class SavedImageActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySavedImageBinding

    private lateinit var viewModel: SavedImageVM
    private lateinit var repository: SavedImageRP

    private val adapter: SavedImageAdapter by lazy {
        SavedImageAdapter { position, pair ->
            onSavedImageClick(position, pair)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_saved_image)

        repository = SavedImageImpRP(this)
        viewModel = ViewModelProvider(this, SavedImageFactory(repository))[SavedImageVM::class.java]

        viewModel.loadSavedImages()

        // set custom toolbar
        setSupportActionBar(binding.imagesToolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // observe the data
        setUpObserver()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home){
            onBackPressedDispatcher.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUpObserver(){
        viewModel.savedImagesUiState.observe(this) {
            val savedImagesDataState = it ?: return@observe
            binding.pbSavedImages.visibility =
                if(savedImagesDataState.isLoading) View.VISIBLE else View.GONE

            binding.rvSavedImages.adapter = adapter
            savedImagesDataState.savedImages?.let { savedImages ->
                adapter.setData(savedImages)
            } ?: run {
                savedImagesDataState.error?.let { error ->
                    displayToast(error)
                }
            }
        }
    }

    private fun onSavedImageClick(position: Int, pair: Pair<File, Bitmap>) {
        val fileUri = FileProvider.getUriForFile(this, "${packageName}.provider", pair.first)
        val intent = Intent(applicationContext, FilteredImageActivity::class.java)
        intent.putExtra(Constants.KEY_FILTERED_IMAGE_URI, fileUri.toString())
        startActivity(intent)
    }

}