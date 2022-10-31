package com.wafflestudio.seminar.core.user.domain

import com.wafflestudio.seminar.core.user.database.ParticipantProfile

data class Profile (
    val id : Long,
    val username : String,
    val email : String,
    var participant : Participant? = null, 
    var instructor : Instructor? = null
        ){
}