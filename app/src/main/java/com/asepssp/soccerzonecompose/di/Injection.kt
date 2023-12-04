package com.asepssp.soccerzonecompose.di

import com.asepssp.soccerzonecompose.data.PlayerRepository

object Injection {
    fun provideRepository(): PlayerRepository {
        return PlayerRepository.getInstance()
    }
}