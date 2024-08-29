package com.example.productivitypatterns.util

import com.example.productivitypatterns.domain.MultipleChoiceQuestion
import com.example.productivitypatterns.domain.Question
import com.example.productivitypatterns.domain.RatingQuestion
import com.example.productivitypatterns.domain.YesNoQuestion
import java.util.*

val listQuestions = listOf<Question>(
    RatingQuestion(id = "prod", question = "How would you rate your productivity?"),
    YesNoQuestion(id = "train",question = "Have you trained today?"),
    MultipleChoiceQuestion(
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
    YesNoQuestion("cafe",question = "Did you have caffeine?"),
    RatingQuestion(id = "sleep",question = "How did you sleep?"),
    RatingQuestion(id ="stress",question = "What was the stress level during the session?"),
    MultipleChoiceQuestion(
        id ="location",
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