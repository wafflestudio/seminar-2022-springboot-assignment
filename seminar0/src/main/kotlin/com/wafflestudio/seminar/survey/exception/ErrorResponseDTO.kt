package com.wafflestudio.seminar.survey.exception

data class ErrorResponseDTO(
    val errorCode: String,
    val message: String?
)