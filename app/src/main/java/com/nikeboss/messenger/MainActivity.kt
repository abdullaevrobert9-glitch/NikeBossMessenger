package com.nikeboss.messenger

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    private lateinit var webView: WebView

    // Обработчик выбора файлов (фото, видео, документы)
    private val fileChooserLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            webView.evaluateJavascript("document.getElementById('file-input').onchange(new Event('change'))", null)
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Запрашиваем разрешения на камеру и микрофон
        requestPermissions(arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO), 1)

        webView = WebView(this)
        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            allowFileAccess = true
            allowContentAccess = true
            mediaPlaybackRequiresUserGesture = false
        }

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                return false // Все ссылки открываются внутри приложения
            }
        }

        // Включаем выбор файлов (чтобы работала скрепка)
        webView.webChromeClient = object : WebChromeClient() {
            override fun onShowFileChooser(
                webView: WebView?,
                filePathCallback: ValueCallback<Array<Uri>>?,
                fileChooserParams: FileChooserParams?
            ): Boolean {
                val intent = fileChooserParams?.createIntent()
                if (intent != null) {
                    fileChooserLauncher.launch(intent)
                }
                return true
            }
        }

        webView.loadUrl("https://nikeboss-messenger.duckdns.org")
        setContentView(webView)
    }

    // Обработка кнопки "Назад" на телефоне
    override fun onBackPressed() {
        // Проверяем, находимся ли мы в чате (ищем элемент с id chat-screen)
        webView.evaluateJavascript(
            "(function() { var chat = document.getElementById('chat-screen'); " +
            "if (chat && !chat.classList.contains('hidden')) { " +
            "   closeChat(); return 'was_in_chat'; " +
            "} return 'was_in_list'; })()"
        ) { result ->
            if (result.contains("was_in_chat")) {
                // Если были в чате — просто остались в приложении (функция closeChat уже сработала)
            } else {
                // Если в списке чатов — сворачиваем приложение (или выходим)
                finish()
            }
        }
    }
}
