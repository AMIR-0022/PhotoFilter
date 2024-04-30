package com.amar.photofilter.ui.edit_image

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ImageFactory(private val editImageRP: EditImageRP): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EditImageVM(editImageRP) as T
    }
}