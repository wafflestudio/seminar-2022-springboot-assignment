package com.wafflestudio.seminar.core.seminar

import com.wafflestudio.seminar.common.Seminar400
import com.wafflestudio.seminar.core.seminar.database.SeminarEntity
import com.wafflestudio.seminar.core.seminar.database.SeminarRepository
import com.wafflestudio.seminar.core.seminar.database.UserSeminarEntity
import com.wafflestudio.seminar.core.seminar.database.UserSeminarRepository
import com.wafflestudio.seminar.core.user.database.UserEntity
import org.springframework.stereotype.Component

@Component
class SeminarTestHelper(
    private val seminarRepository: SeminarRepository,
    private val userSeminarRepository: UserSeminarRepository,
) {
    fun deleteAllSeminar() {
        seminarRepository.deleteAll()
    }
    
    fun createSeminar(
        name: String = "",
        capacity: Int = 100,
        count: Int = 10,
        time: String = "00:00",
        online: Boolean = true,
    ): SeminarEntity {
        return seminarRepository.save(SeminarEntity(name = name, capacity = capacity, count = count, time = time, online = online))
    }
    
    fun joinSeminar(
        seminarEntity: SeminarEntity,
        userEntity: UserEntity,
        role: String,
    ) {
        if (role != "participant" && role != "instructor") throw Seminar400("wrong role")
        val userSeminar = UserSeminarEntity(
            user = userEntity,
            seminar = seminarEntity,
            role = role,
        )

        seminarEntity.userSeminars.add(userSeminar)
        userEntity.userSeminars.add(userSeminar)
        userSeminarRepository.save(userSeminar)
    }
}