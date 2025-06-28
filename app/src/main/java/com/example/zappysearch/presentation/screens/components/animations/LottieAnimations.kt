package com.example.zappysearch.presentation.screens.components.animations

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.dotlottie.dlplayer.Mode
import com.lottiefiles.dotlottie.core.compose.ui.DotLottieAnimation
import com.lottiefiles.dotlottie.core.util.DotLottieSource

@Composable
fun LottieSearchAnimation() {
    DotLottieAnimation(
        source = DotLottieSource.Url("https://lottie.host/2c5f24cb-8845-4b69-bb84-d7db2170e8c3/sSKd9twqEq.lottie"),
        autoplay = true,
        loop = true,
        speed = 1.5f,
        useFrameInterpolation = true,
        playMode = Mode.FORWARD,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
            .height(200.dp)
            .background(color = MaterialTheme.colorScheme.background)
    )
}