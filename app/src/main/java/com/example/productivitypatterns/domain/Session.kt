package com.example.productivitypatterns.domain

import java.time.LocalDateTime
import java.util.UUID

data class Session(
    val id: UUID = UUID.randomUUID(),
    val duration: Long,
    val datetime: LocalDateTime,
    val responses: List<Pair<UUID,Int>>,
    val type: String
)

interface Question {
    val id: UUID
    val question: String
}

data class MultipleChoiceQuestion(
    override val id: UUID = UUID.randomUUID(),
    override val question: String,
    val options: List<String>,
) : Question

data class YesNoQuestion(
    override val id: UUID = UUID.randomUUID(),
    override val question: String,
) : Question

data class RatingQuestion(
    override val id: UUID = UUID.randomUUID(),
    override val question: String,
) : Question


fun Session.toHashMap(): HashMap<String, Any?> {
    val map = HashMap<String, Any?>()
    map["id"] = id.toString()
    map["duration"] = duration
    map["datetime"] = datetime.toString()
    map["responses"] = responses.map { response ->
        hashMapOf(
            "questionId" to response.first.toString(),
            "answer" to response.second
        )
    }
    map["type"] = type
    return map
}

fun hashMapToSession(sessionMap: HashMap<String, Any?>): Session {
    return Session(
        id = UUID.fromString(sessionMap["id"] as String),
        duration = sessionMap["duration"] as Long,
        datetime = LocalDateTime.parse(sessionMap["datetime"] as String),
        responses = (sessionMap["responses"] as List<HashMap<String, Any?>>).map { responseMap ->
            Pair(
                UUID.fromString(responseMap["questionId"] as String),
                (responseMap["answer"] as Long).toInt()
            )
        },
        type = sessionMap["type"] as String
    )
}




