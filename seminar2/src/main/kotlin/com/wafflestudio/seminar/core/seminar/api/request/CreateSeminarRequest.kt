package com.wafflestudio.seminar.core.seminar.api.request

import java.util.*

data class CreateSeminarRequest (
    val name : String,
    val capacity: Int,
    val count : Int,
    val time : String,
    val online : Boolean = true
    ){
    
}