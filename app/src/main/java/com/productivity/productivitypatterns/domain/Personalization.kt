package com.productivity.productivitypatterns.domain

import com.productivity.productivitypatterns.util.listQuestions
import kotlinx.serialization.Serializable

@Serializable
data class Personalization(
    val customQuestions: MutableList<Question>,
    val customAnswers: MutableMap<String,MutableList<String>>,
    val enabledQuestions: MutableMap<String,Boolean>,
    val activityTypes: MutableList<String>,
    val mode : Boolean
){
    companion object {
        fun default(): Personalization {
            return Personalization(
                customQuestions = mutableListOf(),
                customAnswers = mutableMapOf(),
                enabledQuestions = (listQuestions.associate { it.id to true }).toMutableMap(),
                activityTypes = mutableListOf(),
                mode = false
            )
        }
    }
}
