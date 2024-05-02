package com.amar.photofilter.ui.save_image

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amar.photofilter.utils.Coroutines
import java.io.File

class SavedImageVM(private val savedImageRP: SavedImageRP): ViewModel() {

    private val savedImagesDataState= MutableLiveData<SavedImagesDataState>()
    val savedImagesUiState: LiveData<SavedImagesDataState> = savedImagesDataState
    data class SavedImagesDataState(
        val isLoading: Boolean,
        val savedImages: List<Pair<File, Bitmap>>?,
        val error: String?,
    )

    private fun emitSavedImagesUiState(isLoading: Boolean = false, savedImages: List<Pair<File, Bitmap>>? = null, error: String? = null){
        val dataState = SavedImagesDataState(isLoading, savedImages, error)
        savedImagesDataState.postValue(dataState)
    }

    fun loadSavedImages() {
        Coroutines.io {
            runCatching {
                emitSavedImagesUiState(isLoading = true)
                savedImageRP.loadSavedImages()
            }.onSuccess { savedImages ->
                if (savedImages.isNullOrEmpty()){
                    emitSavedImagesUiState(error = "No Images Found")
                } else {
                    emitSavedImagesUiState(savedImages = savedImages)
                }
            }.onFailure {
                emitSavedImagesUiState(error = it.toString())
            }
        }
    }

}