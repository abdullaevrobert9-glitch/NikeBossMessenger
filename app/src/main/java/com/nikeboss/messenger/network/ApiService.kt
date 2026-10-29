package com.nikeboss.messenger.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

data class AuthRequest(val username: String, val password: String, val nickname: String? = null)
data class UserData(val id: Int, val username: String, val nickname: String)
data class AuthResponse(val ok: Boolean, val token: String?, val user: UserData?, val error: String?)

interface ApiService {
    @POST("/api/login")
    fun login(@Body request: AuthRequest): Call<AuthResponse>

    @POST("/api/register")
    fun register(@Body request: AuthRequest): Call<AuthResponse>
}
