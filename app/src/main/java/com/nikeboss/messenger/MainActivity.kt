package com.nikeboss.messenger

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val textView = TextView(this).apply {
            text = "NikeBoss Messenger\n\nГотов к работе!"
            textSize = 24f
            setPadding(50, 50, 50, 50)
            setTextColor(0xFFFF1F3D.toInt())
        }
        setContentView(textView)
    }
}
