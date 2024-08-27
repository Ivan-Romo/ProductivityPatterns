package com.example.productivitypatterns.components.Buttons

import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
    size: Float = 0.115f
) {
    ElevatedButton(
        elevation = ButtonDefaults.elevatedButtonElevation(
            defaultElevation = 10.dp, pressedElevation = 10.dp, disabledElevation = 4.dp
        ),
        onClick = {
            onClick()
        },
        shape = RoundedCornerShape(16),
        modifier = Modifier
            .padding(top = constr.maxWidth * 0.04f, end = constr.maxWidth * 0.04f)
            .width(constr.maxHeight * size)
            .height(constr.maxHeight * size),

        colors = ButtonDefaults.elevatedButtonColors(
            containerColor = Color.White // Color de fondo del botón
        ),
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                "",
            )
        }

        if (text != "") {
            Text(
                text = text,
                fontFamily = InterFontFamily,
                fontWeight = FontWeight.Bold,
            )

        }
    }
}