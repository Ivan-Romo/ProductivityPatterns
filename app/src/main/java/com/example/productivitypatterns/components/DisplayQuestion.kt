package com.example.productivitypatterns.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.runtime.Composable
import com.example.productivitypatterns.domain.Question
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.productivitypatterns.components.Buttons.MediumButton
import com.example.productivitypatterns.components.Buttons.SmallButton
import com.example.productivitypatterns.ui.theme.*

@Composable
fun DisplayQuestion(question: Question, onReply: (Int) -> Unit, constr: BoxWithConstraintsScope) {
    Box(modifier = Modifier .shadow(8.dp, RoundedCornerShape(16.dp)).background(Color.White, shape = RoundedCornerShape(16.dp))) {
        //Surface(shadowElevation = 20.dp,shape = RoundedCornerShape(16.dp),color = Color.White,) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .width(constr.maxWidth * 0.83f)
                    .padding(20.dp)
            ) {
                Text(
                    question.question,
                    fontSize = 20.sp,
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                if (question is Question.YesNoQuestion) {
                    Row {
                        SmallButton(constr, text = "Yes", onClick = { onReply(1) })
                        SmallButton(constr, text = "No", onClick = { onReply(0) })
                    }
                } else if (question is Question.MultipleChoiceQuestion) {
                    Column {
                        question.options.forEachIndexed { index, option ->
                            MediumButton(constr, buttonText = option, onClick = { onReply(index) })
                        }
                        MediumButton(constr, buttonText = "Other", onClick = {
                            onReply(question.options.size)
                            //TODO
                        })
                    }

                } else if (question is Question.RatingQuestion) {
                    for (i in 1..10 step 3) {
                        Row(modifier = Modifier.padding(top = 10.dp)) {
                            SmallButton(constr, text = i.toString(), onClick = { onReply(i) })
                            if (i + 1 <= 10) {
                                SmallButton(constr, text = (i + 1).toString(), onClick = { onReply(i + 1) })
                            }
                            if (i + 2 <= 10) {
                                SmallButton(constr, text = (i + 2).toString(), onClick = { onReply(i + 2) })
                            }
                        }
                    }
                }
            }
        //}
    }
}