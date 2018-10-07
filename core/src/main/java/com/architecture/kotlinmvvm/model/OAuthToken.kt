package com.architecture.kotlinmvvm.model

data class OAuthToken(
        override val token: String,
        val refreshToken: String,
        val expireDate: Long
) : Token()