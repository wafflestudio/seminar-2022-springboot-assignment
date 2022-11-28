package com.wafflestudio.seminar.core.seminar.database

import com.wafflestudio.seminar.common.BaseTimeEntity
import com.wafflestudio.seminar.core.seminar.api.request.UpdateSeminarRequest
import com.wafflestudio.seminar.core.seminar.domain.*
import java.time.LocalTime
import javax.persistence.*

@Entity
@Table(name = "seminars")
class SeminarEntity(
    var name: String,
    var capacity: Int,
    var count: Int,
    var time: LocalTime,
    var online: Boolean,
) : BaseTimeEntity() {
    @OneToMany(mappedBy = "seminar", cascade = [CascadeType.REMOVE])
    val participantSet: MutableSet<ParticipantSeminarTableEntity> = mutableSetOf()

    @OneToMany(mappedBy = "seminar", cascade = [CascadeType.REMOVE])
    val instructorSet: MutableSet<InstructorSeminarTableEntity> = mutableSetOf()

    fun toSeminarDetailInfo(): SeminarDetailInfo {
        val instructorInfoList = mutableListOf<SeminarInstructorInfo>()
        instructorSet.forEach {
            instructorInfoList.add(it.toSeminarInstructorInfo())
        }

        val participantInfoList = mutableListOf<SeminarParticipantInfo>()
        participantSet.forEach {
            participantInfoList.add(it.toSeminarParticipantInfo())
        }

        return SeminarDetailInfo(
            id,
            name,
            capacity,
            count,
            time,
            online,
            instructorInfoList,
            participantInfoList,
        )
    }

    fun updateSeminar(updateSeminarRequest: UpdateSeminarRequest): SeminarDetailInfo {
        name = updateSeminarRequest.name ?: name
        capacity = updateSeminarRequest.capacity ?: capacity
        count = updateSeminarRequest.count ?: count
        time = updateSeminarRequest.time ?: time
        online = updateSeminarRequest.online ?: online

        return this.toSeminarDetailInfo()
    }

    fun toSeminarInfo(): SeminarInfo {
        val instructorInfoList = mutableListOf<SeminarInstructorInfo>()
        instructorSet.forEach {
            instructorInfoList.add(it.toSeminarInstructorInfo())
        }

        return SeminarInfo(
            id,
            name,
            instructorInfoList,
            participantSet.size,
        )
    }
}