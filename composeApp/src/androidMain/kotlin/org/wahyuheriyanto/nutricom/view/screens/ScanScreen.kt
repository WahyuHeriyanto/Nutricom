package org.wahyuheriyanto.nutricom.view.screens

import android.Manifest
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.google.common.util.concurrent.ListenableFuture
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import org.wahyuheriyanto.nutricom.viewmodel.ScanViewModel
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@ExperimentalGetImage
@Composable
fun ScanScreen(navController: NavController, viewModel2: ScanViewModel) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val executor = remember { Executors.newSingleThreadExecutor() }
    var scannedBarcode by remember { mutableStateOf<String?>(null) }
    val foodInfoState by viewModel2.foodInfo.collectAsState()

    RequestCameraPermission()

    LaunchedEffect(scannedBarcode) {
        scannedBarcode?.let {
            viewModel2.fetchFoodInfo(it)
//            navController.navigate("result")
        }
    }

    // Navigasi ketika data foodInfo tersedia
    LaunchedEffect(foodInfoState) {
        if (foodInfoState != null) {
            navController.navigate("result")
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Arahkan kamera ke barcode", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        CameraPreview(
            context = context,
            lifecycleOwner = lifecycleOwner,
            executor = executor
        ) { barcode ->
            scannedBarcode = barcode
        }
    }
}

@ExperimentalGetImage
@Composable
fun CameraPreview(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    executor: ExecutorService,
    onBarcodeScanned: (String) -> Unit
) {
    AndroidView(
        factory = { ctx ->
            PreviewView(ctx).apply {
                scaleType = PreviewView.ScaleType.FILL_CENTER
            }
        },
        modifier = Modifier.fillMaxSize(),
        update = { previewView ->
            val cameraProviderFuture: ListenableFuture<ProcessCameraProvider> =
                ProcessCameraProvider.getInstance(context)

            cameraProviderFuture.addListener({
                try {
                    val cameraProvider = cameraProviderFuture.get()
                    val preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }

                    val barcodeScanner = BarcodeScanning.getClient()
                    val imageAnalyzer = ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build()
                        .also { analyzer ->
                            analyzer.setAnalyzer(executor) { imageProxy ->
                                processBarcodeImage(imageProxy, barcodeScanner, onBarcodeScanned)
                            }
                        }

                    val cameraSelector = CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build()

                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageAnalyzer
                    )
                } catch (e: Exception) {
                    Log.e("CameraPreview", "Gagal menginisialisasi kamera", e)
                }
            }, ContextCompat.getMainExecutor(context))
        }
    )
}

@ExperimentalGetImage
private fun processBarcodeImage(
    imageProxy: ImageProxy,
    barcodeScanner: com.google.mlkit.vision.barcode.BarcodeScanner,
    onBarcodeScanned: (String) -> Unit
) {
    val mediaImage = imageProxy.image
    if (mediaImage != null) {
        val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
        barcodeScanner.process(image)
            .addOnSuccessListener { barcodes ->
                for (barcode in barcodes) {
                    barcode.rawValue?.let { scannedBarcode ->
                        onBarcodeScanned(scannedBarcode)
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e("CameraPreview", "Barcode scanning failed", e)
            }
            .addOnCompleteListener {
                imageProxy.close()
            }
    } else {
        imageProxy.close()
    }
}

@Composable
fun RequestCameraPermission() {
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted) {
            Toast.makeText(context, "Izin kamera diperlukan untuk scan barcode", Toast.LENGTH_LONG).show()
        }
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(Manifest.permission.CAMERA)
    }
}
