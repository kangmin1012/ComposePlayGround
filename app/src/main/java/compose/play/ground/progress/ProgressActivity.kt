package compose.play.ground.progress

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.IntRange
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
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
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
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            AnimateProgressBar()
            Spacer(modifier = Modifier.height(16.dp))
            DashProgress(5, 10)
        }
    }
}

/**
 * 높이가 20dp인 프로그레스 바를 애니메이션으로 증가시키는 UI Composable
 */
@Composable
private fun AnimateProgressBar() {
    var progress by remember { mutableFloatStateOf(0f) }
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(
            durationMillis = 2000,
            easing = LinearOutSlowInEasing
        ),
        label = "증가 애니메이션"
    )

    LaunchedEffect(Unit) {
        progress = 1f
    }

    Box(modifier = Modifier.fillMaxWidth().height(20.dp).background(Color.LightGray)) {
        Box(modifier = Modifier.height(20.dp).fillMaxWidth(animatedProgress).background(Color.Blue))
    }
}

/**
 * Dash 형태로 구성되는 프로그레스 바를 표현하는 UI Composable
 *
 * @param progress
 * @param maxProgress
 */
@Composable
private fun DashProgress(@IntRange(from = 1, to = 10) progress: Int, @IntRange(from = 1, to = 10) maxProgress: Int) {
    Row(
        modifier = Modifier.fillMaxWidth().height(20.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        for (index in 1 .. maxProgress) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(20.dp)
                    .background(
                        if (index <= progress) Color.Blue else Color.LightGray
                    )
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun ProgressScreenPreview() {
    ComposePlayGroundTheme {
        ProgressScreen()
    }
}


@Composable
@Preview
private fun AnimateProgressBarPreview() {
    ComposePlayGroundTheme {
        AnimateProgressBar()
    }
}

@Composable
@Preview(showBackground = true)
private fun DashProgressPreview() {
    ComposePlayGroundTheme {
        DashProgress(progress = 5, maxProgress = 6)
    }
}