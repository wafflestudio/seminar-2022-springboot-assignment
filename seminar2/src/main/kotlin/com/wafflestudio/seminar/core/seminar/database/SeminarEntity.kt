package com.wafflestudio.seminar.core.seminar.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.seminar.api.dto.CreateSeminarResponse
import com.wafflestudio.seminar.core.seminar.domain.SeminarInfo
import java.time.LocalTime
import javax.persistence.*

@Entity
@Table(name = "seminars")
class SeminarEntity(
    val name: String,
    val capacity: Int,
    val count: Int,
    val time: LocalTime,
    val online: Boolean,
) : BaseTimeEntity() {
    @OneToMany(mappedBy = "seminar")
    val participantSet: MutableSet<ParticipantSeminarTableEntity> = mutableSetOf();
    
    @OneToMany(mappedBy = "seminar")
    val instructorSet: MutableSet<InstructorSeminarTableEntity> = mutableSetOf();
    
    fun toCreateSeminarResponse(): CreateSeminarResponse = CreateSeminarResponse(
        id,
        name,
        capacity,
        count,
        time,
        online,
    )
    
    fun toSeminarInfo(): SeminarInfo = SeminarInfo(
        id,
        name,
        // TODO: instructors
    )
}