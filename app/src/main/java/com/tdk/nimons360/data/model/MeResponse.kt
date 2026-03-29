package com.tdk.nimons360.data.model

data class MeResponse(
    val data: MeData
)

data class MeData(
    val id: Int,
    val nim: String,
    val email: String,
    val fullName: String,
    val createdAt: String,
    val updatedAt: String
)