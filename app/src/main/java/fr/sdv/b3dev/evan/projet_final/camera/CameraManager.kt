package fr.sdv.b3dev.evan.projet_final.camera

import android.content.Context
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import fr.sdv.b3dev.evan.projet_final.util.Logger
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraManager(private val context: Context) {

    private var cameraProvider: ProcessCameraProvider? = null
    private var imageCapture: ImageCapture? = null
    private var imageAnalysis: ImageAnalysis? = null
    private var barcodeAnalyzer: BarcodeAnalyzer? = null
    private var cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()

    fun startCamera(
        lifecycleOwner: LifecycleOwner,
        previewView: PreviewView,
        enableBarcodeScanner: Boolean,
        onBarcodeDetected: (String) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        ensureExecutor()

        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            try {
                val provider = cameraProviderFuture.get()
                cameraProvider = provider

                val preview = Preview.Builder().build().also {
                    it.surfaceProvider = previewView.surfaceProvider
                }

                imageCapture = ImageCapture.Builder()
                    .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                    .build()

                provider.unbindAll()

                if (enableBarcodeScanner) {
                    barcodeAnalyzer?.close()
                    barcodeAnalyzer = BarcodeAnalyzer(onBarcodeDetected)

                    imageAnalysis = ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build().also { analysis ->
                            analysis.setAnalyzer(cameraExecutor, barcodeAnalyzer!!)
                        }

                    provider.bindToLifecycle(
                        lifecycleOwner,
                        CameraSelector.DEFAULT_BACK_CAMERA,
                        preview,
                        imageCapture,
                        imageAnalysis
                    )
                } else {
                    imageAnalysis?.clearAnalyzer()
                    imageAnalysis = null
                    barcodeAnalyzer?.close()
                    barcodeAnalyzer = null

                    provider.bindToLifecycle(
                        lifecycleOwner,
                        CameraSelector.DEFAULT_BACK_CAMERA,
                        preview,
                        imageCapture
                    )
                }

                Logger.Camera.opened()
            } catch (throwable: Throwable) {
                Logger.Camera.error("Camera start failed", throwable)
                onError(throwable)
            }
        }, ContextCompat.getMainExecutor(context))
    }

    fun takePhoto(
        onPhotoSaved: (String) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val capture = imageCapture
        if (capture == null) {
            onError(IllegalStateException("ImageCapture is not initialized"))
            return
        }

        val file = File(context.filesDir, "profile_${System.currentTimeMillis()}.jpg")
        val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()

        capture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val path = file.absolutePath
                    Logger.Camera.photoTaken(path)
                    onPhotoSaved(path)
                }

                override fun onError(exception: ImageCaptureException) {
                    Logger.Camera.error("Photo capture failed", exception)
                    onError(exception)
                }
            }
        )
    }

    fun release() {
        imageAnalysis?.clearAnalyzer()
        barcodeAnalyzer?.close()
        barcodeAnalyzer = null
        imageAnalysis = null

        cameraProvider?.unbindAll()
        cameraProvider = null

        if (!cameraExecutor.isShutdown) {
            cameraExecutor.shutdown()
        }

        Logger.Camera.closed()
    }

    private fun ensureExecutor() {
        if (cameraExecutor.isShutdown) {
            cameraExecutor = Executors.newSingleThreadExecutor()
        }
    }
}
