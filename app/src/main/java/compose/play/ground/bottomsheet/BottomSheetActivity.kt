package compose.play.ground.bottomsheet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import compose.play.ground.ui.theme.ComposePlayGroundTheme
import kotlinx.coroutines.launch

class BottomSheetActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposePlayGroundTheme {
                BottomSheetScreen()
            }
        }
    }
}

@Composable
fun BottomSheetScreen() {
    val bottomSheetState = rememberModalBottomSheetState(
        // 스와이프, 외부 터치로 닫히지 않도록 설정
//        confirmValueChange = { false }
    )
    val coroutineScope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = {
                    showBottomSheet = true
                }
            ) {
                Text("버튼을 누르면 바텀시트가 나와요")
            }
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = bottomSheetState,
                // 뒤로가기에도 바텀시트가 닫히지 않도록 처리
//                properties = ModalBottomSheetProperties(shouldDismissOnBackPress = false)
            ) {
                PlayGroundBottomSheetContents {
                    coroutineScope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                        if (!bottomSheetState.isVisible) {
                            showBottomSheet = false
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PlayGroundBottomSheetContents(
    onClickDismiss: () -> Unit = {}
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp)
    ) {
        Text(
            "Welcom Compose Play Ground!",
            fontSize = 24.sp
        )
        Spacer(modifier = Modifier.height(100.dp))
        Button(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = {
                onClickDismiss()
            }
        ) {
            Text("Close")
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewBottomSheetScreen() {
    ComposePlayGroundTheme {
        BottomSheetScreen()
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewBottomSheetContents() {
    ComposePlayGroundTheme {
        PlayGroundBottomSheetContents()
    }
}
