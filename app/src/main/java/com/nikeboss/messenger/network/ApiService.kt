package com.nikeboss.messenger.network

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

data class AuthRequest(
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String,
    @SerializedName("nickname") val nickname: String? = null
)

data class UserData(
    @SerializedName("id") val id: Int,
    @SerializedName("username") val username: String,
    @SerializedName("nickname") val nickname: String
)

data class AuthResponse(
    @SerializedName("ok") val ok: Boolean,
    @SerializedName("token") val token: String?,
    @SerializedName("user") val user: UserData?,
    @SerializedName("error") val error: String?
)

interface ApiService {
    // Явно указываем, что отправляем JSON
    @Headers("Content-Type: application/json")
    @POST("/api/login")
    fun login(@Body request: AuthRequest): Call<AuthResponse>

    @Headers("Content-Type: application/json")
    @POST("/api/register")
    fun register(@Body request: AuthRequest): Call<AuthResponse>
}
