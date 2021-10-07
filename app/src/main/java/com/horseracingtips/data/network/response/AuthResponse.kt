package com.horseracingtips.data.network.response

import com.horseracingtips.data.db.entity.User

data class AuthResponse(
    val message: String?,
    val user: User?
)
