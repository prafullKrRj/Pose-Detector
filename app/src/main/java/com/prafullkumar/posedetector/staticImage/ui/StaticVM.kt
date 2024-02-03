package com.prafullkumar.posedetector.staticImage.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prafullkumar.posedetector.Response
import com.prafullkumar.posedetector.staticImage.domain.models.PoseDetails
import com.prafullkumar.posedetector.staticImage.domain.repositories.PoseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StaticVM @Inject constructor(
    private val poseRepository: PoseRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<StaticUiState>(StaticUiState.Initial)
    val uiState = _uiState.asStateFlow()


    fun getPoses(bitmap: Bitmap?) {
        viewModelScope.launch {
            if (bitmap != null) {
                poseRepository.getPoses(bitmap).collect { resp ->
                    when (resp) {
                        is Response.Loading -> {
                            _uiState.value = StaticUiState.Loading
                        }
                        is Response.Success -> {
                            _uiState.value = StaticUiState.Success(resp.data)
                        }
                        is Response.Error -> {
                            _uiState.value = StaticUiState.Error(resp.exception.message ?: "Unknown error")
                        }
                    }
                }
            }
        }
    }
    fun getBitmapFromDrawable(context: Context, drawableId: Int): Bitmap? {
        val drawable = ContextCompat.getDrawable(context, drawableId)
        return if (drawable is BitmapDrawable) {
            drawable.bitmap
        } else {
            null
        }
    }
}
sealed class StaticUiState {
    data object Initial : StaticUiState()
    data object Loading : StaticUiState()
    data class Success(val poseDetails: PoseDetails) : StaticUiState()
    data class Error(val message: String) : StaticUiState()
}
