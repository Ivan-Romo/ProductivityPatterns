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




