package com.wafflestudio.seminar.core.seminar

import com.wafflestudio.seminar.core.seminar.database.*
import com.wafflestudio.seminar.core.user.database.UserEntity
import com.wafflestudio.seminar.core.user.database.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalTime

@Component
internal class SeminarTestHelper @Autowired constructor(
    private val userRepository: UserRepository,
    private val seminarRepository: SeminarRepository,
    private val instructorSeminarTableRepository: InstructorSeminarTableRepository,
) {
    fun createSeminar(
        name: String = "",
        capacity : Int = 10,
        count : Int = 1,
        time : LocalTime = LocalTime.MIDNIGHT,
        online : Boolean = true,
        instructor: UserEntity,
    ): SeminarEntity {
        val seminar =  seminarRepository.save(
            SeminarEntity(
                name,
                capacity,
                count,
                time,
                online,
            )
        )
        
        val table = instructorSeminarTableRepository.save(
            InstructorSeminarTableEntity(
                instructor,
                seminar,
            )
        )
        
//        instructor.instructingSeminars.add(table)
//        seminar.instructorSet.add(table)
//        userRepository.save(instructor)
//        seminarRepository.save(seminar)

        return seminar
    }
}