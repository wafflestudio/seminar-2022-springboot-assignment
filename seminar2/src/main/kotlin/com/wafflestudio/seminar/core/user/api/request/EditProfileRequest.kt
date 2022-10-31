package com.wafflestudio.seminar.core.user.api.request

data class EditProfileRequest (
    val university : String?,
    val company : String?,
    val CareerYear : Int?,
    val username : String?,
    )