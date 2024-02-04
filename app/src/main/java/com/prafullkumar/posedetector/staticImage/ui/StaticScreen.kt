package com.prafullkumar.posedetector.staticImage.ui

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.prafullkumar.posedetector.R
import com.prafullkumar.posedetector.staticImage.domain.models.PoseDetails
import com.prafullkumar.posedetector.staticImage.domain.utils.createUri
import com.prafullkumar.posedetector.staticImage.domain.utils.uriToBitmap

@Composable
fun StaticScreen(viewModel: StaticVM = viewModel()) {
    val context = LocalContext.current
    val photoUri = createUri(context)
    val uiState by viewModel.uiState.collectAsState()
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = {
            if (it) {
                Log.d("StaticScreen", "onResult: $photoUri")
                viewModel.getPoses(uriToBitmap(photoUri, context))
            }
            else {
                Toast.makeText(context, "error", Toast.LENGTH_SHORT).show()
            }
        }
    )
    val getImages = rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()) { uri ->
        uri?.let {
            Log.d("StaticScreen", "onActivityResult: $it")
            viewModel.getPoses(uriToBitmap(it, context))
        }
    }
    val perMissionLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) {
        if (it) {
            cameraLauncher.launch(photoUri)
        }
        else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }
    Scaffold(
        floatingActionButton = {
            Row(modifier = Modifier, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                FloatingActionButton(
                    onClick = {
                        if (checkPermission(context)) {
                            cameraLauncher.launch(photoUri)
                        }
                        else {
                            perMissionLauncher.launch(android.Manifest.permission.CAMERA)
                        }
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_camera_24),
                        contentDescription = "Camera"
                    )
                }
                FloatingActionButton(
                    onClick = {
                        getImages.launch(
                            PickVisualMediaRequest(mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_browse_gallery_24),
                        contentDescription = "Gallery"
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (uiState) {
                is StaticUiState.Initial -> {
                    Text(text = "Add Image", fontWeight = FontWeight.SemiBold, fontSize = 20.sp)
                }
                is StaticUiState.Loading -> {
                    LoadingScreen()
                }
                is StaticUiState.Success -> {
                    SuccessScreen(viewModel, poseDetails = (uiState as StaticUiState.Success).poseDetails)
                }
                is StaticUiState.Error -> {
                    Text(text = "Error in loading poses: ${(uiState as StaticUiState.Error).message}")
                }
            }
        }
    }
}
@Composable
fun SuccessScreen(viewModel: StaticVM, poseDetails: PoseDetails) {
    if (viewModel.image != null) {
        val aspectRatio = getAspectRatio(viewModel.image!!)
        Box(modifier = Modifier.fillMaxWidth().aspectRatio(aspectRatio)) {
            Image(
                bitmap = viewModel.image!!.asImageBitmap(),
                contentDescription = "Captured Image",
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillWidth
            )
            Canvas(modifier = Modifier.fillMaxWidth().aspectRatio(aspectRatio), onDraw = {
                poseDetails.connections.forEach { pair ->
                    pair.first.position.let { first ->
                        pair.second.position.let { second ->
                            drawLine(
                                color = Color.Red,
                                strokeWidth = 10f,
                                start = Offset(first.x, first.y),
                                end = Offset(second.x, second.y)
                            )
                        }
                    }
                }
            })
        }
    }
}
fun getAspectRatio(bitmap: Bitmap): Float {
    return bitmap.width.toFloat() / bitmap.height.toFloat()
}
@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}
fun checkPermission(context: Context): Boolean {
    val permissionChecked = ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
    return permissionChecked == PackageManager.PERMISSION_GRANTED
}