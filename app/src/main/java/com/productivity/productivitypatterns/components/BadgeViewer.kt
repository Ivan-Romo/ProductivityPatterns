package com.productivity.productivitypatterns.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.LibraryAdd
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.productivity.productivitypatterns.R
import com.productivity.productivitypatterns.ui.theme.BlueCharts
import com.productivity.productivitypatterns.ui.theme.BlueChartsCode
import com.productivity.productivitypatterns.ui.theme.InterFontFamily
import com.productivity.productivitypatterns.viewmodel.GamificationViewModel

@Composable
fun BadgeViewer(gamificationViewModel: GamificationViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(12.dp)
    ) {

        Row(
            modifier = Modifier
                .padding(top = 8.dp, bottom = 8.dp, start = 16.dp, end = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            val badgeDrawable = when (gamificationViewModel.getLevel().coerceAtMost(5)) {
                1 -> R.drawable.badge1
                2 -> R.drawable.badge2
                3 -> R.drawable.badge3
                4 -> R.drawable.badge4
                5 -> R.drawable.badge5
                else -> R.drawable.badge1
            }
            Image(
                painter = painterResource(badgeDrawable),
                contentDescription = "",
                modifier = Modifier.size(70.dp)

            )

            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (i in 1..(4)) { //3+gamificationViewModel.getLevel()/2
                    if (gamificationViewModel.getPoints() < i) {
                        Icon(
                            Icons.Filled.StarOutline,
                            "Forward",
                            tint = Color(13, 188, 171),
                            modifier = Modifier.size(30.dp)
                        )
                    } else {
                        Icon(
                            Icons.Filled.Star,
                            "Forward",
                            tint = Color(13, 188, 171),
                            modifier = Modifier.size(30.dp)
                        )
                    }

                }
            }
        }
    }
}