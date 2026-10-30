package com.nikeboss.messenger

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.nikeboss.messenger.data.AppDatabase
import com.nikeboss.messenger.data.Message
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    private lateinit var db: AppDatabase
    private lateinit var messagesLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        db = AppDatabase.getDatabase(this)
        
        val root = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        
        val scrollView = ScrollView(this)
        messagesLayout = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        scrollView.addView(messagesLayout)
        root.addView(scrollView, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f
        ))

        val inputLayout = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
        val input = EditText(this).apply { hint = "Сообщение..." }
        val sendBtn = Button(this).apply { text = "Отправить" }
        
        inputLayout.addView(input, LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f))
        inputLayout.addView(sendBtn)
        root.addView(inputLayout)

        setContentView(root)

        loadMessages()

        sendBtn.setOnClickListener {
            val text = input.text.toString().trim()
            if (text.isNotEmpty()) {
                thread {
                    db.messageDao().insertMessage(Message(text = text, sender = "me"))
                    runOnUiThread {
                        input.text.clear()
                        loadMessages()
                    }
                }
            }
        }
    }

    private fun loadMessages() {
        thread {
            val messages = db.messageDao().getAllMessages()
            runOnUiThread {
                messagesLayout.removeAllViews()
                for (msg in messages) {
                    val textView = TextView(this).apply {
                        text = "${msg.sender}: ${msg.text}"
                        textSize = 16f
                        setPadding(16, 8, 16, 8)
                    }
                    messagesLayout.addView(textView)
                }
            }
        }
    }
}
