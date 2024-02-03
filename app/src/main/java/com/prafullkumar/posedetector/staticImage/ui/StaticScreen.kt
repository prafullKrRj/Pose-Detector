package com.prafullkumar.posedetector.staticImage.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.prafullkumar.posedetector.R

@Composable
fun StaticScreen(viewModel: StaticVM = viewModel()) {
    val context = LocalContext.current
    val bitmap = viewModel.getBitmapFromDrawable(context, R.drawable.dummypose)
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { viewModel.getPoses(bitmap) }) {
            Text(text = "Get Poses")
        }

        val uiState by viewModel.uiState.collectAsState()
        when (uiState) {
            is StaticUiState.Initial -> {
                Text(text = "Initial")
            }
            is StaticUiState.Loading -> {
                CircularProgressIndicator()
            }
            is StaticUiState.Success -> {
                Text(text = "${(uiState as StaticUiState.Success).poseDetails.leftAnkle?.position?.x}")
            }
            is StaticUiState.Error -> {
                Text(text = "Error")
            }
        }
    }
}