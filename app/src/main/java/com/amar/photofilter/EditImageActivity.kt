package com.amar.photofilter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.amar.photofilter.constants.Constants
import com.amar.photofilter.databinding.ActivityEditImageBinding

class EditImageActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityEditImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_image)

        // set custom toolbar
        setSupportActionBar(binding.mainToolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // display selected image
        displayImagePreview()

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
    
}