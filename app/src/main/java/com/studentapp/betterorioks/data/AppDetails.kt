package com.studentapp.betterorioks.data

import java.time.LocalDateTime

object AppDetails {
    const val appName = "BetterOrioks"
    const val version = "1.12"
    const val BIG_WINDOW_TIME = 30L
    val SAMPLE_TIME = listOf(LocalDateTime.parse("0001-01-01T12:00:00"))
    val changeable = listOf(3)
    val debugUsers = AdminIds.ids + listOf("8211790")
}