package com.wafflestudio.seminar.core.seminar.domain

import com.querydsl.core.annotations.QueryProjection
import javax.validation.constraints.Pattern
import javax.validation.constraints.Positive

class SeminarDTO @QueryProjection constructor(
    var id: Long?,         // Seminar id
    var name: String?,     // Seminar name
    @field: Positive(message="세미나 정원은 양수로만 설정할 수 있습니다.")
    var capacity: Long?,   // Seminar capacity
    @field: Positive(message="세미나 횟수는 양수로만 설정할 수 있습니다.")
    var count: Long?,      // Seminar count
    @field:Pattern(
        regexp="[0-9]{2}:[0-9]{2}",
        message="HH:MM 양식에 맞춰 시간을 입력해주세요."
    )
    var time: String?,     // Seminar time
    var online: Boolean?,  // Seminar online

    var instructors: List<SeminarInstructorDTO>? = emptyList(),
    var participants: List<SeminarParticipantDTO>? = emptyList()
) {

    // fetchJoin을 사용하기 위한 method : Entity -> DTO 변환
    companion object {
        fun of(
            seminar: SeminarEntity,
            instructors: List<SeminarInstructorDTO>,
            participants: List<SeminarParticipantDTO>
        ) = seminar.run {
            SeminarDTO(
                id = this.id,
                name = this.name,
                capacity = this.capacity,
                count = this.count,
                time = this.time,
                online = this.online,
                instructors = instructors,
                participants = participants
            )
        }
    }
    
}