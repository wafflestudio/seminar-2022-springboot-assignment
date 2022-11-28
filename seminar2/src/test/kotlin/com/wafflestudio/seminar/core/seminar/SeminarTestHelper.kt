package com.wafflestudio.seminar.core.seminar

import com.wafflestudio.seminar.core.seminar.database.*
import com.wafflestudio.seminar.core.user.database.UserEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.time.LocalTime

@Component
internal class SeminarTestHelper @Autowired constructor(
    private val seminarRepository: SeminarRepository,
    private val instructorSeminarTableRepository: InstructorSeminarTableRepository,
) {
    fun createSeminar(
        name: String = "",
        capacity: Int = 10,
        count: Int = 1,
        time: LocalTime = LocalTime.MIDNIGHT,
        online: Boolean = true,
        instructor: UserEntity,
    ): SeminarEntity {
        val seminar = seminarRepository.save(
            SeminarEntity(
                name,
                capacity,
                count,
                time,
                online,
            )
        )

        instructorSeminarTableRepository.save(
            InstructorSeminarTableEntity(
                instructor,
                seminar,
            )
        )

        return seminar
    }
}