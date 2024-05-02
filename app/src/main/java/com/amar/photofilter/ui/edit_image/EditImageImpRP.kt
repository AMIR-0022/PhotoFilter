package com.amar.photofilter.ui.edit_image

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.PointF
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import jp.co.cyberagent.android.gpuimage.GPUImage
import jp.co.cyberagent.android.gpuimage.filter.GPUImageAlphaBlendFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageBrightnessFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageColorBlendFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageColorDodgeBlendFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageColorInvertFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageContrastFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageDarkenBlendFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageGammaFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageGrayscaleFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageHardLightBlendFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageHueBlendFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageHueFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageLuminanceFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageLuminanceThresholdFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageMonochromeFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageNormalBlendFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImagePixelationFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImagePosterizeFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSaturationFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSepiaToneFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSharpenFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSketchFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageVibranceFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageVignetteFilter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class EditImageImpRP(private val context: Context): EditImageRP {

    override suspend fun prepareImagePreview(imageUri: Uri): Bitmap? {
        getInputStreamFromUri(imageUri)?.let {
            val bitmap = BitmapFactory.decodeStream(it)
            val width = context.resources.displayMetrics.widthPixels
            val height = ((bitmap.height) * width/bitmap.width)
            return  Bitmap.createScaledBitmap(bitmap, width, height, false)
        } ?: return null
    }
    private fun getInputStreamFromUri(uri: Uri): InputStream? {
        return context.contentResolver.openInputStream(uri)
    }


    override suspend fun getImageFilters(image: Bitmap): List<ImageFilter> {
        val gpuImage = GPUImage(context).apply {
            setImage(image)
        }
        val imageFilter: ArrayList<ImageFilter> = ArrayList()

        // region : ImageFilter

        // 1. Normal
        GPUImageFilter().also { filter ->
            gpuImage.setFilter(filter)
            imageFilter.add(ImageFilter("Normal", filter, gpuImage.bitmapWithFilterApplied))
        }
        // 2. Contrast
        GPUImageContrastFilter(2.0f).also { filter ->
            gpuImage.setFilter(filter)
            imageFilter.add(ImageFilter("Contrast", filter, gpuImage.bitmapWithFilterApplied))
        }
        // 3. Gamma
        GPUImageGammaFilter(2.0f).also { filter ->
            gpuImage.setFilter(filter)
            imageFilter.add(ImageFilter("Gamma", filter, gpuImage.bitmapWithFilterApplied))
        }
        // 4. Invert
        GPUImageColorInvertFilter().also { filter ->
            gpuImage.setFilter(filter)
            imageFilter.add(ImageFilter("Invert", filter, gpuImage.bitmapWithFilterApplied))
        }
        // 5.Pixelation
        GPUImagePixelationFilter().also { filter ->
            gpuImage.setFilter(filter)
            imageFilter.add(ImageFilter("Pixelation", filter, gpuImage.bitmapWithFilterApplied))
        }
        // 6. Hue
        GPUImageHueFilter(90.0f).also { filter ->
            gpuImage.setFilter(filter)
            imageFilter.add(ImageFilter("Hue", filter, gpuImage.bitmapWithFilterApplied))
        }
        // 7. Bright
        GPUImageBrightnessFilter(1.5f).also { filter ->
            gpuImage.setFilter(filter)
            imageFilter.add(ImageFilter("Bright", filter, gpuImage.bitmapWithFilterApplied))
        }
        // 8. Saturation
        GPUImageSaturationFilter(1.0f).also { filter ->
            gpuImage.setFilter(filter)
            imageFilter.add(ImageFilter("Saturate", filter, gpuImage.bitmapWithFilterApplied))
        }
        // 9. GrayScale
        GPUImageGrayscaleFilter().also { filter ->
            gpuImage.setFilter(filter)
            imageFilter.add(ImageFilter("Grayscale", filter, gpuImage.bitmapWithFilterApplied))
        }
        // 10. Sepia
        GPUImageSepiaToneFilter().also { filter ->
            gpuImage.setFilter(filter)
            imageFilter.add(ImageFilter("Sepia", filter, gpuImage.bitmapWithFilterApplied))
        }
        // 11. Sharpen
        GPUImageSharpenFilter().also { filter ->
            gpuImage.setFilter(filter)
            imageFilter.add(ImageFilter("Sharpen", filter, gpuImage.bitmapWithFilterApplied))
        }
        // 12. Posterize
        GPUImagePosterizeFilter().also { filter ->
            gpuImage.setFilter(filter)
            imageFilter.add(ImageFilter("Posterize", filter, gpuImage.bitmapWithFilterApplied))
        }
        // 13. Sketch
        GPUImageSketchFilter().also { filter ->
            gpuImage.setFilter(filter)
            imageFilter.add(ImageFilter("Sketch", filter, gpuImage.bitmapWithFilterApplied))
        }
        // 14. Monochrome
        GPUImageMonochromeFilter(1.0f, floatArrayOf(0.6f, 0.45f, 0.3f, 1.0f)).also { filter ->
            gpuImage.setFilter(filter)
            imageFilter.add(ImageFilter("Monochrome", filter, gpuImage.bitmapWithFilterApplied))
        }
        // 15. Vignette
        GPUImageVignetteFilter(PointF(0.5f, 0.5f),
            floatArrayOf(0.0f, 0.0f, 0.0f),
            0.3f,
            0.75f).also { filter ->
            gpuImage.setFilter(filter)
            imageFilter.add(ImageFilter("Vignette", filter, gpuImage.bitmapWithFilterApplied))
        }
        // 16. Luminance
        GPUImageLuminanceFilter().also { filter ->
            gpuImage.setFilter(filter)
            imageFilter.add(ImageFilter("Luminance", filter, gpuImage.bitmapWithFilterApplied))
        }
        // 17. Threshold
        GPUImageLuminanceThresholdFilter(0.5f).also { filter ->
            gpuImage.setFilter(filter)
            imageFilter.add(ImageFilter("Threshold", filter, gpuImage.bitmapWithFilterApplied))
        }
        // 18. Normal Blend
        GPUImageNormalBlendFilter().also { filter ->
            gpuImage.setFilter(filter)
            imageFilter.add(ImageFilter("Normal Blend", filter, gpuImage.bitmapWithFilterApplied))
        }
        // 19. Hue Blend
        GPUImageHueBlendFilter().also { filter ->
            gpuImage.setFilter(filter)
            imageFilter.add(ImageFilter("Hue Blend", filter, gpuImage.bitmapWithFilterApplied))
        }
        // 20. Alpha Blend
        GPUImageAlphaBlendFilter().also { filter ->
            gpuImage.setFilter(filter)
            imageFilter.add(ImageFilter("Alpha Blend", filter, gpuImage.bitmapWithFilterApplied))
        }
        // 21. Hard Blend
        GPUImageHardLightBlendFilter().also { filter ->
            gpuImage.setFilter(filter)
            imageFilter.add(ImageFilter("Hard Blend", filter, gpuImage.bitmapWithFilterApplied))
        }
        // 22. Darken Blend
        GPUImageDarkenBlendFilter().also { filter ->
            gpuImage.setFilter(filter)
            imageFilter.add(ImageFilter("Darken Blend", filter, gpuImage.bitmapWithFilterApplied))
        }
        // 23. Color Blend
        GPUImageColorBlendFilter().also { filter ->
            gpuImage.setFilter(filter)
            imageFilter.add(ImageFilter("Color Blend", filter, gpuImage.bitmapWithFilterApplied))
        }
        // 24. Dodge Blend
        GPUImageColorDodgeBlendFilter().also { filter ->
            gpuImage.setFilter(filter)
            imageFilter.add(ImageFilter("Dodge Blend", filter, gpuImage.bitmapWithFilterApplied))
        }
        // 25. Vibrance
        GPUImageVibranceFilter().also { filter ->
            gpuImage.setFilter(filter)
            imageFilter.add(ImageFilter("Vibrance Blend", filter, gpuImage.bitmapWithFilterApplied))
        }

        // --->>> End Region

        return imageFilter
    }

    override suspend fun saveFilteredImage(filteredBitmap: Bitmap): Uri? {
        val fileName = "IMG_${System.currentTimeMillis()}.jpg"
        val folderName = "Photo Filter"
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, "${Environment.DIRECTORY_PICTURES}/$folderName")
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            context.contentResolver?.let { resolver ->
                val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                imageUri?.let { uri ->
                    resolver.openOutputStream(uri)?.use { outputStream ->
                        filteredBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                    }
                    return imageUri
                }
            }
        } else {
            val directory = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), folderName)
            if (!directory.exists()) {
                directory.mkdirs()
            }
            val file = File(directory, fileName)
            try {
                withContext(Dispatchers.IO) {
                    FileOutputStream(file).use { outputStream ->
                        filteredBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                    }
                }
                // Update media store with file path
                MediaScannerConnection.scanFile(context, arrayOf(file.toString()), arrayOf("image/jpeg"), null)
                return Uri.fromFile(file)
            } catch (e: IOException) {
                Log.e("TAG", "saveImage: ${e.message}")
            }
        }
        return null
    }

}