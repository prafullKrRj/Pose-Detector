package com.prafullkumar.posedetector.staticImage.ui

import android.graphics.Bitmap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prafullkumar.posedetector.Response
import com.prafullkumar.posedetector.staticImage.domain.models.PoseDetails
import com.prafullkumar.posedetector.staticImage.domain.repositories.PoseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StaticVM @Inject constructor(
    private val poseRepository: PoseRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<StaticUiState>(StaticUiState.Initial)
    val uiState = _uiState.asStateFlow()
    var image by mutableStateOf<Bitmap?>(null)
    fun getPoses(bitmap: Bitmap?) {
        image = bitmap
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
}


sealed class StaticUiState {
    data object Initial : StaticUiState()
    data object Loading : StaticUiState()
    data class Success(val poseDetails: PoseDetails) : StaticUiState()
    data class Error(val message: String) : StaticUiState()
}
