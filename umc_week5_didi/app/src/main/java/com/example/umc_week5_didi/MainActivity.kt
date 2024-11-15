package com.example.umc_week5_didi

import android.R
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity

class MainActivity : AppCompatActivity() {
    private var editTextNote: EditText? = null
    private var savedNote = ""

    protected fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextNote = findViewById(R.id.editTextNote)
        val buttonNext: Button = findViewById(R.id.buttonNext)

        buttonNext.setOnClickListener { v: View? ->
            val intent: Intent =
                Intent(
                    this@MainActivity,
                    ConfirmActivity::class.java
                )
            intent.putExtra("note", editTextNote!!.text.toString())
            startActivity(intent)
        }
    }

    protected fun onResume() {
        super.onResume()
        if (!savedNote.isEmpty()) {
            editTextNote!!.setText(savedNote)
        }
    }

    protected fun onPause() {
        super.onPause()
        savedNote = editTextNote!!.text.toString()
    }

    protected fun onRestart() {
        super.onRestart()
        AlertDialog.Builder(this)
            .setTitle("다시 작성할까요?")
            .setPositiveButton(
                "예"
            ) { dialog: DialogInterface?, which: Int -> }
            .setNegativeButton(
                "아니오"
            ) { dialog: DialogInterface?, which: Int ->
                savedNote = ""
            }
            .show()
    }
}

