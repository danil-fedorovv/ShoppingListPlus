package danilfedorov.common.compose

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import danilfedorov.common.R


@Composable
fun LoadAnimation(
    isPlaying: Boolean = true,
) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.load_anim)
    )
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever,
        restartOnPlay = false,
        isPlaying = isPlaying

    )

    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = Modifier
            .size(128.dp)
    )
}