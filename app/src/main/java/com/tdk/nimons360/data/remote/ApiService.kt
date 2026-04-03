package com.tdk.nimons360.data.remote

import com.tdk.nimons360.data.model.CreateFamilyRequest
import com.tdk.nimons360.data.model.CreateFamilyResponse
import com.tdk.nimons360.data.model.DiscoverFamiliesResponse
import com.tdk.nimons360.data.model.LoginRequest
import com.tdk.nimons360.data.model.LoginResponse
import com.tdk.nimons360.data.model.MeResponse
import com.tdk.nimons360.data.model.MyFamiliesResponse
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

    @GET("api/me/families")
    suspend fun getMyFamilies(): Response<MyFamiliesResponse>

    @GET("api/families/discover")
    suspend fun getDiscoverFamilies(): Response<DiscoverFamiliesResponse>

    @POST("api/families")
    suspend fun createFamily(
        @Body request: CreateFamilyRequest
    ): Response<CreateFamilyResponse>
}