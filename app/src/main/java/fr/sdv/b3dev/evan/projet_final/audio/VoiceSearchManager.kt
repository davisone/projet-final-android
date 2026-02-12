package fr.sdv.b3dev.evan.projet_final.audio

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import fr.sdv.b3dev.evan.projet_final.util.Logger
import java.util.Locale

class VoiceSearchManager(private val context: Context) {

    private var speechRecognizer: SpeechRecognizer? = null
    private var isListening = false

    fun startListening(
        onResult: (String) -> Unit,
        onError: (String) -> Unit,
        onReady: () -> Unit = {},
        onEnd: () -> Unit = {}
    ) {
        if (!SpeechRecognizer.isRecognitionAvailable(context)) {
            val message = "Recherche vocale indisponible sur cet appareil"
            Logger.VoiceSearch.error(message)
            onError(message)
            return
        }

        val recognizer = speechRecognizer ?: SpeechRecognizer.createSpeechRecognizer(context).also {
            speechRecognizer = it
        }

        recognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                Logger.VoiceSearch.started()
                onReady()
            }

            override fun onBeginningOfSpeech() = Unit

            override fun onRmsChanged(rmsdB: Float) = Unit

            override fun onBufferReceived(buffer: ByteArray?) = Unit

            override fun onEndOfSpeech() {
                isListening = false
                onEnd()
            }

            override fun onError(error: Int) {
                isListening = false
                val message = mapError(error)
                Logger.VoiceSearch.error(message)
                onError(message)
                onEnd()
            }

            override fun onResults(results: Bundle?) {
                isListening = false
                val text = results
                    ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    ?.firstOrNull()
                    ?.trim()
                    .orEmpty()

                if (text.isNotBlank()) {
                    Logger.VoiceSearch.result(text)
                    onResult(text)
                } else {
                    val message = "Aucun résultat vocal"
                    Logger.VoiceSearch.error(message)
                    onError(message)
                }
                onEnd()
            }

            override fun onPartialResults(partialResults: Bundle?) = Unit

            override fun onEvent(eventType: Int, params: Bundle?) = Unit
        })

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, false)
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
        }

        try {
            if (isListening) {
                recognizer.cancel()
            }
            isListening = true
            recognizer.startListening(intent)
        } catch (throwable: Throwable) {
            isListening = false
            val message = "Impossible de démarrer la recherche vocale"
            Logger.VoiceSearch.error(message, throwable)
            onError(message)
            onEnd()
        }
    }

    fun stopListening() {
        if (isListening) {
            speechRecognizer?.stopListening()
            isListening = false
        }
    }

    fun release() {
        speechRecognizer?.cancel()
        speechRecognizer?.destroy()
        speechRecognizer = null
        isListening = false
    }

    private fun mapError(error: Int): String {
        return when (error) {
            SpeechRecognizer.ERROR_AUDIO -> "Erreur audio du micro"
            SpeechRecognizer.ERROR_CLIENT -> "Erreur client de reconnaissance"
            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Permission micro manquante"
            SpeechRecognizer.ERROR_NETWORK,
            SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Erreur réseau de reconnaissance"
            SpeechRecognizer.ERROR_NO_MATCH -> "Aucune correspondance vocale"
            SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Reconnaissance vocale occupée"
            SpeechRecognizer.ERROR_SERVER -> "Erreur serveur de reconnaissance"
            SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "Aucune voix détectée"
            else -> "Erreur inconnue de reconnaissance vocale"
        }
    }
}
