package fr.sdv.b3dev.evan.projet_final.camera

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import fr.sdv.b3dev.evan.projet_final.util.Logger

class BarcodeAnalyzer(
    private val onBarcodeDetected: (String) -> Unit
) : ImageAnalysis.Analyzer {

    private val scanner = BarcodeScanning.getClient()
    private var lastValue: String? = null
    private var lastDetectedAt: Long = 0L

    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage == null) {
            imageProxy.close()
            return
        }

        val inputImage = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

        scanner.process(inputImage)
            .addOnSuccessListener { barcodes ->
                val value = barcodes
                    .firstOrNull { !it.rawValue.isNullOrBlank() }
                    ?.rawValue

                if (!value.isNullOrBlank()) {
                    val now = System.currentTimeMillis()
                    if (value != lastValue || now - lastDetectedAt > 1500L) {
                        lastValue = value
                        lastDetectedAt = now
                        onBarcodeDetected(value)
                    }
                }
            }
            .addOnFailureListener { throwable ->
                Logger.Camera.error("Barcode analysis failed", throwable)
            }
            .addOnCompleteListener {
                imageProxy.close()
            }
    }

    fun close() {
        scanner.close()
    }
}
