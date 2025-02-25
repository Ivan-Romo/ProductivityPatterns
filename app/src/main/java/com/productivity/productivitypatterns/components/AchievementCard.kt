package com.productivity.productivitypatterns.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.productivity.productivitypatterns.ui.theme.InterFontFamily

@Composable
fun AchievementCard(isCompleted: Boolean, text: String, type:String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top=8.dp, start = 8.dp, end = 8.dp, bottom = 8.dp),
        colors = if(type=="Streak")CardDefaults.cardColors(Color(255,230,230))
                 else if(type=="Productivity") CardDefaults.cardColors(Color(230,255,230))
                 else CardDefaults.cardColors(Color(230,230,255)),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
               // .clickable(onClick = { activityFinished = true })
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            color = Color.Red.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.FreeCancellation,
                        contentDescription = null,
                        tint = Color.Red
                    )
                }
                Text(
                    text = text,
                    fontFamily = InterFontFamily,
//                                        fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    style = MaterialTheme.typography.titleLarge
                )
            }
            Box(
                modifier = Modifier
                    .background(
                        color = Color(13, 188, 171).copy(alpha = 0.1f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = if (isCompleted) Icons.Default.Check else Icons.Default.Error,
                    contentDescription = null,
                    tint = Color(13, 188, 171)
                )
            }
        }
    }
}