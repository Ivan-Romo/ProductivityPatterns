package com.example.productivitypatterns.domain

import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

@Serializable
data class UserData(
    val customQuestions: List<Question>,
    val customAnswers: Map<String,String>,
    val mode : Boolean
)

data class Session(
    val id: String = UUID.randomUUID().toString(),
    val duration: Long,
    val datetime: LocalDateTime = LocalDateTime.now(),
    val responses: Map<String,Int>,
    val type: String
)

@Serializable
sealed class Question {
    abstract val id: String
    abstract val question: String

    @Serializable
    data class MultipleChoiceQuestion(
        override val id: String = UUID.randomUUID().toString(),
        override val question: String,
        val options: List<String>,
    ) : Question()

    @Serializable
    data class YesNoQuestion(
        override val id: String = UUID.randomUUID().toString(),
        override val question: String,
    ) : Question()

    @Serializable
    data class RatingQuestion(
        override val id: String = UUID.randomUUID().toString(),
        override val question: String,
    ) : Question()
}


fun Session.toHashMap(): HashMap<String, Any> {
    val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    return hashMapOf(
        "id" to id,
        "duration" to duration,
        "datetime" to datetime.format(formatter),
        "responses" to responses,
        "type" to type
    )
}

fun hashMapToSession(map: HashMap<String, Any>): Session {
    @Suppress("UNCHECKED_CAST")
    val responsesMap = map["responses"] as Map<String, Any> // Obtener el mapa como Map<String, Any>
    val convertedResponses = responsesMap.mapValues { (_, value) ->
        // Convertir cada valor a Int, si es Long o si es otro tipo numérico
        (value as? Number)?.toInt() ?: throw IllegalArgumentException("Invalid number format in responses")
    }

    return Session(
        id = map["id"] as String,
        duration = (map["duration"] as Long),
        datetime = LocalDateTime.parse(map["datetime"] as String),
        responses = convertedResponses, // Usar el mapa convertido
        type = map["type"] as String
    )
}



