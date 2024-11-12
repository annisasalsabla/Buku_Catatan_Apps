package com.annisa.bukucatatan

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.annisa.bukucatatan.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var db: NoteDatabaseHelper
    private lateinit var notesAdapter: NotesAdapter
    private var notesList = mutableListOf<Triple<Int, String, String>>() // Store notes as Triple (ID, Title, Content)

    // Launch AddNoteActivity and refresh notes if a new note was added
    private val addNoteLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                loadNotes() // Refresh notes list from database
            }
        }

    // Launch EditNoteActivity and refresh notes if a note was edited
    private val editNoteLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                loadNotes() // Refresh notes list from database
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = NoteDatabaseHelper(this)

        // Initialize the NotesAdapter with a callback to launch EditNoteActivity
        notesAdapter = NotesAdapter(notesList, this, db, editNoteLauncher) // Passing editNoteLauncher

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = notesAdapter
        }

        loadNotes() // Load notes from the database

        binding.addButton.setOnClickListener {
            addNoteLauncher.launch(Intent(this, AddNoteActivity::class.java))
        }
    }

    private fun loadNotes() {
        // Use Coroutine to perform database operation on a background thread
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Database operation on a background thread
                val notesFromDb = db.getAllNotes()
                withContext(Dispatchers.Main) {
                    // Update UI on the main thread after the operation is complete
                    notesList.clear()
                    notesList.addAll(notesFromDb)
                    notesAdapter.notifyDataSetChanged()
                    Log.d("MainActivity", "Notes loaded successfully: $notesList")
                }
            } catch (e: Exception) {
                Log.e("MainActivity", "Error loading notes", e)
            }
        }
    }
}