package com.example.productivitypatterns.util

import com.example.productivitypatterns.domain.MultipleChoiceQuestion
import com.example.productivitypatterns.domain.Question
import com.example.productivitypatterns.domain.RatingQuestion
import com.example.productivitypatterns.domain.YesNoQuestion

val listQuestions = listOf<Question>(
   // YesNoQuestion(question = "¿Has tomado cafe?"),
    RatingQuestion(question = "¿Como valorarias tu productividad?"),
//    MultipleChoiceQuestion(
//        question = "¿Qué tipo de música has esuchado?",
//        options = listOf(
//            "Sin musica",
//            "Ruido blanco",
//            "Lo fi",
//            "Rock",
//            "Otro (TODO)"
//        )
//    ),
)