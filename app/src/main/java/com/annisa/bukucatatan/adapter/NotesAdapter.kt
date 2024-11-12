package com.annisa.bukucatatan

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView

class NotesAdapter(
    private val notes: MutableList<Triple<Int, String, String>>, // List of notes
    private val context: Context,
    private val db: NoteDatabaseHelper,
    private val editNoteLauncher: ActivityResultLauncher<Intent> // Edit launcher from MainActivity
) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val contentTextView: TextView = itemView.findViewById(R.id.contentTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_list_item, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val (id, title, content) = notes[position] // Deconstruct the Triple
        holder.titleTextView.text = title
        holder.contentTextView.text = content

        // Handle short click to view details
        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailNoteActivity::class.java).apply {
                putExtra(DetailNoteActivity.EXTRA_TITLE, title)
                putExtra(DetailNoteActivity.EXTRA_CONTENT, content)
            }
            context.startActivity(intent)
        }

        // Handle long click to show options (Edit, Delete)
        holder.itemView.setOnLongClickListener {
            val options = arrayOf("Edit", "Delete")
            AlertDialog.Builder(context)
                .setTitle("Choose an action")
                .setItems(options) { _, which ->
                    when (which) {
                        0 -> {
                            // Edit Note
                            val intent = Intent(context, EditNoteActivity::class.java).apply {
                                putExtra("NOTE_ID", id)
                                putExtra("NOTE_TITLE", title)
                                putExtra("NOTE_CONTENT", content)
                            }
                            editNoteLauncher.launch(intent) // Use editNoteLauncher to start EditNoteActivity
                        }
                        1 -> {
                            // Delete Note
                            AlertDialog.Builder(context)
                                .setMessage("Are you sure you want to delete this note?")
                                .setPositiveButton("Yes") { _, _ ->
                                    db.deleteNote(id)
                                    Toast.makeText(context, "Note deleted", Toast.LENGTH_SHORT).show()
                                    notes.removeAt(position) // Remove item from the list
                                    notifyItemRemoved(position) // Notify adapter
                                }
                                .setNegativeButton("No", null)
                                .show()
                        }
                    }
                }
                .show()
            true // Return true to indicate the long click was handled
        }
    }

    override fun getItemCount(): Int = notes.size
}