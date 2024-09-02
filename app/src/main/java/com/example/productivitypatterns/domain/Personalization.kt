package com.example.productivitypatterns.domain

import com.example.productivitypatterns.util.listQuestions
import kotlinx.serialization.Required
import kotlinx.serialization.Serializable

@Serializable
data class Personalization(
    val customQuestions: MutableList<Question>,
    val customAnswers: MutableMap<String,MutableList<String>>,
    val enabledQuestions: MutableMap<String,Boolean>,
    val mode : Boolean
){
    companion object {
        fun default(): Personalization {
            return Personalization(
                customQuestions = mutableListOf(),
                customAnswers = mutableMapOf(),
                enabledQuestions = (listQuestions.associate { it.id to true }).toMutableMap(),
                mode = false
            )
        }
    }
}
