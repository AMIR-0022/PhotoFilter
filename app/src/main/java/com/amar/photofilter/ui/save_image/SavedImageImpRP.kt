package com.amar.photofilter.ui.save_image

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Environment
import com.amar.photofilter.constants.Constants.FOLDER_NAME
import java.io.File

class SavedImageImpRP(private val context: Context): SavedImageRP {

    override suspend fun loadSavedImages(): List<Pair<File, Bitmap>>? {
        val savedImages = ArrayList<Pair<File, Bitmap>>()

        val path = if (Build.VERSION.SDK_INT > 29) {
            Environment.getExternalStorageDirectory().toString() + "/Pictures/$FOLDER_NAME"
        } else {
            Environment.getExternalStorageDirectory().toString() + "/$FOLDER_NAME"
        }

        val dir = File(path)

        dir.listFiles()?.let { data ->
            data.forEach { file ->
                savedImages.add(Pair(file, getPreviewBitmap(file)))
            }
            return savedImages
        } ?: return null
    }

    private fun getPreviewBitmap(file: File): Bitmap {
        val originalBitmap = BitmapFactory.decodeFile(file.absolutePath)
        val width = 150
        val height = ((originalBitmap.height * width) / originalBitmap.width)
        return Bitmap.createScaledBitmap(originalBitmap, width, height, false)
    }



//    override suspend fun loadSavedImages(): List<Pair<File, Bitmap>>? {
//        val savedImages = ArrayList<Pair<File, Bitmap>>()
//
//        // val dir = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), FOLDER_NAME)
//
//        val path = if (Build.VERSION.SDK_INT > 29) {
//            Environment.getExternalStorageDirectory().toString() + "/Pictures/$FOLDER_NAME"
//        } else {
//            Environment.getExternalStorageDirectory().toString() + "/$FOLDER_NAME"
//        }
//
//        val dir = File(path)
//
//        dir.listFiles()?.let { data ->
//            data.forEach { file ->
//                val previewBitmap = getPreviewBitmap(file)
//                if (previewBitmap != null) {
//                    savedImages.add(Pair(file, previewBitmap))
//                }
//            }
//            return savedImages
//        }
//        return null
//    }
//
//    private fun getPreviewBitmap(file: File): Bitmap? {
//        val options = BitmapFactory.Options()
//        options.inJustDecodeBounds = true
//        BitmapFactory.decodeFile(file.absolutePath, options)
//        val width = options.outWidth
//        val height = options.outHeight
//        val reqWidth = 150
//        val reqHeight = 150
//        options.inSampleSize = calculateInSampleSize(width, height, reqWidth, reqHeight)
//        options.inJustDecodeBounds = false
//        return BitmapFactory.decodeFile(file.absolutePath, options)
//    }
//
//    private fun calculateInSampleSize(width: Int, height: Int, reqWidth: Int, reqHeight: Int): Int {
//        var inSampleSize = 1
//        if (height > reqHeight || width > reqWidth) {
//            val halfHeight: Int = height / 2
//            val halfWidth: Int = width / 2
//            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
//                inSampleSize *= 2
//            }
//        }
//        return inSampleSize
//    }


}