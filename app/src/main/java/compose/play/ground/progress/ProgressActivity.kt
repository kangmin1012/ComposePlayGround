package compose.play.ground.progress

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.IntRange
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import compose.play.ground.ui.theme.ComposePlayGroundTheme

/**
 * 프로그레스 바를 표현하는 방법에 대한 Sample Activity입니다.
 *
 */
class ProgressActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposePlayGroundTheme {
                ProgressScreen()
            }
        }
    }
}

@Composable
private fun ProgressScreen() {
    var progress by remember { mutableIntStateOf(0) }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White),
        ) {
            DashProgress(
                progress = progress,
                maxProgress = 10,
                progressFillSpeedMillisecond = 100,
                progressHeight = 10.dp
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                progress = (0..10).filter { it != progress }.random()
            }) {
                Text("Randomize Progress")
            }
        }
    }
}

/**
 * 대시 형태의 프로그레스바
 *
 * @param progress 진행 상태
 * @param maxProgress 최대 프로그레스 진행도
 * @param progressFillSpeedMillisecond 프로그레스 채워지는 속도
 */
@Composable
internal fun DashProgress(
    @IntRange(from = 0, to = 10) progress: Int,
    @IntRange(from = 1, to = 10) maxProgress: Int = 10,
    progressFillSpeedMillisecond: Int = 100,
    progressHeight: Dp = 6.dp,
    modifier: Modifier = Modifier
) {
    var nowProgress by remember { mutableIntStateOf(progress) }
    var previousProgress by remember { mutableIntStateOf(0) }

    LaunchedEffect(progress) {
        previousProgress = nowProgress
        nowProgress = progress
    }

    Box(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(progressHeight),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            for (index in 1..maxProgress) {
                DashItem(
                    modifier = Modifier.weight(1f),
                    isFilled = index <= nowProgress,
                    speedMillis = progressFillSpeedMillisecond,
                    delayMillis =
                    if (nowProgress > previousProgress) {
                        if (index in (previousProgress + 2)..progress) {
                            progressFillSpeedMillisecond * (index - previousProgress - 1)
                        } else {
                            0
                        }
                    } else {
                        if (index in (progress + 1)..previousProgress) {
                            progressFillSpeedMillisecond * (previousProgress - index)
                        } else {
                            0
                        }
                    }

                )
            }
        }
    }
}

@Composable
private fun DashItem(
    modifier: Modifier = Modifier,
    isFilled: Boolean,
    speedMillis: Int = 0,
    delayMillis: Int = 0,
) {
    val scaleAnimation by animateFloatAsState(
        targetValue = if (isFilled) 1f else 0f,
        animationSpec = tween(
            durationMillis = speedMillis,
            delayMillis = delayMillis,
            easing = LinearEasing
        ),
        label = ""
    )

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFEDEDED)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    transformOrigin = TransformOrigin(0f, 0.5f)
                    scaleX = scaleAnimation
                }
                .background(Color(0xFFFF7949))
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun ProgressScreenPreview() {
    ComposePlayGroundTheme {
        ProgressScreen()
    }
}
