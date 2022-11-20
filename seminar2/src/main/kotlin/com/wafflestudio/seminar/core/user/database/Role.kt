package com.wafflestudio.seminar.core.user.database

import com.fasterxml.jackson.annotation.JsonProperty

enum class Role {
    @JsonProperty("Participant")
    Participant,
    @JsonProperty("Instructor")
    Instructor
    
}