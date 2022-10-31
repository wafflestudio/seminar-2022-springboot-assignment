package com.wafflestudio.seminar.core.seminar.api.request


data class UpdateSeminarRequest (
    val id : Long,
    val name : String,
    val capacity: Int,
    val count : Int,
    val time : String,
    val online : Boolean = true
){

}