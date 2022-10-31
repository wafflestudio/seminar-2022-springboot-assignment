package com.wafflestudio.seminar.core.seminar.api.response

import com.wafflestudio.seminar.core.maptable.SeminarUser
import com.wafflestudio.seminar.core.seminar.database.SeminarEntity
import com.wafflestudio.seminar.core.user.api.request.Role
import java.time.LocalDateTime
import kotlin.streams.toList

data class QuerySeminarResponse (
    val id : Long,
    val name : String,
    val instructors : List <instructorDTO>,
    val participantCount : Int
        ) {
    
    companion object{
        fun toQuerySeminarReseponse(seminarEntity: SeminarEntity): QuerySeminarResponse{
            return QuerySeminarResponse(
                id = seminarEntity.id,
                name = seminarEntity.name,
                instructors = getDTOList(seminarEntity),
                participantCount = seminarEntity.count
            )
        }
        
        private fun getDTOList(seminarEntity: SeminarEntity): List<instructorDTO>{
            return seminarEntity.seminarUser
                .stream()
                .filter{ it.role == Role.Instructors}
                .map(instructorDTO.Companion::toInstructorDTO)
                .toList()
        }
    }
}

data class instructorDTO(
    val id: Long,
    val username: String,
    val email: String,
    val joinedAt : LocalDateTime
) {
    companion object{
        fun toInstructorDTO(seminarUser: SeminarUser?): instructorDTO{
            val user = seminarUser!!.user
            return instructorDTO(
                id = user.id,
                username = user.userName,
                email = user.email,
                joinedAt = seminarUser.createdAt
            )
        }
    }
}