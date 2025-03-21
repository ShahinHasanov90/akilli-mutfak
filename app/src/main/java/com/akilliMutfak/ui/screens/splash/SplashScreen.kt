package com.akilliMutfak.ui.screens.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.akilliMutfak.R
import com.airbnb.lottie.compose.*
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit
) {
    var startAnimation by remember { mutableStateOf(false) }
    val scale = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.3f,
        animationSpec = tween(
            durationMillis = 1000,
            easing = FastOutSlowInEasing
        )
    )

    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(2000L)
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo animasyonu
            val composition by rememberLottieComposition(
                LottieCompositionSpec.RawRes(R.raw.cooking_animation)
            )
            val progress by animateLottieCompositionAsState(
                composition = composition,
                iterations = 1,
            )
            
            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier
                    .size(200.dp)
                    .scale(scale.value)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Uygulama adı
            Text(
                text = "Akıllı Mutfak",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.scale(scale.value)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Slogan
            Text(
                text = "Mutfağınızın Akıllı Asistanı",
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 16.sp,
                modifier = Modifier.scale(scale.value)
            )
        }
    }
}