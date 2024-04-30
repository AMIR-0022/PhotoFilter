package com.amar.photofilter.ui.edit_image

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amar.photofilter.utils.Coroutines

class EditImageVM(private val editImageRP: EditImageRP): ViewModel() {

    // Region:: Prepare Image Preview
    private val imageProDataString = MutableLiveData<ImageProviderDataState>()
    val imagePreviewUiState: LiveData<ImageProviderDataState> get() = imageProDataString
    data class ImageProviderDataState(
        val isLoading: Boolean,
        val bitmap: Bitmap?,
        val error: String?,
    )
    private fun emitImagePreviewUiState(isLoading: Boolean = false, bitmap: Bitmap? = null, error: String? = null){
        val dataState = ImageProviderDataState(isLoading, bitmap, error)
        imageProDataString.postValue(dataState)
    }
    fun prepareImagePreview(imageUri: Uri) {
        Coroutines.io {
            runCatching {
                emitImagePreviewUiState(isLoading = true)
                editImageRP.prepareImagePreview(imageUri)
            }.onSuccess {
                if (it != null) {
                    emitImagePreviewUiState(bitmap = it)
                } else {
                    emitImagePreviewUiState(error = "Unable to prepare image preview")
                }
            }.onFailure {
                emitImagePreviewUiState(error = it.message.toString())
            }
        }
    }






    // Region:: Load Image Filter
    private val imageFilterDataState = MutableLiveData<ImageFilterDataState>()
    val imageFilterUiState: LiveData<ImageFilterDataState> get() = imageFilterDataState
    data class ImageFilterDataState(
        val isLoading: Boolean,
        val imageFilter: List<ImageFilter>?,
        val error: String?,
    )
    private fun emitImageFilterUiState(isLoading: Boolean = false, imageFilter: List<ImageFilter>? = null, error: String? = null){
        val dataState = ImageFilterDataState(isLoading, imageFilter, error)
        imageFilterDataState.postValue(dataState)
    }
    private fun getPreviewImage(originalBitmap: Bitmap): Bitmap{
        return runCatching {
            val previewWidth = 150
            val previewHeight = originalBitmap.height * previewWidth/originalBitmap.width
            Bitmap.createScaledBitmap(originalBitmap, previewWidth, previewHeight, false)
        }.getOrDefault(originalBitmap)
    }
    fun loadingImageFilters(originalBitmap: Bitmap) {
        Coroutines.io {
            runCatching {
                emitImageFilterUiState(isLoading = true)
                editImageRP.getImageFilters(getPreviewImage(originalBitmap))
            }.onSuccess {
                emitImageFilterUiState(imageFilter = it)
            }.onFailure {
                emitImageFilterUiState(error = it.message.toString())
            }
        }
    }

}