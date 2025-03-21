package com.productivity.productivitypatterns.components.Buttons

import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.productivity.productivitypatterns.ui.theme.InterFontFamily

@Composable
fun BigButton(constr: BoxWithConstraintsScope, onClick: () -> Unit, buttonText: String) {
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
            .width(constr.maxWidth * 0.7f)
            .height(constr.maxHeight * 0.1f),

        colors = ButtonDefaults.elevatedButtonColors(
            containerColor = Color.White // Color de fondo del botón
        ),
    ) {
        Text(text = buttonText, fontSize = 20.sp, fontFamily = InterFontFamily)
    }
}