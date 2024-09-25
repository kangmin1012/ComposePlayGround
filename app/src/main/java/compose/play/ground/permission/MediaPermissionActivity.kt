package compose.play.ground.permission

import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import compose.play.ground.ui.theme.ComposePlayGroundTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 미디어 권한에 대한 Sample Activity입니다.
 * Target SDK 34 부터 추가 적용되는 READ_MEDIA_VISUAL_USER_SELECTED 권한에 대해 알아봅니다.
 */
class MediaPermissionActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposePlayGroundTheme {
                PermissionScreen()
            }
        }
    }
}

@Composable
fun PermissionScreen() {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    // 앨범을 불러오는 Intent
    val imageAlbumIntent =
        Intent(Intent.ACTION_PICK).apply {
            setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                MediaStore.Images.Media.CONTENT_TYPE
            )
            type = "image/*"
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
            putExtra(
                Intent.EXTRA_MIME_TYPES,
                arrayOf("image/jpeg", "image/png", "image/bmp", "image/webp")
            )
        }

    val albumLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                Activity.RESULT_OK -> {
                    result.data?.data?.let { uri ->
                        uri.let {
                            Log.d("TargetSDK", "imageUri - selected : $uri")
                        }
                    }
                }
            }
        }

    // Photo Picker를 실행하는 Launcher
    val photoPickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) {
            it?.let { uri ->
                Log.d("TargetSDK", "imageUri - selected : $uri")
            }
        }

    val permissionCheckLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { grantedPermissionMap ->
            if (checkPhotoPermissionGranted(context)) {
                coroutineScope.launch {
                    val images = getImages(context.contentResolver)
                    Log.d("TargetSDK", "imageUri - selected : ${images.map { it.uri }}")
                }
//                photoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
//                albumLauncher.launch(imageAlbumIntent)
            } else {
                Toast.makeText(context, "권한이 없습니다", Toast.LENGTH_SHORT).show()
            }
        }


    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = {
                    context.startActivity(
                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            setData(Uri.parse("package:${context.packageName}"))
                        }
                    )
                }
            ) {
                Text("권한 설정 화면 이동")
            }

            Button(
                onClick = {
                    val permissionGranted = checkPhotoPermissionGranted(context)

                    if (permissionGranted) {
                        coroutineScope.launch {
                            val images = getImages(context.contentResolver)
                            Log.d("TargetSDK", "imageUri - selected : ${images.map { it.uri }}")
                        }
//                        photoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
//                        albumLauncher.launch(imageAlbumIntent)
                    } else {
                        permissionCheckLauncher.launch(getImagePermission().toTypedArray())
                    }
                }
            ) {
                Text("사진")
            }

            Button(
                onClick = {

                }
            ) {
                Text("카메라")
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun PermissionScreenPreview() {
    ComposePlayGroundTheme {
        PermissionScreen()
    }
}

/**
 * 이미지 권한과 관련되어 현재 권한 설정이 허용되어 있는지를 체크하는 함수
 *
 * @param context
 * @return
 */
private fun checkPhotoPermissionGranted(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_MEDIA_IMAGES
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        true
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_MEDIA_IMAGES
        ) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
                ) == PackageManager.PERMISSION_GRANTED
    } else {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }
}

/**
 * SDK 버전에 따라 이미지를 불러오기 위해 필요한 Manifest 권한을 반환하는 함수
 *
 */
private fun getImagePermission() =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
        listOf(
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
        )
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        listOf(Manifest.permission.READ_MEDIA_IMAGES)
    } else {
        listOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

data class Media(
    val uri: Uri,
    val name: String,
    val size: Long,
    val mimeType: String,
)

/**
 * MediaStore를 이용해서 이미지를 불러오는 함수
 *
 * @param contentResolver
 * @return
 */
suspend fun getImages(contentResolver: ContentResolver): List<Media> = withContext(Dispatchers.IO) {
    val projection = arrayOf(
        MediaStore.Images.Media._ID,
        MediaStore.Images.Media.DISPLAY_NAME,
        MediaStore.Images.Media.SIZE,
        MediaStore.Images.Media.MIME_TYPE,
    )

    val collectionUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        // Query all the device storage volumes instead of the primary only
        MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
    } else {
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    }

    val images = mutableListOf<Media>()

    contentResolver.query(
        collectionUri,
        projection,
        null,
        null,
        "${MediaStore.Images.Media.DATE_ADDED} DESC"
    )?.use { cursor ->
        val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
        val displayNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
        val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)
        val mimeTypeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE)

        while (cursor.moveToNext()) {
            val uri = ContentUris.withAppendedId(collectionUri, cursor.getLong(idColumn))
            val name = cursor.getString(displayNameColumn)
            val size = cursor.getLong(sizeColumn)
            val mimeType = cursor.getString(mimeTypeColumn)

            val image = Media(uri, name, size, mimeType)
            images.add(image)
        }
    }

    return@withContext images
}