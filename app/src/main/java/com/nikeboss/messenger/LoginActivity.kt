package com.nikeboss.messenger

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.nikeboss.messenger.databinding.ActivityLoginBinding
import com.nikeboss.messenger.network.ApiService
import com.nikeboss.messenger.network.AuthRequest
import com.nikeboss.messenger.network.AuthResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://nikeboss-messenger.duckdns.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Если уже вошли (есть токен), сразу на главный экран
        val prefs = getSharedPreferences("NikeBossPrefs", MODE_PRIVATE)
        if (prefs.getString("token", null) != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        binding.loginButton.setOnClickListener {
            val username = binding.usernameInput.text.toString().trim()
            val password = binding.passwordInput.text.toString()
            if (username.isNotEmpty() && password.isNotEmpty()) {
                performAuth("/api/login", username, password, null)
            } else {
                Toast.makeText(this, "Заполни все поля", Toast.LENGTH_SHORT).show()
            }
        }

        binding.registerButton.setOnClickListener {
            val username = binding.usernameInput.text.toString().trim()
            val password = binding.passwordInput.text.toString()
            if (username.isNotEmpty() && password.isNotEmpty()) {
                performAuth("/api/register", username, password, username)
            } else {
                Toast.makeText(this, "Заполни все поля", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun performAuth(endpoint: String, username: String, password: String, nickname: String?) {
        binding.progressBar.visibility = View.VISIBLE
        val request = AuthRequest(username, password, nickname)
        
        val call = if (endpoint == "/api/login") apiService.login(request) else apiService.register(request)
        
        call.enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                binding.progressBar.visibility = View.GONE
                if (response.isSuccessful && response.body()?.ok == true) {
                    val token = response.body()?.token
                    val user = response.body()?.user
                    
                    // Сохраняем токен
                    val prefs = getSharedPreferences("NikeBossPrefs", MODE_PRIVATE)
                    prefs.edit().putString("token", token).apply()
                    
                    Toast.makeText(this@LoginActivity, "Успешно!", Toast.LENGTH_SHORT).show()
                    
                    // Переходим на главный экран (WebView)
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                } else {
                    val error = response.body()?.error ?: "Ошибка сервера"
                    Toast.makeText(this@LoginActivity, error, Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this@LoginActivity, "Ошибка сети: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}
