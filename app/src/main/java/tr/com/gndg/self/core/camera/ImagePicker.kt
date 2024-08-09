package tr.com.gndg.self.core.camera

import android.Manifest
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import coil.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState


/*
class ComposeFileProvider : FileProvider(
    R.xml.filepaths
) {
    companion object {
        fun getImageUri(context: Context): Uri {
            val directory = File(context.filesDir, Constants.IMAGE_FOLDER)
            directory.mkdirs()
            val file = File.createTempFile(
                "selected_image_",
                ".jpg",
                directory,
            )
            val authority = context.packageName + ".fileprovider"
            return getUriForFile(
                context,
                authority,
                file,
            )
        }
    }
}
*/

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ImagePicker(
    modifier: Modifier,
    productImageUri: String?,
    newProductImageUri: (Uri?) -> Unit,
    toCameraScreen: () -> Unit
) {

    val cameraPermissionState = rememberPermissionState(
        permission = Manifest.permission.CAMERA )


    var productImage by remember {
        mutableStateOf(productImageUri)
    }


    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            if (uri != null) {
                productImage = uri.toString()
                newProductImageUri(uri)
            }
        }
    )
/*
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            hasImage = success
        }
    )

    val context = LocalContext.current*/
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(
                onClick = {
                    newProductImageUri(null)
                    productImage = null
                },
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null
                )
            }

            ElevatedButton(
                onClick = {
                    imagePicker.launch("image/*")
                },
            ) {
                Text(
                    text = "Select Image"
                )
            }
            ElevatedButton(
                onClick = {
                    if (cameraPermissionState.hasPermission) {
                        toCameraScreen()
  /*                      val uri = ComposeFileProvider.getImageUri(context)
                        imageUri = uri
                        cameraLauncher.launch(uri)*/
                    } else {

                        cameraPermissionState.launchPermissionRequest()
                    }

                },
            ) {
                Text(
                    text = "Take photo"
                )
            }
        }

        if (productImage != null) {
            AsyncImage(
                model = productImage,
                modifier = Modifier.fillMaxWidth(),
                contentDescription = "Selected image",
            )
        }

    }
}