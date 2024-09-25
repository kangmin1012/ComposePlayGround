package compose.play.ground.coordinate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import compose.play.ground.ui.theme.ComposePlayGroundTheme

/**
 * 특정 아이템이 보이지 않게 되었을 때 상단에 고정적인 화면 하나를 표시하는 Sample Activity입니다.
 */
class CoordinateActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposePlayGroundTheme {
                CoordinateScreen()
            }
        }
    }
}

@Composable
private fun CoordinateScreen() {
    val listItem = (1 .. 100).map { it.toString() }
    val spanCount = 3

    val listState = rememberLazyListState()
    val isFirstItemVisible by remember {
        derivedStateOf { listState.firstVisibleItemIndex == 0 }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize()
            ) {
                item { Top() }

                items(listItem.chunked(spanCount)) {
                    Bottom(listItem = it, spanCount = spanCount)
                }
            }

            if (!isFirstItemVisible) {
                TopB()
            }
        }
    }
}

@Composable
private fun Top() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .background(Color.Red)
    )
}

@Composable
private fun Bottom(
    listItem: List<String>,
    spanCount: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        listItem.forEach {
            BottomItem(
                modifier = Modifier.weight(1f),
                text = it
            )
        }

        val emptyCells = (spanCount - listItem.size)
        if (emptyCells > 0) {
            Spacer(modifier = Modifier.weight(emptyCells.toFloat()))
        }
    }
}

@Composable
private fun BottomItem(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        modifier = modifier,
        text = "Item $text",
        textAlign = TextAlign.Center
    )
}

@Composable
private fun TopB() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(Color.LightGray)
    )
}

@Composable
@Preview(showBackground = true)
private fun CoordinateScreenPreview() {
    ComposePlayGroundTheme {
        CoordinateScreen()
    }
}

@Composable
@Preview(showBackground = true)
private fun BottomPreview() {
    ComposePlayGroundTheme {
         Bottom(
             listItem = listOf("1", "2", "3"),
             3
         )
    }
}