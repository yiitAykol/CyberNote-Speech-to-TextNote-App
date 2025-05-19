package com.example.cybernote

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cybernote.data.Note
import com.example.cybernote.SaveNoteDialog
import com.example.cybernote.NoteListActivity
import com.example.cybernote.view.NoteViewModel
import java.util.*

class MainActivity : ComponentActivity(), RecognitionListener {

    private lateinit var recognizer: SpeechRecognizer
    private var showSaveDialog by mutableStateOf(false)
    private val _partialText = mutableStateOf("(kısmi sonuçlar burada)")
    private val _finalText = mutableStateOf("(nihai sonuç burada)")

    private val viewModel: NoteViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return NoteViewModel(application) as T
            }
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (!granted) {
            Toast.makeText(
                this,
                "Mikrofon izni gerekli!",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        recognizer = SpeechRecognizer.createSpeechRecognizer(this)
        recognizer.setRecognitionListener(this)

        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MainContent()
                }
            }
        }
    }

    @Composable
    private fun MainContent() {
        val context = LocalContext.current

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { startListening() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Konuşmayı Dinle")
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Partial:", style = MaterialTheme.typography.titleMedium)
            Text(
                text = _partialText.value,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Final:", style = MaterialTheme.typography.titleMedium)
            Text(
                text = _finalText.value,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { showSaveDialog = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Notu Kaydet")
            }

            Button(
                onClick = {
                    startActivity(Intent(context, NoteListActivity::class.java))
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Notları Görüntüle")
            }

            if (showSaveDialog) {
                SaveNoteDialog(
                    noteContent = _finalText.value,
                    onDismiss = { showSaveDialog = false },
                    onSave = { title ->
                        viewModel.insert(
                            Note(
                                title = title.toString(),
                                content = _finalText.value,
                                date = Calendar.getInstance().time
                            )
                        )
                        _finalText.value = "(nihai sonuç burada)"
                        Toast.makeText(context, "Not kaydedildi!", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }

    private fun startListening() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
            == PackageManager.PERMISSION_GRANTED) {
            recognizer.startListening(intent)
        } else {
            requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    override fun onPartialResults(partialResults: Bundle?) {
        partialResults
            ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            ?.firstOrNull()
            ?.let { _partialText.value = it }
    }

    override fun onResults(results: Bundle?) {
        results
            ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            ?.firstOrNull()
            ?.let {
                _finalText.value = it
                showSaveDialog = true
            }
    }

    override fun onError(errorCode: Int) {
        Log.e("SpeechRecognition", "Hata kodu: $errorCode")
        Toast.makeText(this, "Ses tanıma hatası: $errorCode", Toast.LENGTH_SHORT).show()
    }

    override fun onReadyForSpeech(params: Bundle?) {}
    override fun onBeginningOfSpeech() {}
    override fun onRmsChanged(rmsdB: Float) {}
    override fun onBufferReceived(buffer: ByteArray?) {}
    override fun onEndOfSpeech() {}
    override fun onEvent(eventType: Int, params: Bundle?) {}

    override fun onDestroy() {
        super.onDestroy()
        recognizer.destroy()
    }
}