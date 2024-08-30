package com.example.productivitypatterns.domain

import com.example.productivitypatterns.util.listQuestions
import kotlinx.serialization.Required
import kotlinx.serialization.Serializable

@Serializable
data class Personalization(
    val customQuestions: MutableList<Question>,
    val customAnswers: Map<String,List<String>>,
    val enabledQuestions: MutableMap<String,Boolean>,
    val mode : Boolean
){
    companion object {
        fun default(): Personalization {
            return Personalization(
                customQuestions = mutableListOf(),
                customAnswers = emptyMap(),
                enabledQuestions = (listQuestions.associate { it.id to true }).toMutableMap(),
                mode = false
            )
        }
    }
}
