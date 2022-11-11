package com.wafflestudio.seminar.core.seminar.service

import com.wafflestudio.seminar.common.SeminarException
import com.wafflestudio.seminar.core.UserSeminar.domain.UserSeminarEntity
import com.wafflestudio.seminar.core.UserSeminar.repository.UserSeminarRepository
import com.wafflestudio.seminar.core.seminar.api.request.SeminarRequest
import com.wafflestudio.seminar.core.seminar.domain.SeminarEntity
import com.wafflestudio.seminar.core.seminar.domain.SeminarInstructorDTO
import com.wafflestudio.seminar.core.seminar.repository.SeminarRepository
import com.wafflestudio.seminar.core.user.domain.UserEntity
import com.wafflestudio.seminar.core.user.domain.enums.RoleType
import com.wafflestudio.seminar.core.user.repository.UserRepository
import com.wafflestudio.seminar.global.HibernateQueryCounter
import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull

@SpringBootTest
internal class SeminarServiceTest @Autowired constructor(
        private val userRepository: UserRepository,
        private val seminarRepository: SeminarRepository,
        private val userSeminarRepository: UserSeminarRepository,
        private val seminarService: SeminarService,
        private val queryCounter: HibernateQueryCounter,
        private val seminarTestHelper: SeminarTestHelper,
) {
    
    @AfterEach
    fun clear() {
        userRepository.deleteAll()
        seminarRepository.deleteAll()
        userSeminarRepository.deleteAll()
    }

    /**
     * Testing makeSeminar
     */

    @Test
    fun `Can make seminar when user is instructor, and didn't instruct other seminar`() {
        // given
        val (instructorList, participantList) = initializeUsers()
        
        // when
        val instructor = instructorList[0]
        val (newSeminar, cnt) = queryCounter.count {
            seminarService.makeSeminar(instructor.id, SeminarRequest(
                    name = "unitseminar",
                    capacity = 11,
                    count = 11,
                    time = "11:11",
                    online = true
                )
            ) }
        
        // then
        assertThat(newSeminar.name).isEqualTo("unitseminar")
        assertThat(newSeminar.capacity).isEqualTo(11)
        assertThat(newSeminar.count).isEqualTo(11)
        assertThat(newSeminar.time).isEqualTo("11:11")
        assert(newSeminar.online!!)
        assertThat(seminarRepository.findByIdOrNull(newSeminar.id!!)?.instructor).isEqualTo(instructor.username)
        assertThat(userSeminarRepository.findByUser_IdAndSeminar_Id(instructor.id, newSeminar.id!!)).isNotNull()
    }
    
    @Test
    fun `Throw Exception when user is not INSTRUCTOR`() {
        // given
        val (instructorList, participantList) = initializeUsers()
        
        // when
        val participant = participantList[0]
        
        // then
        assertThrows<SeminarException> {
            seminarService.makeSeminar(
                    userId = participant.id,
                    request = SeminarRequest("", 1, 1, "11:11"))
        }
    }
    
    @Test
    fun `Throw Exception when user is already instructing other seminar`() {
        // given
        val (instructorList, participantList) = initializeUsers()
        val (seminarList, _) = initializeSeminars(instructorList)
        
        // when
        val instructor = instructorList[0]
        
        // then
        assertThrows<SeminarException> {
            seminarService.makeSeminar(
                    userId = instructor.id,
                    request = SeminarRequest("", 1, 1, "11:11"))
        }
    }
    
    
    private fun initializeUsers(): Pair<List<UserEntity>, List<UserEntity>> {
        val instructorList = (0 .. 2).map {i ->
            seminarTestHelper.createInstructor(
                    "inst${i}@inst.com",
                    "instname${i}",
                    "instpassword${i}",
                    company = "company${i}",
                    year = 1998,
            )
        }
        
        val participantList = (3 .. 10).map {i ->
            seminarTestHelper.createParticipant(
                    "part${i}@part.com",
                    "partname${i}",
                    "partpassword${i}",
                    isRegistered = true,
                    university = "SNU${i}",
            )
        }
        
        return Pair(instructorList, participantList)
    }
    
    private fun initializeSeminars(userList: List<UserEntity>): Pair<List<SeminarEntity>, List<UserSeminarEntity>> {
        val seminarList = (0..2).map { i ->
            seminarTestHelper.createSeminar(
                    name = "testseminar${i}",
                    instructor = "instname${i}",
                    capacity = 10 + i.toLong(),
                    count = 10 + i.toLong(),
                    time = "11:1${i}",
                    online = true,
            )
        }
        
        val userSeminarList = (0..2).map {i -> 
            seminarTestHelper.createUserSeminarEntity(userList[i], seminarList[i])
        }
        
        return Pair(seminarList, userSeminarList)
    }
    
}