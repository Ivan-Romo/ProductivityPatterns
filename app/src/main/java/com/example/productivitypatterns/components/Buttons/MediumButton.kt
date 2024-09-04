package com.example.productivitypatterns.components.Buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.productivitypatterns.ui.theme.InterFontFamily

@Composable
fun MediumButton(
    constr: BoxWithConstraintsScope,
    onClick: () -> Unit,
    buttonText: String,
    width: Dp = constr.maxWidth * 0.7f,
    colorScheme: ColorScheme,
    isInBackground: Boolean = true,
) {


    if (!isSystemInDarkTheme()) {
        ElevatedButton(
            elevation = ButtonDefaults.elevatedButtonElevation(
                defaultElevation = 10.dp, pressedElevation = 10.dp, disabledElevation = 4.dp
            ),
            onClick = {
                onClick()
            },
            shape = RoundedCornerShape(16),
            modifier = Modifier
                .padding(bottom = constr.maxWidth * 0.1f)
                .width(width)
                .height(constr.maxHeight * 0.06f)
                .background(colorScheme.surface),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            )

        ) {
            Text(text = buttonText, fontSize = 20.sp, fontFamily = InterFontFamily)
        }
    } else {
        Button(
            onClick = {
                onClick()
            },
            shape = RoundedCornerShape(16),
            modifier = Modifier
                .padding(bottom = constr.maxWidth * 0.1f)
                .width(width)
                .height(constr.maxHeight * 0.06f)
                .background(colorScheme.surface),
            colors = ButtonDefaults.buttonColors(
                containerColor = if(isInBackground) colorScheme.surface else colorScheme.background,
                contentColor = colorScheme.onSurface,
            )
        ) {
            Text(text = buttonText, fontSize = 20.sp, fontFamily = InterFontFamily)
        }
    }
}