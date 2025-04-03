package com.dara.swiftfx.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.dara.swiftfx.R

val brFirmaFamily = FontFamily(
    Font(R.font.br_firma_bold, FontWeight.Bold),
    Font(R.font.br_firma_light, FontWeight.Light),
    Font(R.font.br_firma_medium, FontWeight.Medium),
    Font(R.font.br_firma_regular, FontWeight.Normal),
    Font(R.font.br_firma_semi_bold, FontWeight.SemiBold),
    Font(R.font.br_firma_thin, FontWeight.Thin),
)

val Typography = Typography(
    headlineLarge = TextStyle(
        fontFamily = brFirmaFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.5.sp
    ),
    titleLarge = TextStyle(
        fontFamily = brFirmaFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = brFirmaFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    labelLarge = TextStyle(
        fontFamily = brFirmaFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
)
