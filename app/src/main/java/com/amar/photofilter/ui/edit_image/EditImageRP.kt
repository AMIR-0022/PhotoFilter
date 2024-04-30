package com.amar.photofilter.ui.edit_image

import android.graphics.Bitmap
import android.net.Uri

interface EditImageRP {

    suspend fun prepareImagePreview(imageUri: Uri): Bitmap?

    suspend fun getImageFilters(image: Bitmap): List<ImageFilter>

}