package com.productivity.productivitypatterns.domain

import kotlinx.serialization.Serializable

@Serializable
data class Gamification(
    var points: Int = 0,
    var challenge: MutableMap<String, Boolean> = mutableMapOf("" to false))