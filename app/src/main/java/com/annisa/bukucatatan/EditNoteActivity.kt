package com.annisa.bukucatatan

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.annisa.bukucatatan.databinding.ActivityEditNoteBinding

class EditNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditNoteBinding
    private lateinit var db: NoteDatabaseHelper
    private var noteId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = NoteDatabaseHelper(this)

        // Retrieve note details from the intent
        noteId = intent.getIntExtra("NOTE_ID", -1)
        val noteTitle = intent.getStringExtra("NOTE_TITLE") ?: ""
        val noteContent = intent.getStringExtra("NOTE_CONTENT") ?: ""

        // Set note details in the edit fields
        binding.titleEditText.setText(noteTitle)
        binding.contentEditText.setText(noteContent)

        binding.saveButton.setOnClickListener {
            val newTitle = binding.titleEditText.text.toString()
            val newContent = binding.contentEditText.text.toString()

            // Check for empty title/content
            if (newTitle.isNotBlank() && newContent.isNotBlank()) {
                val updatedRows = db.updateNote(noteId, newTitle, newContent)
                if (updatedRows > 0) {
                    Toast.makeText(this, "Note updated successfully!", Toast.LENGTH_SHORT).show()

                    // Set the result to indicate a successful update
                    setResult(RESULT_OK)
                    finish()  // Close the activity and go back to MainActivity
                } else {
                    Toast.makeText(this, "Failed to update the note.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Title and Content cannot be empty.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}