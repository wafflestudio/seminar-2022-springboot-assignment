package com.wafflestudio.seminar.core.seminar.api.request

import com.wafflestudio.seminar.core.seminar.database.SeminarEntity

data class EditSeminarRequest (
    val name : String?,
    val capacity : Int?,
    val count : Int?,
    val time : String?,
    val online : Boolean = true
)