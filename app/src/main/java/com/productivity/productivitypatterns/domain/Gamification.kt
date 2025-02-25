package com.productivity.productivitypatterns.domain

import kotlinx.serialization.Serializable

@Serializable
data class Gamification(
    var points: Int = 0,
    var challenge: MutableMap<String, Pair<Boolean,String>> =
        mutableMapOf(
            "One day streak" to Pair(true,"Streak"),
            "Add one session" to Pair(false,"Productivity")
        )
){
    fun toggleChallengeStatus(key: String) {
        challenge[key]?.let { (flag, description) ->
            challenge[key] = Pair(!flag, description)
        }
    }
}
