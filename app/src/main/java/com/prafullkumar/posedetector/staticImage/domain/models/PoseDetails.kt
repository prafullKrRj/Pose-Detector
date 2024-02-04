package com.prafullkumar.posedetector.staticImage.domain.models

import com.google.mlkit.vision.pose.Pose
import com.google.mlkit.vision.pose.PoseLandmark


data class PoseDetails(
    val list: List<PoseLandmark>,
    val connections: List<Pair<PoseLandmark, PoseLandmark>>
)

fun Pose.toPoseDetails(): PoseDetails {
    val poseLandmark = mutableListOf<PoseLandmark>()
    for (landmarkType in PoseLandmark.NOSE..PoseLandmark.RIGHT_FOOT_INDEX) {
        getPoseLandmark(landmarkType)?.let { poseLandmark.add(it) }
    }
    poseLandmark.sortBy { it.landmarkType }
    val pairs = listOf(
        Pair(poseLandmark[0], poseLandmark[1]),
        Pair(poseLandmark[0], poseLandmark[4]),
        Pair(poseLandmark[1], poseLandmark[2]),
        Pair(poseLandmark[2], poseLandmark[3]),
        Pair(poseLandmark[3], poseLandmark[7]),
        Pair(poseLandmark[4], poseLandmark[5]),
        Pair(poseLandmark[5], poseLandmark[6]),
        Pair(poseLandmark[6], poseLandmark[8]),
        Pair(poseLandmark[10], poseLandmark[9]),
        Pair(poseLandmark[12], poseLandmark[11]),
        Pair(poseLandmark[12], poseLandmark[14]),
        Pair(poseLandmark[11], poseLandmark[13]),
        Pair(poseLandmark[13], poseLandmark[15]),
        Pair(poseLandmark[15], poseLandmark[21]),
        Pair(poseLandmark[15], poseLandmark[19]),
        Pair(poseLandmark[15], poseLandmark[17]),
        Pair(poseLandmark[17], poseLandmark[19]),
        Pair(poseLandmark[14], poseLandmark[16]),
        Pair(poseLandmark[16], poseLandmark[22]),
        Pair(poseLandmark[16], poseLandmark[18]),
        Pair(poseLandmark[16], poseLandmark[20]),
        Pair(poseLandmark[18], poseLandmark[20]),
        Pair(poseLandmark[12], poseLandmark[24]),
        Pair(poseLandmark[11], poseLandmark[23]),
        Pair(poseLandmark[24], poseLandmark[26]),
        Pair(poseLandmark[26], poseLandmark[28]),
        Pair(poseLandmark[28], poseLandmark[32]),
        Pair(poseLandmark[32], poseLandmark[30]),
        Pair(poseLandmark[28], poseLandmark[30]),
        Pair(poseLandmark[23], poseLandmark[25]),
        Pair(poseLandmark[25], poseLandmark[27]),
        Pair(poseLandmark[27], poseLandmark[29]),
        Pair(poseLandmark[27], poseLandmark[31]),
        Pair(poseLandmark[29], poseLandmark[31]),
        Pair(poseLandmark[24], poseLandmark[23]),
    )
    return PoseDetails(poseLandmark, pairs)
}