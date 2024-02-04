package com.prafullkumar.posedetector.staticImage.domain.repositories

import android.graphics.Bitmap
import android.net.Uri
import com.google.mlkit.vision.pose.Pose
import com.prafullkumar.posedetector.Response
import com.prafullkumar.posedetector.staticImage.domain.models.PoseDetails
import kotlinx.coroutines.flow.Flow

interface PoseRepository {
    suspend fun getPoses(bitmap: Bitmap): Flow<Response<PoseDetails>>
}