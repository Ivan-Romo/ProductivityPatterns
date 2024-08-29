package com.example.productivitypatterns.util


import com.example.productivitypatterns.domain.Question
import java.util.*

val listQuestions = listOf<Question>(
    Question.RatingQuestion(id = "prod", question = "How would you rate your productivity?"),
    Question.YesNoQuestion(id = "train", question = "Have you trained today?"),
    Question.MultipleChoiceQuestion(
        id = "music",
        question = "What genre of music have you listened to?",
        options = listOf(
            "No music",
            "White noise",
            "Lo fi",
            "Rock",
            "Other (TODO)"
        )
    ),
    Question.YesNoQuestion("cafe", question = "Did you have caffeine?"),
    Question.RatingQuestion(id = "sleep", question = "How did you sleep?"),
    Question.RatingQuestion(id = "stress", question = "What was the stress level during the session?"),
    Question.MultipleChoiceQuestion(
        id = "location",
        question = "Where was the session?",
        options = listOf(
            "Café",
            "Office",
            "Home",
            "Library",
            "Other (TODO)"
        )
    ),
)