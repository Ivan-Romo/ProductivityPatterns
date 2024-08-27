package com.example.productivitypatterns.util

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

fun formatTime(seconds: Long): String {
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val secs = seconds % 60
    return String.format("%02d:%02d:%02d", hours, minutes, secs)
}