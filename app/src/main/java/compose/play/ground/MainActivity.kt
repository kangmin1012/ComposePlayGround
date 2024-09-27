package compose.play.ground

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import compose.play.ground.bottomsheet.BottomSheetActivity
import compose.play.ground.coordinate.CoordinateActivity
import compose.play.ground.permission.MediaPermissionActivity
import compose.play.ground.progress.ProgressActivity
import compose.play.ground.ui.theme.ComposePlayGroundTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposePlayGroundTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val context = LocalContext.current

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Button(
                    onClick = {
                        context.startActivity(Intent(context, MediaPermissionActivity::class.java))
                    }
                ) {
                    Text("미디어 권한 샘플")
                }
            }

            item {
                Button(
                    onClick = {
                        context.startActivity(Intent(context, ProgressActivity::class.java))
                    }
                ) {
                    Text("프로그레스 샘플")
                }
            }

            item {
                Button(
                    onClick = {
                        context.startActivity(Intent(context, CoordinateActivity::class.java))
                    }
                ) {
                    Text("Coordinate 샘플")
                }
            }

            item {
                Button(
                    onClick = {
                        context.startActivity(Intent(context, BottomSheetActivity::class.java))
                    }
                ) {
                    Text("BottomSheet 샘플")
                }
            }
        }

    }
}


@Composable
@Preview(showBackground = true)
fun ScreePreview() {
    ComposePlayGroundTheme {
        MainScreen()
    }
}