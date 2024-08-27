package com.example.productivitypatterns.components

import androidx.compose.runtime.Composable
import com.example.productivitypatterns.domain.Question
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.productivitypatterns.components.DisplayQuestion
import com.example.productivitypatterns.components.charts.RadialCircleChart
import com.example.productivitypatterns.domain.MultipleChoiceQuestion
import com.example.productivitypatterns.domain.RatingQuestion
import com.example.productivitypatterns.domain.YesNoQuestion
import com.example.productivitypatterns.ui.theme.*
import com.example.productivitypatterns.util.formatTime

@Composable
fun DisplayQuestion(question: Question, onReply : (Int) -> Unit, onCancel : () -> Unit, constr: BoxWithConstraintsScope) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceBetween) {
        Text(question.question)
        if(question is YesNoQuestion) {

        }
        else if(question is MultipleChoiceQuestion) {

        }
        else if(question is RatingQuestion) {
            Row(){
                for(i in 0..9){
                    SmallButton(constr, text = i.toString(), onClick = {onReply(i)})
                }
            }
        }

        Button(onClick = onCancel) {
            Text("Cancel")
        }
    }
}