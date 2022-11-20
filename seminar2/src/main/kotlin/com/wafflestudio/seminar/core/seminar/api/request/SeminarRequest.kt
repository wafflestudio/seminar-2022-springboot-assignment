package com.wafflestudio.seminar.core.seminar.api.request

import com.wafflestudio.seminar.core.seminar.database.SeminarEntity

data class SeminarRequest (
    val name : String,
    val capacity : Int,
    val count : Int,
    val time : String,
    val online : Boolean = true
        ) {
    
    fun toSeminarEntity() : SeminarEntity {
        return SeminarEntity(
            name = name,
            capacity = capacity,
            count = count,
            time = time,
            online = online
        )
    }
}