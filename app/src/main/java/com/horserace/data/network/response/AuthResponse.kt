package com.horserace.data.network.response

import com.horserace.data.db.entity.User

data class AuthResponse(
    val message: String?,
    val user: User?
)
