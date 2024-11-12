package com.annisa.bukucatatan

import android.os.Bundle
import android.widget.Toast
import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import com.annisa.bukucatatan.databinding.ActivityAddNoteBinding

class AddNoteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddNoteBinding
    private lateinit var db: NoteDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = NoteDatabaseHelper(this)

        binding.saveButton.setOnClickListener {
            val title = binding.titleEditText.text.toString()
            val content = binding.contentEditText.text.toString()

            if (title.isNotBlank() && content.isNotBlank()) {
                val result = db.addNote(title, content)
                if (result != -1L) {
                    Toast.makeText(this, "Catatan disimpan", Toast.LENGTH_SHORT).show()
                    setResult(RESULT_OK) // Notify MainActivity to refresh the list
                    finish()
                } else {
                    Toast.makeText(this, "Gagal menyimpan catatan", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Judul dan Konten tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
        }
    }
}