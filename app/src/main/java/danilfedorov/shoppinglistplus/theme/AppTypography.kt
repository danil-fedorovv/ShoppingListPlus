package danilfedorov.shoppinglistplus.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import danilfedorov.common.core_res.latoFont


val AppTypography = Typography(
    titleLarge = TextStyle(
        fontFamily = latoFont,
        fontWeight = FontWeight.Black,
        fontSize = 20.sp,
        lineHeight = 24.sp
    ),
    titleMedium = TextStyle(
        fontFamily = latoFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = 20.sp
    ),
    titleSmall = TextStyle(
        fontFamily = latoFont,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 16.sp
    )
)