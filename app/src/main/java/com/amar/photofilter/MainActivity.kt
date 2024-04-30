package com.amar.photofilter

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.amar.photofilter.constants.Constants
import com.amar.photofilter.databinding.ActivityMainBinding
import com.amar.photofilter.ui.edit_image.EditImageActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main)

        setListeners()
    }

    private fun setListeners() {
        binding.btnEditImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivityForResult(intent, Constants.REQUEST_COE_IMAGE_PICK)
        }

        binding.btnSavedImages.setOnClickListener {

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.REQUEST_COE_IMAGE_PICK && resultCode == RESULT_OK){
            data?.data?.let {
                val intent = Intent(this@MainActivity, EditImageActivity::class.java)
                intent.putExtra(Constants.KEY_IMAGE_URI, it.toString())
                startActivity(intent)
            }
        }
    }

}