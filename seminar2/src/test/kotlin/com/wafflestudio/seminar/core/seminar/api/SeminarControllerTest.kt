package com.wafflestudio.seminar.core.seminar.api

import com.wafflestudio.seminar.core.mappingTable.InstructorProfile
import com.wafflestudio.seminar.core.mappingTable.ParticipantProfile
import com.wafflestudio.seminar.core.seminar.api.request.EditSeminarRequest
import com.wafflestudio.seminar.core.seminar.api.request.JoinSeminarRequest
import com.wafflestudio.seminar.core.seminar.api.request.SeminarMakeRequest
import com.wafflestudio.seminar.core.seminar.api.response.CountSeminarResponse
import com.wafflestudio.seminar.core.seminar.api.response.SeminarResponse
import com.wafflestudio.seminar.core.seminar.repository.SeminarRepository
import com.wafflestudio.seminar.core.seminar.repository.UserSeminarRepository
import com.wafflestudio.seminar.core.seminar.service.SeminarService
import com.wafflestudio.seminar.core.user.Role
import com.wafflestudio.seminar.core.user.repository.UserEntity
import com.wafflestudio.seminar.core.user.repository.UserRepository
import com.wafflestudio.seminar.global.HibernateQueryCounter
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull

@SpringBootTest
internal class SeminarControllerTest @Autowired constructor(
    private val seminarController: SeminarController,
    private val seminarService: SeminarService,
    private val userRepository: UserRepository,
    private val userSeminarRepository: UserSeminarRepository,
    private val seminarRepository: SeminarRepository,
    private val hibernateQueryCounter: HibernateQueryCounter
) {

    @BeforeEach
    fun refresh() {
        userRepository.deleteAll()
        seminarRepository.deleteAll()
    }

    @Test
    fun 세미나_만들기() {
        //given
        val user = createInstructor("a@naver.com")
        val seminarMakeRequest = createSeminarMakeRequest("Spring")

        //when
        var seminarId = 0L
        val queryCount = hibernateQueryCounter.count {
            seminarId = seminarController.makeSeminar(user, seminarMakeRequest).id!!
        }.queryCount

        //then
        val seminar = seminarRepository.findByIdOrNull(seminarId) ?: fail("Creating seminar failed")
        assertThat(seminar.name).isEqualTo(seminarMakeRequest.name)
        assertThat(seminar.capacity).isEqualTo(seminarMakeRequest.capacity)
        assertThat(seminar.count).isEqualTo(seminarMakeRequest.count)
        assertThat(seminar.time).isEqualTo(seminarMakeRequest.time)
        assertThat(seminar.online).isEqualTo(seminarMakeRequest.online)
        val userSeminar = userSeminarRepository.findByUserEntityIdAndSeminarId(user.id, seminar.id).get()
        assertThat(userSeminar.role).isEqualTo(Role.Instructor)

        //기존에 instructing 하는 세미나가 있는지 조회 쿼리 1
        //userSeminarRepository, seminarRepository 저장 쿼리 2
        assertThat(queryCount).isEqualTo(3)
    }

    @Test
    fun 세미나_수정() {
        //given
        val user = createInstructor("a@naver.com")
        seminarService.makeSeminar(user, createSeminarMakeRequest("Spring"))
        val editSeminarRequest = EditSeminarRequest("New name", 123, 8, time = "12:30")

        //when
        var seminarId = 0L
        val queryCount = hibernateQueryCounter.count {
            seminarId = seminarController.editSeminar(user, editSeminarRequest).id!!
        }.queryCount

        //then
        val seminar = seminarRepository.findById(seminarId).get()
        assertThat(seminar.name).isEqualTo(editSeminarRequest.name)
        assertThat(seminar.count).isEqualTo(editSeminarRequest.count)
        assertThat(seminar.capacity).isEqualTo(editSeminarRequest.capacity)
        assertThat(seminar.online).isEqualTo(editSeminarRequest.online)

        //수정가능한 세미나 있는지 조회 쿼리 1
        //세미나 업데이트 쿼리 1
        assertThat(queryCount).isEqualTo(2)
    }

    @Test
    fun 세미나_하나_불러오기() {
        //given
        val instructor = createInstructor("b@naver.com")
        val seminar = seminarService.makeSeminar(instructor, createSeminarMakeRequest("Spring"))

        //when

        //단순초기화
        var seminarResponse = seminar

        val queryCount = hibernateQueryCounter.count {
            seminarResponse = seminarController.getSeminarById(seminar.id!!)
        }.queryCount

        //then
        val seminarFound = seminarRepository.findById(seminar.id!!).get()
        assertThat(seminarResponse.capacity).isEqualTo(seminarFound.capacity)
        assertThat(seminarResponse.name).isEqualTo(seminarFound.name)
        assertThat(seminarResponse.count).isEqualTo(seminarFound.count)
        assertThat(seminarResponse.time).isEqualTo(seminarFound.time)
        assertThat(seminarResponse.online).isEqualTo(seminarFound.online)

        //세미나 조회 쿼리 1
        assertThat(queryCount).isEqualTo(1)
    }

    @Test
    fun 키워드로_세미나_불러오기() {
        //given
        createSeminars()

        //when
        var seminarList = listOf<CountSeminarResponse>()
        val queryCount = hibernateQueryCounter.count {
            seminarList = seminarController.getSeminarByQuery("1", "")
        }.queryCount

        //then
        for (seminar in seminarList) {
            assertThat(seminar.name).contains("1")
        }

        //세미나 조회 쿼리 1
        assertThat(queryCount).isEqualTo(1)
    }

    @Test
    fun 오래된순으로_세미나_불러오기() {
        //given
        createSeminars()

        //when
        var seminarList = listOf<CountSeminarResponse>()
        val queryCount = hibernateQueryCounter.count {
            seminarList = seminarController.getSeminarByQuery("", "earliest")
        }.queryCount

        //then
        for (i in 0..seminarList.size - 2) {
            assertThat(seminarRepository.findById(seminarList[i].id).get().createdAt)
                .isBeforeOrEqualTo(seminarRepository.findById(seminarList[i + 1].id).get().createdAt)
        }

        //세미나 조회 쿼리 1
        assertThat(queryCount).isEqualTo(1)
    }

    @Test
    fun 키워드랑_오래된순으로_세미나_불러오기() {
        //given
        createSeminars()

        //when
        var seminarList = listOf<CountSeminarResponse>()
        val queryCount = hibernateQueryCounter.count {
            seminarList = seminarController.getSeminarByQuery("2", "earliest")
        }.queryCount

        //then
        for (i in 0..seminarList.size - 2) {
            assertThat(seminarRepository.findById(seminarList[i].id).get().createdAt)
                .isBeforeOrEqualTo(seminarRepository.findById(seminarList[i + 1].id).get().createdAt)
        }
        for (seminar in seminarList) {
            assertThat(seminar.name).contains("2")
        }

        //세미나 조회 쿼리1
        assertThat(queryCount).isEqualTo(1)
    }

    @Test
    fun 수강생_세미나_참여하기() {
        //given
        val participant = createParticipant("a@naver.com")
        val instructor = createInstructor("b@naver.com")
        val seminar = seminarService.makeSeminar(instructor, createSeminarMakeRequest("Spring"))
        val joinSeminarRequest = JoinSeminarRequest("Participant")

        //when
        val queryCount = hibernateQueryCounter.count {
            seminarController.joinSeminar(participant, joinSeminarRequest, seminar.id!!)
        }.queryCount

        //then
        val seminarEntity = seminarRepository.findById(seminar.id!!).get()
        val userSeminar = userSeminarRepository.findByUserEntityIdAndSeminarId(participant.id, seminarEntity.id).get()
        assertThat(userSeminar.role).isEqualTo(Role.Participant)

        //유저, 세미나 조회 쿼리 2
        //이미 참여중이거나 드랍했던 세미나인지 userSeminar 조회 쿼리 1
        //세미나 업데이트 쿼리 1
        //userSeminar 저장 쿼리 1
        assertThat(queryCount).isEqualTo(5)
    }

    @Test
    fun 진행자_세미나_진행하기() {
        //given
        val instructor = createInstructor("b@naver.com")
        val seminarMaker = createInstructor("c@naver.com")
        val seminar = seminarService.makeSeminar(seminarMaker, createSeminarMakeRequest("Spring"))
        val joinSeminarRequest = JoinSeminarRequest("Instructor")

        //when
        val queryCount = hibernateQueryCounter.count {
            seminarController.joinSeminar(instructor, joinSeminarRequest, seminar.id!!)
        }.queryCount

        //then
        val seminarEntity = seminarRepository.findById(seminar.id!!).get()
        val userSeminar = userSeminarRepository.findByUserEntityIdAndSeminarId(instructor.id, seminarEntity.id).get()
        assertThat(userSeminar.role).isEqualTo(Role.Instructor)

        //유저, 세미나 조회 쿼리 2
        //이미 참여중인지 userSeminar 조회 쿼리 1
        //진행중인 다른 세미나가 있는지 userSeminar 조회 쿼리 1
        //userSeminar 저장 쿼리 1
        assertThat(queryCount).isEqualTo(5)
    }

    @Test
    fun 세미나_드랍하기() {
        //given
        val participant = createParticipant("a@naver.com")
        val instructor = createInstructor("b@naver.com")
        val seminar = seminarService.makeSeminar(instructor, createSeminarMakeRequest("Spring"))
        seminarService.joinSeminar(participant, "Participant", seminar.id!!)

        //when
        val queryCount = hibernateQueryCounter.count {
            seminarController.dropSeminar(participant, seminar.id!!)
        }.queryCount

        //then
        val seminarEntity = seminarRepository.findById(seminar.id!!).get()
        val userSeminar = userSeminarRepository.findByUserEntityIdAndSeminarId(participant.id, seminarEntity.id).get()
        assertThat(userSeminar.isActive).isFalse
        assertThat(userSeminar.droppedAt).isNotNull

        //유저, 세미나, 유저세미나 조회 쿼리 3
        //유저세미나, 세미나 업데이트 쿼리 2
        assertThat(queryCount).isEqualTo(5)
    }

    private fun createInstructor(email: String): UserEntity {
        val user = UserEntity(
            "A", email, "1",
            instructor = InstructorProfile("waffle", 5)
        )
        return userRepository.save(user)
    }

    private fun createParticipant(email: String): UserEntity {
        val user = UserEntity(
            "B", email, "2",
            participant = ParticipantProfile("SNU", true)
        )
        return userRepository.save(user)
    }

    private fun createSeminarMakeRequest(name: String): SeminarMakeRequest {
        return SeminarMakeRequest(name, 40, 5, "12:00")
    }

    private fun createSeminars(): List<SeminarResponse> {
        val instructors = (1..20).map { createInstructor("ins@tructor#$it.com") }
        return instructors.map { seminarService.makeSeminar(it, createSeminarMakeRequest("Spring${it.id}")) }
    }
}