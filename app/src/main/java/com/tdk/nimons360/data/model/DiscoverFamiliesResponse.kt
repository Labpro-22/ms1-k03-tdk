package com.tdk.nimons360.data.model


data class DiscoverFamiliesResponse(
    val data: List<DiscoverFamily>
)

data class DiscoverFamily(
    val id: Int,
    val name: String,
    val iconUrl: String,
    val createdAt: String,
    val members: List<DiscoverFamilyMember>
)

data class DiscoverFamilyMember(
    val fullName: String,
    val email: String
)
