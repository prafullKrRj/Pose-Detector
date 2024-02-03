package com.prafullkumar.posedetector.staticImage.di


import com.prafullkumar.posedetector.staticImage.data.repositories.PoseRepositoryImpl
import com.prafullkumar.posedetector.staticImage.domain.repositories.PoseRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindPoseRepository(
        poseRepositoryImpl: PoseRepositoryImpl
    ): PoseRepository
}