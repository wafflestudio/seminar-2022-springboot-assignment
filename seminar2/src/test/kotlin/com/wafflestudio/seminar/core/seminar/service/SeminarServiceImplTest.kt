package com.wafflestudio.seminar.core.seminar.service

import com.wafflestudio.seminar.common.SeminarException
import com.wafflestudio.seminar.core.UserSeminar.repository.UserSeminarRepository
import com.wafflestudio.seminar.core.seminar.api.request.RegisterRequest
import com.wafflestudio.seminar.core.seminar.api.request.SeminarRequest
import com.wafflestudio.seminar.core.seminar.domain.SeminarDTO
import com.wafflestudio.seminar.core.seminar.domain.SeminarEntity
import com.wafflestudio.seminar.core.seminar.repository.SeminarRepository
import com.wafflestudio.seminar.core.user.domain.UserEntity
import com.wafflestudio.seminar.core.user.repository.UserRepository
import com.wafflestudio.seminar.global.HibernateQueryCounter
import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
internal class SeminarServiceImplTest @Autowired constructor(
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
        seminarRepository.deleteAll()
        userRepository.deleteAll()
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
        val newSeminar = seminarService.makeSeminar(instructor.id,
            SeminarRequest(
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
        assertThat(userSeminarRepository.findByUser_IdAndSeminar_Id(instructor.id, newSeminar.id!!)).isNotNull
    }
    
    @Test
    fun `Throw 403 when user is not INSTRUCTOR while creating seminar`() {
        // given
        val (_, participantList) = initializeUsers()
        
        // when
        val participant = participantList[0]
        
        // then
        val exception = assertThrows<SeminarException> {
            seminarService.makeSeminar(
                userId = participant.id,
                request = SeminarRequest("", 1, 1, "11:11"))
        }
        assertEquals(exception.errorCode.httpStatus, HttpStatus.FORBIDDEN)
    }
    
    @Test
    fun `Throw 400 when user is already instructing other seminar while creating seminar`() {
        // given
        val (instructorList, _) = initializeUsers()
        val seminarList = initializeSeminars(instructorList)
        
        // when
        val instructor = instructorList[0]
        
        // then
        val exception = assertThrows<SeminarException> {
            seminarService.makeSeminar(
                userId = instructor.id,
                request = SeminarRequest("", 1, 1, "11:11"))
        }
        assertEquals(exception.errorCode.httpStatus, HttpStatus.BAD_REQUEST)
    }


    /**
     * Testing editSeminar
     */
    // FIXME
    @Test
    fun `Could edit seminar properly`() {
        // given
        val (instructorList, participantList) = initializeUsers()
        val seminarList = initializeSeminars(instructorList)
        
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
    fun `Throw 403 when user is not instructor while editting seminar`() {
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
        val exception = assertThrows<SeminarException> {seminarService.editSeminar(participant.id, edittedSeminarDTO)}
        assertEquals(exception.errorCode.httpStatus, HttpStatus.FORBIDDEN)
    }
    
    @Test
    fun `Throw 403 if seminar's instructor is not user while editting seminar`() {
        // given
        val (instructorList, _) = initializeUsers()
        val seminarList = initializeSeminars(instructorList)
        
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
        val exception = assertThrows<SeminarException> { seminarService.editSeminar(instructor1.id, edittedSeminarDTO) }
        assertEquals(exception.errorCode.httpStatus, HttpStatus.FORBIDDEN)
    }


    /**
     * Test findSeminarContainingWord
     */
    @Test
    @Transactional
    fun `Could find seminar containing word efficiently`() {
        // given
        val (instructorList, _) = initializeUsers()
        val seminarList = initializeSeminars(instructorList)
        val instructor = instructorList[1]
        val seminar = seminarList[1]
        val userSeminar = userSeminarRepository.findByUser_IdAndSeminar_Id(instructor.id, seminar.id)
        
        // when
        val (foundedSeminarList, cnt) = queryCounter.count {
            seminarService.findSeminarsContainingWord("seminar1", order=null)
        }
        
        // then
        assertThat(foundedSeminarList).hasSize(1)
        assertThat(foundedSeminarList[0]).extracting("id").isEqualTo(seminar.id)
        assertThat(foundedSeminarList[0]).extracting("name").isEqualTo(seminar.name)
        assertThat(foundedSeminarList[0]).extracting("participantCount").isEqualTo(0L)
        assertThat(foundedSeminarList[0].instructors).hasSize(1)
        assertThat(foundedSeminarList[0].instructors?.get(0))
                .extracting("id")
                .isEqualTo(instructor.id)
        assertThat(foundedSeminarList[0].instructors?.get(0))
                .extracting("joinedAt")
                .isEqualTo(userSeminar!!.joinedAt)
        
        /* 쿼리 실행 횟수 = 총 2번 (= 조건 충족 세미나 수 + 1)
        - 해당 단어를 이름에 포함하는 세미나 조회   -> 쿼리 1번 실행 : seminarRepository.findSeminarsContainingWord(word, order)
        - 해당 세미나에 참여한/했던 유저 정보 조회  -> 각각 쿼리 1번 실행, 총 세미나 수만큼 실행 : userRepository.findWithProfiles(userIds)
        */
        assertThat(cnt).isEqualTo(2)
    }
    
    @Test
    @Transactional
    fun `Could sort seminar earliest efficiently`() {
        // given
        val (instructorList, _) = initializeUsers()
        val seminarList = initializeSeminars(instructorList)
        
        // when
        val (sortedSeminarList, cnt) = queryCounter.count {
            seminarService.findSeminarsContainingWord(null, order="earliest")
        }
        
        assertThat(sortedSeminarList).isSortedAccordingTo { o1, o2 -> 
            seminarRepository.findByIdOrNull(o1.id)!!.createdAt!!.compareTo(
                    seminarRepository.findByIdOrNull(o2.id)!!.createdAt!!
            )
        }

        /* 쿼리 실행 횟수 = 총 4번 (=세미나 수 + 1)
        - 전체 세미나 조회             -> 쿼리 1번 실행 : seminarRepository.findSeminarsContainingWord(word, order)
        - 세미나별로 참여 유저 정보 조회 -> 각각 쿼리 1번 실행, 총 세미나 수만큼 실행 : userRepository.findWithProfiles(userIds)
        */
        assertThat(cnt).isEqualTo(sortedSeminarList.size + 1)
    }


    /**
     * Test findSeminarById
     */

    @Test
    @Transactional
    fun `Could find seminar by id efficiently`() {
        // given
        val (instructorList, _) = initializeUsers()
        val seminarList = initializeSeminars(instructorList)
        val seminar = seminarList[0]
        
        // when
        val (foundSeminarDTO, cnt) = queryCounter.count{
            seminarService.findSeminarById(seminar.id)
        }
        
        assertThat(foundSeminarDTO).extracting("id").isEqualTo(seminar.id)
        /* 쿼리 실행 횟수 = 총 2번
        - 해당 id를 가진 세미나가 존재하는지 확인           -> 쿼리 1번 실행 : seminarRepository.existsById(seminarId)
        - 존재한다면, 그 세미나 정보 가져와서 DTO로 내보내기  -> 쿼리 1번 실행 : seminarRepository.findSeminarById(seminarId)
        */
        assertThat(cnt).isEqualTo(2)
    }
    
    @Test
    fun `Throw 404 when there is no seminar exists while finding seminar by id`() {
        // given
        // when
        // then
        val exception = assertThrows<SeminarException>{seminarService.findSeminarById(1)}
        assertEquals(exception.errorCode.httpStatus, HttpStatus.NOT_FOUND)
    }


    /**
     * Test registerSeminar
     */
    
    @Test
    fun `Could register seminar as participant`() {
        // given
        val (instructorList, participantList) = initializeUsers()
        val seminarList = initializeSeminars(instructorList)
        val participant = participantList[0]
        val seminar = seminarList[0]
        val request = RegisterRequest(role="PARTICIPANT")
        
        // when
        val seminarDTO = seminarService.registerSeminar(participant.id, seminar.id, request)
        
        // then
        assertThat(seminarDTO.participants).hasSize(1)
        assertThat(seminarDTO.participants!![0]).extracting("id").isEqualTo(participant.id)
    }
    
    @Test
    fun `Could register seminar as instructor`() {
        // given
        val (instructorList, participantList) = initializeUsers()
        val seminarList = initializeSeminars(instructorList)
        
        val boringInstructor = seminarTestHelper.createInstructor(
            "boring@email.com",
            "boring",
            "boringpassword",
            "boringCompany",
            2011
        )
        val seminar = seminarList[0]
        val request = RegisterRequest(role="INSTRUCTOR")

        // when
        val seminarDTO = seminarService.registerSeminar(boringInstructor.id, seminar.id, request)

        // then
        assertThat(seminarDTO.instructors).hasSize(2)
        assertThat(seminarDTO.instructors!![1]).extracting("id").isEqualTo(boringInstructor.id)
    }
    
    @Test
    fun `Throw 404 when there is no seminar exists while registering seminar`() {
        // given
        val (_, _) = initializeUsers()
        // when
        // then
        val exception = assertThrows<SeminarException> { 
            seminarService.registerSeminar(1, 1, RegisterRequest(role="INSTRUCTOR")) 
        }
        assertEquals(exception.errorCode.httpStatus,  HttpStatus.NOT_FOUND)
    }
    
    @Test
    fun `Throw 403 when participant is not active while registering seminar`() {
        // given
        val (instructorList, participantList) = initializeUsers()
        val seminarList = initializeSeminars(instructorList)
        val unregisteredParticipant = seminarTestHelper.createParticipant(
                email =  "a@a.com",
                username = "hh",
                password = "dd",
                university = "NAKSUNG",
                isRegistered = false
        )
        
        // when
        // then
        val exception = assertThrows<SeminarException> {
            seminarService.registerSeminar(
                    unregisteredParticipant.id, seminarList[0].id, RegisterRequest(role="PARTICIPANT")
            )
        }
        assertEquals(exception.errorCode.httpStatus, HttpStatus.FORBIDDEN)
    }
    
    @Test
    fun `Throw 403 when user register with wrong profile while registering seminar`() {
        // given
        val (instructorList, participantList) = initializeUsers()
        val seminarList = initializeSeminars(instructorList)
        
        // when
        val newInstructor = seminarTestHelper.createInstructor(
                email = "a@a.com",
                username = "sd",
                password = "sorryforlate",
                company =  "com",
                year = 2022
        )
        
        // then
        // instructor -> participant
        val exceptionInstructorToParticipant = assertThrows<SeminarException> {
            seminarService.registerSeminar(
                    newInstructor.id, seminarList[0].id, RegisterRequest(role="PARTICIPANT")
            )
        }
        assertEquals(exceptionInstructorToParticipant.errorCode.httpStatus, HttpStatus.FORBIDDEN)
        
        // participant -> instructor
        val exceptionParticipantToInstructor = assertThrows<SeminarException> {
            seminarService.registerSeminar(
                    participantList[0].id, seminarList[0].id, RegisterRequest(role="INSTRUCTOR")
            )
        }
        assertEquals(exceptionParticipantToInstructor.errorCode.httpStatus, HttpStatus.FORBIDDEN)
    }
    
    @Test
    fun `Throw 400 if seminar is full while registering seminar`() {
        // given
        val (instructorList, participantList) = initializeUsers()
        val seminarList = initializeSeminars(instructorList)
        val seminar = seminarRepository.save(seminarList[0].apply { capacity = 0 })
        
        // when
        // then
        val exception = assertThrows<SeminarException> {  
            seminarService.registerSeminar(participantList[0].id, seminar.id, RegisterRequest(role="PARTICIPANT"))
        }
        assertEquals(exception.errorCode.httpStatus, HttpStatus.BAD_REQUEST)
    }
    
    @Test
    fun `Throw 400 if instructor already instruct other seminar while registering seminar`() {
        // given 
        val (instructorList, participantList) = initializeUsers()
        val seminarList = initializeSeminars(instructorList)
        
        // when
        // then
        val exception = assertThrows<SeminarException> {
            seminarService.registerSeminar(instructorList[0].id, seminarList[1].id, RegisterRequest(role="INSTRUCTOR"))
        }
        
        assertEquals(exception.errorCode.httpStatus, HttpStatus.BAD_REQUEST)
    }
    
    @Test
    @Transactional
    fun `Throw 400 if already registered in same seminar while registering seminar`() {
        // given 
        val (instructorList, participantList) = initializeUsers()
        val seminarList = initializeSeminars(instructorList)
        
        // when
        val instructor = instructorList[0]
        val participant = participantList[0]
        val seminar = seminarList[0]
        seminarTestHelper.createUserSeminarEntity(participant, seminar)
        
        // then
        val exceptionInstructorSameSeminar = assertThrows<SeminarException> {
            seminarService.registerSeminar(instructor.id, seminar.id, RegisterRequest(role="INSTRUCTOR"))
        }
        assertEquals(exceptionInstructorSameSeminar.errorCode.httpStatus, HttpStatus.BAD_REQUEST)
        
        val exceptionParticipantSameSeminar = assertThrows<SeminarException> {
            seminarService.registerSeminar(participant.id, seminar.id, RegisterRequest(role="PARTICIPANT"))
        }
        assertEquals(exceptionParticipantSameSeminar.errorCode.httpStatus, HttpStatus.BAD_REQUEST)
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
    
    fun initializeSeminars(userList: List<UserEntity>): List<SeminarEntity> {
        val seminarList = (0..2).map { i ->
            seminarTestHelper.createSeminar(
                    name = "testseminar${i}",
                    instructor = userList[i],
                    capacity = 10 + i.toLong(),
                    count = 10 + i.toLong(),
                    time = "11:1${i}",
                    online = true,
            )
        }
        
        return seminarList
    }
    
}