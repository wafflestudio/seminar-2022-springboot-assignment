package com.wafflestudio.seminar.core.seminar

import com.wafflestudio.seminar.core.maptable.SeminarUser
import com.wafflestudio.seminar.core.maptable.SeminarUserRepository
import com.wafflestudio.seminar.core.seminar.database.SeminarEntity
import com.wafflestudio.seminar.core.seminar.database.SeminarRepository
import com.wafflestudio.seminar.core.user.api.request.Role
import com.wafflestudio.seminar.core.user.database.UserEntity
import com.wafflestudio.seminar.core.user.database.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
internal class SeminarTestHelper @Autowired constructor(
    private val userRepository: UserRepository,
    private val seminarRepository: SeminarRepository,
    private val seminarUserRepository: SeminarUserRepository,
) {
    fun createSeminar(
        name: String = "",
        capacity : Int = 10,
        count : Int = 1,
        time : String = "00:00",
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

        val seminarUser = seminarUserRepository.save(
            SeminarUser(
                seminar,
                instructor,
                Role.Instructors
            )
        )

        instructor.seminarUser.add(seminarUser)
        seminar.seminarUser.add(seminarUser)
        userRepository.save(instructor)
        seminarRepository.save(seminar)

        return seminar
    }
}