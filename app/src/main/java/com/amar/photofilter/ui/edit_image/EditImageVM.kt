package com.amar.photofilter.ui.edit_image

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amar.photofilter.utils.Coroutines

class EditImageVM(private val editImageRP: EditImageRP): ViewModel() {

    // Region:: Prepare Image Preview
    private val imagePreviewDataString = MutableLiveData<ImageProviderDataState>()
    val imagePreviewUiState: LiveData<ImageProviderDataState> get() = imagePreviewDataString
    data class ImageProviderDataState(
        val isLoading: Boolean,
        val bitmap: Bitmap?,
        val error: String?,
    )
    private fun emitImagePreviewUiState(isLoading: Boolean = false, bitmap: Bitmap? = null, error: String? = null){
        val dataState = ImageProviderDataState(isLoading, bitmap, error)
        imagePreviewDataString.postValue(dataState)
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




    // Region:: Save Filtered Image
    private val saveFilteredImageDataState = MutableLiveData<SaveFilteredImageDatState>()
    val saveFilteredImageUiState: LiveData<SaveFilteredImageDatState> get() = saveFilteredImageDataState
    data class SaveFilteredImageDatState(
        val isLoading: Boolean,
        val uri: Uri?,
        val error: String?,
    )
    private fun emitSaveFilteredImageUiState(isLoading: Boolean = false, uri: Uri? = null, error: String? = null){
        val dataState = SaveFilteredImageDatState(isLoading, uri, error)
        saveFilteredImageDataState.postValue(dataState)
    }
    fun saveFilteredImage(filteredBitmap: Bitmap){
        Coroutines.io {
            runCatching {
                emitSaveFilteredImageUiState(isLoading = true)
                editImageRP.saveFilteredImage(filteredBitmap)
            }.onSuccess {
                emitSaveFilteredImageUiState(uri = it)
            }.onFailure {
                emitSaveFilteredImageUiState(error = it.message.toString())
            }
        }
    }

}