package tr.com.gndg.self.core.camera

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import android.util.Rational
import android.view.Surface
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import org.koin.androidx.compose.koinViewModel
import tr.com.gndg.self.R
import tr.com.gndg.self.core.fileProvider.getImageDirectory
import tr.com.gndg.self.ui.navigation.NavigationDestination
import tr.com.gndg.self.ui.product.presentation.ProductViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.*



object CameraDestination : NavigationDestination {
    override val route = "camera"
    override val titleRes = R.string.cameraScreen_title
    const val uuidArg = "productUUID"
    val routeWithUUIDArgs = "$route/{$uuidArg}"
}

@Composable
fun CameraOpen(
    navigateBack: () -> Unit,
    productUUID: String?
) {

    val productViewModel : ProductViewModel = koinViewModel()
    val context = LocalContext.current
    val directory = getImageDirectory(context)
    val lifecycleOwner = LocalLifecycleOwner.current

    if (directory != null) {
        SimpleCameraPreview(
            navigateBack = navigateBack,
            modifier = Modifier.fillMaxSize(),
            context = context,
            lifecycleOwner = lifecycleOwner,
            outputDirectory = directory,
            onMediaCaptured = { url ->
                if (productUUID != null && url != null) {
                    productViewModel.setProductImageUri(productUUID, url.toString())
                }
                navigateBack()
            }
        )
    } else {
        Log.e("CamerOpen", "Directory is NULL")
    }

}

@Composable
fun SimpleCameraPreview(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    context: Context,
    lifecycleOwner: LifecycleOwner,
    outputDirectory: File,
    onMediaCaptured: (Uri?) -> Unit
) {
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    var imageCapture: ImageCapture? by remember { mutableStateOf(null) }
    var preview by remember { mutableStateOf<Preview?>(null) }
    var viewPort by remember {  mutableStateOf<ViewPort?>(null) }
    var useCaseGroup by remember {  mutableStateOf<UseCaseGroup?>(null) }
    var camera by remember { mutableStateOf<Camera?>(null)   }
    var lensFacing by remember { mutableIntStateOf(CameraSelector.LENS_FACING_BACK) }
    var flashEnabled by remember { mutableStateOf(false) }
    var flashRes by remember { mutableIntStateOf(R.drawable.baseline_flash_off_24) }
    val executor = ContextCompat.getMainExecutor(context)
    var cameraSelector: CameraSelector?
    val cameraProvider = cameraProviderFuture.get()

    Box {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                val previewView = PreviewView(ctx)
                cameraProviderFuture.addListener({
                    viewPort =  ViewPort
                        .Builder(Rational(350, 350), Surface.ROTATION_0)
                        .build()

                    val imageAnalysis = ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build()
                        .apply {
                            setAnalyzer(executor, FaceAnalyzer())
                        }
                    imageCapture = ImageCapture.Builder()
                        .setTargetRotation(previewView.display.rotation)
                        .build()

                    cameraSelector = CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build()

                    useCaseGroup = UseCaseGroup.Builder()
                        .addUseCase(preview!!) //your preview
                        //.addUseCase(imageAnalysis) //if you are using imageAnalysis
                        .addUseCase(imageCapture!!)
                        .setViewPort(viewPort!!)
                        .build()

                    cameraProvider.unbindAll()
                    camera = cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector as CameraSelector,
                        useCaseGroup as UseCaseGroup
                    )

                }, executor)
                preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }
                previewView
            }
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
                .align(Alignment.TopStart)
        ) {
            IconButton(
                onClick = {
                    navigateBack()
                    //Toast.makeText(context, "Back Clicked", Toast.LENGTH_SHORT).show()
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "back arrow",
                    tint = MaterialTheme.colorScheme.surface
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
                .clip(RoundedCornerShape(15.dp))
                //.background(Purple500, RoundedCornerShape(15.dp))
                .padding(8.dp)
                .align(Alignment.BottomCenter)
        ) {
            IconButton(
                onClick = {
                    camera?.let {
                        if (it.cameraInfo.hasFlashUnit()) {
                            if (flashEnabled) {
                                it.cameraControl.enableTorch(false)
                                flashRes = R.drawable.baseline_flash_off_24
                                flashEnabled = !flashEnabled
                            } else {
                                it.cameraControl.enableTorch(true)
                                flashRes = R.drawable.baseline_flash_on_24
                                flashEnabled = !flashEnabled
                            }
                        }
                    }
                    useCaseGroup = UseCaseGroup.Builder()
                        .addUseCase(preview!!) //your preview
                        .addUseCase(imageCapture!!)
                        .setViewPort(viewPort!!)
                        .build()
                }
            ) {
                Icon(
                    painter = painterResource(id = flashRes),
                    contentDescription = "Flash on off",
                    modifier = Modifier.size(35.dp),
                    tint = MaterialTheme.colorScheme.surface
                )
            }

            Button(
                onClick = {
                    val imgCapture = imageCapture ?: return@Button
                    val photoFile = File(
                        outputDirectory,
                        SimpleDateFormat("yyyyMMDD-HHmmss", Locale.US)
                            .format(System.currentTimeMillis()) + ".jpg"
                    )
                    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
                    imgCapture.takePicture(
                        outputOptions,
                        executor,
                        object : ImageCapture.OnImageSavedCallback {
                            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                                onMediaCaptured(Uri.fromFile(photoFile))
                            }

                            override fun onError(exception: ImageCaptureException) {

                                Log.e("ImageCapture", exception.message.toString())
                                navigateBack()
                            }
                        }
                    )
                },
                modifier = Modifier
                    .size(70.dp)
                    //.background(Purple500, CircleShape)
                    .shadow(4.dp, CircleShape)
                    .clip(CircleShape)
                    .border(5.dp, Color.LightGray, CircleShape),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Unspecified),
            ) {

            }

            IconButton(
                onClick = {
                    lensFacing = if (lensFacing == CameraSelector.LENS_FACING_BACK) CameraSelector.LENS_FACING_FRONT
                    else CameraSelector.LENS_FACING_BACK

                    cameraSelector = CameraSelector.Builder()
                        .requireLensFacing(lensFacing)
                        .build()
                    cameraProvider.unbindAll()

                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector as CameraSelector,
                        useCaseGroup as UseCaseGroup
                    )
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_screen_rotation_alt_24),
                    contentDescription = "Change camera",
                    modifier = Modifier.size(35.dp),
                    tint = MaterialTheme.colorScheme.surface
                )
            }
        }
    }
}

private class FaceAnalyzer(): ImageAnalysis.Analyzer {
    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(image: ImageProxy) {
        val imagePic = image.image
        imagePic?.close()
    }
}