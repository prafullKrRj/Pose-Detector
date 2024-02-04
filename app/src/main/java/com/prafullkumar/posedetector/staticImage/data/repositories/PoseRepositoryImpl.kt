package com.prafullkumar.posedetector.staticImage.data.repositories

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import com.google.mlkit.vision.pose.PoseDetector
import com.prafullkumar.posedetector.Response
import com.prafullkumar.posedetector.staticImage.domain.models.PoseDetails
import com.prafullkumar.posedetector.staticImage.domain.models.toPoseDetails
import com.prafullkumar.posedetector.staticImage.domain.repositories.PoseRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class PoseRepositoryImpl @Inject constructor(
    private val poseDetector: PoseDetector,
    @ApplicationContext private val context: Context
) : PoseRepository {
    override suspend fun getPoses(bitmap: Bitmap): Flow<Response<PoseDetails>> {
        return callbackFlow {
            trySend(Response.Loading)
            poseDetector.process(bitmap, 0).addOnSuccessListener { pose ->
                trySend(Response.Success(pose.toPoseDetails()))
            }.addOnFailureListener { e ->
                Log.e("DetectedPose", "Pose detection failed", e)
                trySend(Response.Error(e))
            }
            awaitClose()
        }
    }

}