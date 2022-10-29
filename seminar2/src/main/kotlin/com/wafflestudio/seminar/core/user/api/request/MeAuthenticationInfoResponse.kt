package com.wafflestudio.seminar.core.user.api.request


data class MeAuthenticationInfoResponse(
        val message: String = "You are authenticated!",
        val username: String,
        val email: String
)