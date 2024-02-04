package com.prafullkumar.posedetector.staticImage.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.prafullkumar.posedetector.R
import com.prafullkumar.posedetector.staticImage.domain.models.PoseDetails

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

                SuccessScreen(poseDetails = (uiState as StaticUiState.Success).poseDetails)
            }
            is StaticUiState.Error -> {
                Text(text = "Error")
            }
        }
    }
}

@Composable
fun SuccessScreen(poseDetails: PoseDetails) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        poseDetails.connections.forEach { pair ->
            pair.first.position?.let { first ->
                pair.second.position?.let { second ->
                    drawLine(
                        color = Color.Red,
                        strokeWidth = 10f,
                        start = Offset(first.x, first.y),
                        end = Offset(second.x, second.y)
                    )
                }
            }
        }
    }
}