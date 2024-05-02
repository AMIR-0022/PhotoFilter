package com.amar.photofilter.ui.save_image

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SavedImageFactory(private val savedImageRP: SavedImageRP): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SavedImageVM(savedImageRP) as T
    }
}