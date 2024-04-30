package com.amar.photofilter.ui.edit_image

import android.graphics.Bitmap
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter

data class ImageFilter(
    val name: String,
    val filter: GPUImageFilter,
    val filterPrev: Bitmap,
)