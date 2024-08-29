package com.example.productivitypatterns.util

import com.example.productivitypatterns.domain.MultipleChoiceQuestion
import com.example.productivitypatterns.domain.Question
import com.example.productivitypatterns.domain.RatingQuestion
import com.example.productivitypatterns.domain.YesNoQuestion
import java.util.*

val listQuestions = listOf<Question>(
    RatingQuestion(question = "How would you rate your productivity?"),
    YesNoQuestion(question = "Have you trained today?"),
    MultipleChoiceQuestion(

        question = "What genre of music have you listened to?",
        options = listOf(
            "No music",
            "White noise",
            "Lo fi",
            "Rock",
            "Other (TODO)"
        )
    ),
    YesNoQuestion(question = "Did you have caffeine?"),
    RatingQuestion(question = "How did you sleep?"),
    RatingQuestion(question = "What was the stress level during the session?"),
    MultipleChoiceQuestion(
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