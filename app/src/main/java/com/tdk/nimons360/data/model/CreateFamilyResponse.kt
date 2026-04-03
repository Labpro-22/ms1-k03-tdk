package com.tdk.nimons360.data.model

data class CreateFamilyResponse(
    val data: CreatedFamilyData
)

data class CreatedFamilyData(
    val id: Int,
    val name: String,
    val iconUrl: String,
    val familyCode: String,
    val createdAt: String,
    val updatedAt: String
)
