package com.wafflestudio.seminar.core.seminar.api.request

import com.wafflestudio.seminar.common.Seminar400
import com.wafflestudio.seminar.common.Seminar403
import com.wafflestudio.seminar.core.seminar.database.SeminarEntity
import com.wafflestudio.seminar.core.user.database.UserEntity
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalTime


data class CreateSeminarRequest(
    val name: String,
    val capacity: Int,
    val count: Int,
    val time: LocalTime,
    val online: Boolean = true,
) {
    init {
        if (name.isEmpty() || capacity <= 0 || count <= 0) {
            throw Seminar400("이름, 정원, 수업 차수가 잘못되었습니다.")
        }
    }
    
    fun toEntity(creator: UserEntity): SeminarEntity {
        require(creator.isInstructor) {
            throw Seminar403("세미나를 만들 자격이 없습니다.")
        }
        
        return SeminarEntity(
            name = name,
            capacity = capacity,
            count = count,
            time = time,
            online = online,
            creator = creator,
        )
    }
}