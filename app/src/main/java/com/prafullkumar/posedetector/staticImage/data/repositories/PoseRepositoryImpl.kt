package com.prafullkumar.posedetector.staticImage.data.repositories

import android.graphics.Bitmap
import android.util.Log
import com.google.mlkit.vision.pose.PoseDetector
import com.prafullkumar.posedetector.Response
import com.prafullkumar.posedetector.staticImage.domain.models.PoseDetails
import com.prafullkumar.posedetector.staticImage.domain.models.toPoseDetails
import com.prafullkumar.posedetector.staticImage.domain.repositories.PoseRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class PoseRepositoryImpl @Inject constructor(
    private val poseDetector: PoseDetector
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