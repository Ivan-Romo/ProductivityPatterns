package com.productivity.productivitypatterns.view

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.PostAdd
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.productivity.productivitypatterns.components.Buttons.MediumButton
import com.productivity.productivitypatterns.components.DisplayQuestion
import com.productivity.productivitypatterns.components.Buttons.SmallButton
import com.productivity.productivitypatterns.components.TypeDropdown
import com.productivity.productivitypatterns.domain.Session
import com.productivity.productivitypatterns.ui.theme.*
import com.productivity.productivitypatterns.util.formatTime
import com.productivity.productivitypatterns.viewmodel.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.*
import com.productivity.productivitypatterns.components.AchievementCard
import kotlin.math.min


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Achievements(gamificationViewModel: GamificationViewModel) {
    LazyColumn {
        var achievements = gamificationViewModel.getAchievements()
        achievements.forEach { (key, value) ->
            item { AchievementCard((value.first), key, value.second) }
        }

//        item { AchievementCard(true, "3 days in a row", "Streak") }
//        item { AchievementCard(false, "10 days in a row", "Streak") }
//        item { AchievementCard(false, "10 days in a row", "Productivity") }
//        item { AchievementCard(false, "10 days in a row","") }
//        item { AchievementCard(false, "10 days in a row", "Streak") }
//        item { AchievementCard(false, "10 days in a row", "Productivity") }
//        item { AchievementCard(false, "10 days in a row","") }
//        item { AchievementCard(false, "10 days in a row", "Streak") }
//        item { AchievementCard(false, "10 days in a row", "Productivity") }
//        item { AchievementCard(false, "10 days in a row","") }
    }
}
