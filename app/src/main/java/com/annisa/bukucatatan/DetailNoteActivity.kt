package com.annisa.bukucatatan

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.annisa.bukucatatan.databinding.ActivityDetailNoteBinding

class DetailNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailNoteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mengatur toolbar sebagai ActionBar
        setSupportActionBar(binding.toolbar)

        // Aktifkan tombol kembali di toolbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Notes" // Mengatur judul toolbar, jika diperlukan

        // Ambil data dari Intent
        val title = intent.getStringExtra(EXTRA_TITLE)
        val content = intent.getStringExtra(EXTRA_CONTENT)

        // Tampilkan data title dan content pada TextView
        binding.noteTitle.text = title
        binding.noteContent.text = content

        // Tombol Kembali pada toolbar untuk menutup aktivitas
        binding.toolbar.setNavigationOnClickListener {
            finish()  // Menutup aktivitas dan kembali ke layar sebelumnya
        }
    }

    companion object {
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_CONTENT = "extra_content"
    }
}