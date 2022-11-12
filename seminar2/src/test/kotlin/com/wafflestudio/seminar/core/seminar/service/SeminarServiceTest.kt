package com.wafflestudio.seminar.core.seminar.service

import com.wafflestudio.seminar.common.SeminarException
import com.wafflestudio.seminar.core.UserSeminar.domain.UserSeminarEntity
import com.wafflestudio.seminar.core.UserSeminar.repository.UserSeminarRepository
import com.wafflestudio.seminar.core.seminar.api.request.SeminarRequest
import com.wafflestudio.seminar.core.seminar.domain.SeminarDTO
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
import javax.transaction.Transactional

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
        userSeminarRepository.deleteAll()
        userRepository.deleteAll()
        seminarRepository.deleteAll()
    }

    /**
     * Testing makeSeminar
     */

    @Test
    fun `Could make seminar when user is instructor, and didn't instruct other seminar`() {
        // given
        val (instructorList, participantList) = initializeUsers()
        
        // when
        val instructor = instructorList[0]
        val newSeminar = seminarService.makeSeminar(instructor.id, SeminarRequest(
                    name = "unitseminar",
                    capacity = 11,
                    count = 11,
                    time = "11:11",
                    online = true
                )
            )
        
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
    fun `Throw Exception when user is not INSTRUCTOR while creating seminar`() {
        // given
        val (_, participantList) = initializeUsers()
        
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
    fun `Throw Exception when user is already instructing other seminar while creating seminar`() {
        // given
        val (instructorList, _) = initializeUsers()
        val (_, _) = initializeSeminars(instructorList)
        
        // when
        val instructor = instructorList[0]
        
        // then
        assertThrows<SeminarException> {
            seminarService.makeSeminar(
                    userId = instructor.id,
                    request = SeminarRequest("", 1, 1, "11:11"))
        }
    }


    /**
     * Testing editSeminar
     */
    // FIXME
    @Transactional
    @Test
    fun `Could edit seminar properly`() {
        // given
        val (instructorList, participantList) = initializeUsers()
        val (seminarList, userSeminarList) = initializeSeminars(instructorList)
        
        // when
        val instructor = instructorList[0]
        val originalSeminar = seminarList[0]
        val edittedSeminarDTO = SeminarDTO(
                id = originalSeminar.id,
                name = "editseminar",
                capacity = 111,
                count = 111,
                time = "22:22",
                online = false
        )
        
        val edittedSeminarReturn = seminarService.editSeminar(instructor.id, edittedSeminarDTO)

        // then
        assertThat(edittedSeminarReturn.id).isEqualTo(edittedSeminarDTO.id)
        assertThat(edittedSeminarReturn.name).isEqualTo(edittedSeminarDTO.name)
        assertThat(edittedSeminarReturn.capacity).isEqualTo(edittedSeminarDTO.capacity)
        assertThat(edittedSeminarReturn.count).isEqualTo(edittedSeminarDTO.count)
        assertThat(edittedSeminarReturn.time).isEqualTo(edittedSeminarDTO.time)
        assertThat(edittedSeminarReturn.online).isEqualTo(edittedSeminarDTO.online)
    }
    
    @Test
    fun `Throw Exception when user is not instructor while editting seminar`() {
        // given
        val (_, participantList) = initializeUsers()
        
        // when
        val participant = participantList[0]
        val edittedSeminarDTO = SeminarDTO(
                id = 1,
                name = "editseminar",
                capacity = 111,
                count = 111,
                time = "22:22",
                online = false
        )
        
        // then
        assertThrows<SeminarException> {seminarService.editSeminar(participant.id, edittedSeminarDTO)}
    }
    
    @Test
    fun `Throw Exception if seminar's instructor is not user while editting seminar`() {
        // given
        val (instructorList, _) = initializeUsers()
        val (seminarList, _) = initializeSeminars(instructorList)
        
        // when
        val instructor1 = instructorList[1]
        val seminar0 = seminarList[0]
        val edittedSeminarDTO = SeminarDTO(
                id = seminar0.id,
                name = "editseminar",
                capacity = 111,
                count = 111,
                time = "22:22",
                online = false
        )
        
        // then
        assertThrows<SeminarException> { seminarService.editSeminar(instructor1.id, edittedSeminarDTO) }
    }


    /**
     * Test findSeminarContainingWord
     */
    @Test
    fun `Could find seminar containing word efficiently`() {
        // given
        val (instructorList, _) = initializeUsers()
        val (seminarList, userSeminarList) = initializeSeminars(instructorList)
        val instructor = instructorList[1]
        val seminar = seminarList[1]
        val userSeminar = userSeminarList[1]
        
        // when
        val (foundedSeminarList, cnt) = queryCounter.count {
            seminarService.findSeminarsContainingWord("seminar1", order=null)
        }
        
        // then
        assertThat(foundedSeminarList).hasSize(1)
        assertThat(foundedSeminarList[0]).extracting("id").isEqualTo(seminar.id)
        assertThat(foundedSeminarList[0]).extracting("name").isEqualTo(seminar.name)
        assertThat(foundedSeminarList[0]).extracting("participantCount").isEqualTo(0)
        assertThat(foundedSeminarList[0].instructors).hasSize(1)
        assertThat(foundedSeminarList[0].instructors?.get(0))
                .extracting("id")
                .isEqualTo(instructor.id)
        assertThat(foundedSeminarList[0].instructors?.get(0))
                .extracting("joinedAt")
                .isEqualTo(userSeminar.joinedAt)
        assertThat(cnt).isLessThanOrEqualTo(seminarList.size)
    }
    
    @Test
    fun `Could sort seminar eariliest efficiently`() {
        // given
        val (instructorList, _) = initializeUsers()
        val (seminarList, _) = initializeSeminars(instructorList)
        
        // when
        val (sortedSeminarList, cnt) = queryCounter.count {
            seminarService.findSeminarsContainingWord(null, order="earliest")
        }
        
        assertThat(sortedSeminarList).isSortedAccordingTo { o1, o2 -> 
            seminarRepository.findByIdOrNull(o1.id)!!.createdAt!!.compareTo(
                    seminarRepository.findByIdOrNull(o2.id)!!.createdAt!!
            )
        }
        
        assertThat(cnt).isLessThanOrEqualTo(seminarList.size)
    }


    fun initializeUsers(): Pair<List<UserEntity>, List<UserEntity>> {
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
    
    fun initializeSeminars(userList: List<UserEntity>): Pair<List<SeminarEntity>, List<UserSeminarEntity>> {
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