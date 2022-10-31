package com.wafflestudio.seminar.core.seminar.api.request

import com.wafflestudio.seminar.core.seminar.database.SeminarEntity
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.validation.constraints.NotBlank

data class createSeminarRequest (
    @NotBlank
    val name: String,
    @NotBlank
    val capacity: Int,
    @NotBlank
    val count : Int,
    @NotBlank
    val time : String,
    
    val online: Boolean = true
        ){
    fun toSeminar(): SeminarEntity{
        
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
        val localTime = LocalTime.parse(time, formatter)
        
        return SeminarEntity(
            name = name,
            capacity = capacity,
            count = count,
            time = time,
            online = online
        )
    }
}
