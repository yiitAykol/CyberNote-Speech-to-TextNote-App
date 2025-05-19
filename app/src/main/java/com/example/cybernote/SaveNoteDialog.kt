package com.example.cybernote

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.material3.*
import com.example.cybernote.data.Note
import java.util.Calendar

@Composable
fun SaveNoteDialog(
    noteContent: String,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit // Sadece title alacak şekilde düzeltildi
) {
    var title by remember { mutableStateOf("") }
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Notu Kaydet") },
        text = {
            Column(modifier = Modifier.padding(vertical = 8.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Başlık") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "İçerik: $noteContent")
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onSave(title) // Sadece title gönderiliyor
                        Toast.makeText(context, "Not kaydedildi!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Lütfen başlık girin!", Toast.LENGTH_SHORT).show()
                    }
                }
            ) { Text("Kaydet") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("İptal") }
        }
    )
}