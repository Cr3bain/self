package tr.com.gndg.self.ui.product.stateScreens

import android.Manifest
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import tr.com.gndg.self.R
import tr.com.gndg.self.domain.join.ProductsJoinUiState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PictureStateScreen(
    modifier: Modifier,
    productsJoinUiState: ProductsJoinUiState,
    deleteImage: (String) -> Unit,
    toCameraScreen: () -> Unit
) {
    val cameraPermissionState = rememberPermissionState(
        permission = Manifest.permission.CAMERA )


    var productImage by remember {
        mutableStateOf(productsJoinUiState.imageUri)
    }


    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(
                onClick = {
                    if (productImage != null) {
                        deleteImage(productImage!!)
                        productImage = null
                    }
                },
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Clear Picture"
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
                    text = stringResource(id = R.string.takePhoto)
                )
            }
        }

        AsyncImage(
            model = productImage?: R.drawable.baseline_image_white_54,
            contentDescription= "Product Image",
            placeholder = painterResource(R.drawable.baseline_image_white_54),
        )

    }

}