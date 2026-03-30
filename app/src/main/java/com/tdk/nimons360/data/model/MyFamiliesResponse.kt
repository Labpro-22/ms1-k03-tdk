package com.tdk.nimons360.data.model

data class MyFamiliesResponse(
    val data: List<MyFamily>
)

data class MyFamily(
    val id: Int,
    val name: String,
    val iconUrl: String,
    val familyCode: String,
    val createdAt: String,
    val updatedAt: String,
    val members: List<MyFamilyMember>
)

data class MyFamilyMember(
    val id: Int,
    val fullName: String,
    val email: String,
    val joinedAt: String
)
