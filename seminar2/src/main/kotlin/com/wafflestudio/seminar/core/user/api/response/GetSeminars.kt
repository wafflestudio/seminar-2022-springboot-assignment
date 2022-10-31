package com.wafflestudio.seminar.core.user.api.response

data class GetSeminars(
    val id:Long?,  //seminarEntity
    val name: String?,  // seminarEntity
    val capacity: Int?,  // seminarEntity
    val count: Int?,  // seminarEntity
    val time: String?,  // seminarEntity
    val online: Boolean? = true,  // seminarEntity
)