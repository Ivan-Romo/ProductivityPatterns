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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.productivitypatterns.ui.theme.InterFontFamily

@Composable
fun SmallButton(
    constr: BoxWithConstraintsScope,
    icon: ImageVector? = null,
    text: String = "",
    onClick: () -> Unit = {},
    size: Float = 0.115f,
    colorScheme: ColorScheme ,
    isMainScreen: Boolean = false,
) {
    if(!isSystemInDarkTheme()) {
        ElevatedButton(
            elevation = ButtonDefaults.elevatedButtonElevation(
                defaultElevation = 6.dp,
                pressedElevation = 8.dp,
                disabledElevation = 0.dp
            ),
            onClick = { onClick() },
            shape = RoundedCornerShape(16),
            modifier = Modifier
                .padding(top = constr.maxWidth * 0.04f, end = constr.maxWidth * 0.04f)
                .width(constr.maxHeight * size)
                .height(constr.maxHeight * size),
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            )
        ) {
            if (icon != null) {
                Icon(imageVector = icon, contentDescription = null)
            }

            if (text.isNotEmpty()) {
                Text(
                    text = text,
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.onSurface // Asegúrate de que el color del texto sea visible
                )
            }
        }
    }
    else{
        Button(
            onClick = {
                onClick()
            },
            shape = RoundedCornerShape(16),
            modifier = Modifier
                .padding(top = constr.maxWidth * 0.04f, end = constr.maxWidth * 0.04f)
                .width(constr.maxHeight * size)
                .height(constr.maxHeight * size),
            colors = ButtonDefaults.buttonColors(
                containerColor = if(isMainScreen) colorScheme.surface else colorScheme.background,
                contentColor = colorScheme.onSurface
            ),
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                )
            }

            if (text.isNotEmpty()) {
                Text(
                    text = text,
                    fontFamily = InterFontFamily,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}