package com.productivity.productivitypatterns.domain

import kotlinx.serialization.Contextual
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

@Serializable
data class Session(
    val id: String = UUID.randomUUID().toString(),
    val duration: Long,
    @Serializable(with = LocalDateTimeSerializer::class) val datetime: LocalDateTime = LocalDateTime.now(),
    val responses: Map<String,String>,
    val type: String
)

@Serializable
sealed class Question {
    abstract val id: String
    abstract val question: String
    abstract val title: String?

    @Serializable
    data class MultipleChoiceQuestion(
        override val id: String = UUID.randomUUID().toString(),
        override val question: String,
        override val title: String? = "",
        val options: MutableList<String>,
    ) : Question()

    @Serializable
    data class YesNoQuestion(
        override val id: String = UUID.randomUUID().toString(),
        override val question: String,
        override val title: String? = "",
    ) : Question()

    @Serializable
    data class RatingQuestion(
        override val id: String = UUID.randomUUID().toString(),
        override val question: String,
        override val title: String? = "",
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
        (value as? String) ?: throw IllegalArgumentException("Invalid number format in responses")
    }

    return Session(
        id = map["id"] as String,
        duration = (map["duration"] as Long),
        datetime = LocalDateTime.parse(map["datetime"] as String),
        responses = convertedResponses, // Usar el mapa convertido
        type = map["type"] as String
    )
}

object LocalDateTimeSerializer : KSerializer<LocalDateTime> {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("LocalDateTime", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        encoder.encodeString(value.format(formatter))
    }

    override fun deserialize(decoder: Decoder): LocalDateTime {
        return LocalDateTime.parse(decoder.decodeString(), formatter)
    }
}



