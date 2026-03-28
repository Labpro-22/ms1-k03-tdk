package com.tdk.nimons360.data.remote

import com.tdk.nimons360.data.model.LoginRequest
import com.tdk.nimons360.data.model.LoginResponse
import com.tdk.nimons360.data.model.MeResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("api/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    @GET("api/me")
    suspend fun getMe(): Response<MeResponse>
}