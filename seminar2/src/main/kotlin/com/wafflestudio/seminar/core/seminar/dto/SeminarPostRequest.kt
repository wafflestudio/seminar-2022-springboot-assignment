package com.wafflestudio.seminar.core.seminar.dto

import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive

data class SeminarPostRequest(
        @field: NotEmpty(message = "Name should not be empty")
        val name: String = "",

        @field: NotNull(message = "Capacity should not be null")
        @field: Positive(message = "Capacity should be positive integer.")
        val capacity: Int? = null,

        @field: Positive(message = "Count should be positive integer.")
        @field: NotNull(message = "Count should not be null")
        val count: Int? = null,
        
        @field: NotEmpty(message = "Time should not be empty")
        val time: String? = null,
        
        val online: Boolean = true,
) {
}