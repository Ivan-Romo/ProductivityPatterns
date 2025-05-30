package com.productivity.productivitypatterns.domain

import kotlinx.serialization.Serializable

@Serializable
data class Gamification(
    var level: Int = 1,
    var points: Int = 0,
    var challenge: MutableMap<String, Pair<Boolean,String>> =
        mutableMapOf(
            "One day streak" to Pair(false,"Streak"), //not done
            "Add one session" to Pair(false,"Productivity"),
            "Generate a report" to Pair(false,"Productivity"),
            "Add ten sessions" to Pair(false,"Productivity"), //not done
            "Add a session type" to Pair(false,"Productivity"),
            "Add a custom question" to Pair(false,"Productivity"),
            "Add a session manually" to Pair(false,"Productivity"), //not done
            "Try to disable one question" to Pair(false,"Productivity"),
            "Add three custom questions" to Pair(false,"Productivity"), //not done
            "Add a hundred sessions" to Pair(false,"Productivity"), //not done
            "Generate 3 reports" to Pair(false,"Productivity"), //not done
            "Add one session after 10pm" to Pair(false,"Productivity"),
            "Add one session before 7am" to Pair(false,"Productivity"),
            "Add a fully productive session" to Pair(false,"Productivity"), //not done

        )
){
    fun toggleChallengeStatus(key: String) {
        challenge[key]?.let { (flag, description) ->
            challenge[key] = Pair(!flag, description)
        }
    }
}
