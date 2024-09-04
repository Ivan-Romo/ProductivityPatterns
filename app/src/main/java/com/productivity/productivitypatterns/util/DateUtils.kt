package com.productivity.productivitypatterns.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun formatTime(seconds: Long): String {
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val secs = seconds % 60
    return String.format("%02d:%02d:%02d", hours, minutes, secs)
}

fun LocalDateTime.formatToDayMonth(): String {
    val formatter = DateTimeFormatter.ofPattern("dd/MM")
    return this.format(formatter)
}