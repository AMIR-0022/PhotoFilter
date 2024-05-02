package com.amar.photofilter.ui.save_image

import android.graphics.Bitmap
import java.io.File

interface SavedImageRP {

    suspend fun loadSavedImages(): List<Pair<File, Bitmap>>?

}