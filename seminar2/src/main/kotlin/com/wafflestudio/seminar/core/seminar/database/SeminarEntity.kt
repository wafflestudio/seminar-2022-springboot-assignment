package com.wafflestudio.seminar.core.seminar.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.seminar.api.request.EditSeminarRequest
import com.wafflestudio.seminar.core.seminar.api.response.InstructorSeminarResponse
import com.wafflestudio.seminar.core.seminar.api.response.ParticipantSeminarResponse
import com.wafflestudio.seminar.core.seminar.api.response.SeminarResponse
import com.wafflestudio.seminar.core.user.database.Role
import javax.persistence.*

@Entity
@Table(name="seminars")
class SeminarEntity (
    var name : String,
    var capacity : Int,
    var count : Int,
    var participantCount : Int = 0,
    var time : String,
    var online : Boolean
    ) : BaseTimeEntity() {
    fun toSeminarResponse(userSeminarRepository: UserSeminarRepository) : SeminarResponse {
        val retInstructors = mutableListOf<InstructorSeminarResponse>()
        val retParticipants = mutableListOf<ParticipantSeminarResponse>()
        val userList = userSeminarRepository.findAllBySeminar(this)
        for(userSeminar in userList) {
            if(userSeminar.role == Role.Instructor) retInstructors.add(userSeminar.toInstructorSeminarResponse())
            else retParticipants.add(userSeminar.toParticipantSeminarResponse())
        }
        return SeminarResponse(
            id = id,
            name = name,
            capacity = capacity,
            count = count,
            time = time,
            online = online,
            instructors = retInstructors,
            participants = retParticipants
        )
    }
    
    fun editSeminar(req: EditSeminarRequest) {
        if(req.name != null) name = req.name
        if(req.capacity != null) capacity = req.capacity
        if(req.count != null) count = req.count
        if(req.time != null) time = req.time
        online = req.online
    }
    
}