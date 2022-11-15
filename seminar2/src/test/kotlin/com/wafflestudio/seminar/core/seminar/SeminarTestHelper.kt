package com.wafflestudio.seminar.core.seminar

import com.wafflestudio.seminar.core.seminar.database.SeminarEntity
import com.wafflestudio.seminar.core.seminar.database.SeminarRepository
import com.wafflestudio.seminar.core.seminar.database.UserSeminarRepository
import org.springframework.stereotype.Component

@Component
class SeminarTestHelper(
    private val seminarRepository: SeminarRepository,
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
}