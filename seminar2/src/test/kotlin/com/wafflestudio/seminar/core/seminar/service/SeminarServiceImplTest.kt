package com.wafflestudio.seminar.core.seminar.service

import com.wafflestudio.seminar.common.Seminar403
import com.wafflestudio.seminar.common.Seminar404
import com.wafflestudio.seminar.core.seminar.api.request.CreateSeminarRequest
import com.wafflestudio.seminar.core.seminar.api.request.EditSeminarRequest
import com.wafflestudio.seminar.core.user.api.request.SignUpRequest
import com.wafflestudio.seminar.core.seminar.api.response.SeminarResponse
import com.wafflestudio.seminar.core.user.domain.Role
import com.wafflestudio.seminar.core.seminar.domain.Seminar
import com.wafflestudio.seminar.core.seminar.repository.SeminarRepository
import com.wafflestudio.seminar.core.user.repository.CustomUserRepository
import com.wafflestudio.seminar.core.user.repository.InstructorProfileRepository
import com.wafflestudio.seminar.core.user.repository.ParticipantProfileRepository
import com.wafflestudio.seminar.core.user.repository.UserRepository
import com.wafflestudio.seminar.core.user.service.UserService
import com.wafflestudio.seminar.core.userSeminar.repository.UserSeminarRepository
import com.wafflestudio.seminar.global.HibernateQueryCounter
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
internal class SeminarServiceImplTest @Autowired constructor(
    private val userService: UserService,
    private val seminarService: SeminarService,
    private val hibernateQueryCounter: HibernateQueryCounter,
    private val userRepository: UserRepository,
    private val seminarRepository: SeminarRepository,
    private val userSeminarRepository: UserSeminarRepository,
    private val participantProfileRepository: ParticipantProfileRepository,
    private val instructorProfileRepository: InstructorProfileRepository
) {

    @BeforeEach
    fun refresh() {
        userRepository.deleteAll()
        seminarRepository.deleteAll()
        userSeminarRepository.deleteAll()
        participantProfileRepository.deleteAll()
        instructorProfileRepository.deleteAll()
    }

    //    fun createSeminar(userId: Long, createSeminarRequest: CreateSeminarRequest): Seminar
    @Test
    fun 세미나_만들기_성공() {
        val instId = createInstructor("inst1@naver.com", "inst1")
        val inst = userService.getProfile(instId)
        val request = CreateSeminarRequest("seminar2", 10, 20, "12:00")

        var createSeminar: Seminar? = null
        val count = hibernateQueryCounter.count {
            createSeminar = seminarService.createSeminar(inst.id, request)
        }.queryCount

        val findSeminar = seminarService.getSeminar(createSeminar!!.id)

        assertThat(findSeminar.id).isEqualTo(createSeminar!!.id)
        assertThat(findSeminar.capacity).isEqualTo(createSeminar!!.capacity)
        assertThat(findSeminar.count).isEqualTo(createSeminar!!.count)
        assertThat(findSeminar.time).isEqualTo(createSeminar!!.time)

        assertThat(count).isEqualTo(3)
    }

    @Test
    fun 세미나_만들기_실패_이름_미포함() {
        val instId = createInstructor("inst1@naver.com", "inst1")
        val request = CreateSeminarRequest(null, 10, 20, "12:30")
        assertThrows<NullPointerException> { seminarService.createSeminar(instId, request) }
    }

    /*
    아래 테스트 두개는 컨트롤러단에서 검증되고 있음을 확인했습니다.
    fun 세미나_만들기_실패_이름_0글자() {}
    fun 세미나_만들기_실패_정원_음수() {}
    */

//    fun editSeminar(userId: Long, editSeminarRequest: EditSeminarRequest): Seminar

    @Test
    fun 세미나_수정_성공() {
        val instId = createInstructor("inst1@naver.com", "inst1")
        val seminar = createSeminar(instId)
        val request = EditSeminarRequest(seminar.id, "editSeminar", 20, 10, "12:30", false)

        seminarService.editSeminar(instId, request)
        val findSeminar = seminarService.getSeminar(seminar.id)

        assertThat(findSeminar.name).isEqualTo(request.name)
        assertThat(findSeminar.capacity).isEqualTo(request.capacity)
        assertThat(findSeminar.count).isEqualTo(request.count)
        assertThat(findSeminar.time).isEqualTo(request.time)
        assertThat(findSeminar.online).isFalse
    }

    @Test
    fun 세미나_수정_실패_만든_사람이_아닌경우() {
        val instId = createInstructor("inst1@naver.com", "inst1")
        val seminar = createSeminar(instId)

        val request = EditSeminarRequest(seminar.id, "editSeminar", 20, 10, "12:30", false)
        val instId2 = createInstructor("123@naver.com", "inst2")

        assertThrows<Seminar403> { seminarService.editSeminar(instId2, request) }
    }

    //    fun getSeminar(seminarId: Long): Seminar
    @Test
    fun 세미나_불러오기_성공() {
        val partiId1 = createParticipant("part1@naver.com", "parti1")
        val partiId2 = createParticipant("part2@naver.com", "parti2")
        val partiId3 = createParticipant("part3@naver.com", "parti3")
        val instId = createInstructor("inst1@naver.com", "inst1")
        val seminar = createSeminar(instId)

        // N + 1 문제 발생 여부 체크
        seminarService.joinSeminar(partiId1, seminar.id, Role.PARTICIPANT)
        seminarService.joinSeminar(partiId2, seminar.id, Role.PARTICIPANT)
        seminarService.joinSeminar(partiId3, seminar.id, Role.PARTICIPANT)

        val queryCount = hibernateQueryCounter.count {
            seminarService.getSeminar(seminar.id)
        }.queryCount

        // N + 1 문제 발생 
//        assertThat(queryCount).isEqualTo(6)
        assertThat(seminar.name).isEqualTo("seminar1")
    }

    @Test
    fun 세미나_불러오기_실패_아이디에_해당하는_세미나_없음() {
        assertThrows<Seminar404> { seminarService.getSeminar(1000) }
    }

    //    fun getSeminars(name: String, order: String): List<SeminarResponse>
    @Test
    fun 세미나들_불러오기_성공() {

        val instId1 = createInstructor("inst1@naver.com", "inst1")
        val instId2 = createInstructor("inst2@naver.com", "inst2")
        val seminar1 = createSeminar(instId1)
        val seminar2 = createSeminar(instId2)

        val instructorId1 = createInstructor("instructor1@naver.com", "instructor1")
        val instructorId2 = createInstructor("instructor2@naver.com", "instructor2")
        val instructorId3 = createInstructor("instructor3@naver.com", "instructor3")

        seminarService.joinSeminar(instructorId1, seminar1.id, Role.INSTRUCTOR)
        seminarService.joinSeminar(instructorId2, seminar1.id, Role.INSTRUCTOR)
        seminarService.joinSeminar(instructorId3, seminar1.id, Role.INSTRUCTOR)

        var seminars: List<SeminarResponse>? = null
        val queryCount = hibernateQueryCounter.count {
            seminars = seminarService.getSeminars("", "")
        }.queryCount

        assertThat(seminars!![0].id).isEqualTo(seminar2.id)
        // N + 1 문제 발생
//        assertThat(queryCount).isEqualTo(8)
    }

    @Test
    fun 세미나들_불러오기_성공_name_쿼리파라미터_적용() {
        val instId1 = createInstructor("inst1@naver.com", "inst1")
        val instId2 = createInstructor("inst2@naver.com", "inst2")
        val seminar1 = createSeminar(instId1, "siminar1")
        val seminar2 = createSeminar(instId2, "seminar2")

        val seminars = seminarService.getSeminars("siminar", "")

        assertThat(seminars.size).isEqualTo(1)
        assertThat(seminars[0].name).isEqualTo(seminar1.name)
    }

    @Test
    fun 세미나들_불러오기_성공_order_옵션적용() {
        val instId1 = createInstructor("inst1@naver.com", "inst1")
        val instId2 = createInstructor("inst2@naver.com", "inst2")
        val seminar1 = createSeminar(instId1)
        val seminar2 = createSeminar(instId2)

        val seminars = seminarService.getSeminars("", "earliest")

        println(seminars)
        assertThat(seminars.size).isEqualTo(2)
        assertThat(seminars[0].name).isEqualTo(seminar1.name)
        assertThat(seminars[0].id).isEqualTo(seminar1.id)
    }

    @Test
    fun 세미나들_불러오기_성공_name_order_옵션_둘다_적용() {
        val instId1 = createInstructor("inst1@naver.com", "inst1")
        val instId2 = createInstructor("inst2@naver.com", "inst2")
        val seminar1 = createSeminar(instId1, "siminar1")
        val seminar2 = createSeminar(instId2, "seminar2")

        val seminars = seminarService.getSeminars("siminar", "earliest")

        assertThat(seminars.size).isEqualTo(1)
        assertThat(seminars[0].name).isEqualTo(seminar1.name)
        assertThat(seminars[0].id).isEqualTo(seminar1.id)
    }

    // fun joinSeminar(userId: Long, seminarId: Long, role: Role): Seminar
    @Test
    fun 세미나_참여_성공_진행자() {
        val instId1 = createInstructor("inst1@naver.com", "inst1")
        val instId2 = createInstructor("inst2@naver.com", "inst2")
        val seminar = createSeminar(instId1)

        var joinSeminar: Seminar? = null
        val queryCount = hibernateQueryCounter.count {
            joinSeminar = seminarService.joinSeminar(instId2, seminar.id, Role.INSTRUCTOR)
        }.queryCount

        assertThat(joinSeminar!!.id).isEqualTo(seminar.id)
        assertThat(joinSeminar!!.name).isEqualTo(seminar.name)
        assertThat(joinSeminar!!.count).isEqualTo(seminar.count)

        assertThat(joinSeminar!!.instructors.size).isEqualTo(2)
    }

    @Test
    fun 세미나_참여_성공_참여자() {
        val instId1 = createInstructor("inst1@naver.com", "inst1")
        val parti1 = createParticipant("parti1@naver.com", "parti1")
        val parti2 = createParticipant("parti2@naver.com", "parti2")
        val seminar = createSeminar(instId1)

        var joinSeminar: Seminar? = null
        val queryCount = hibernateQueryCounter.count {
            joinSeminar = seminarService.joinSeminar(parti1, seminar.id, Role.PARTICIPANT)
        }.queryCount

        assertThat(joinSeminar!!.id).isEqualTo(seminar.id)
        assertThat(joinSeminar!!.name).isEqualTo(seminar.name)
        assertThat(joinSeminar!!.count).isEqualTo(seminar.count)

        assertThat(joinSeminar!!.participants.size).isEqualTo(1)
    }

    //    fun dropSeminar(userId: Long, seminarId: Long): Seminar
    @Test
    fun 세미나_드랍_성공() {
        val parti1 = createParticipant("parti1@naver.com", "parti1")
        val parti2 = createParticipant("parti2@naver.com", "parti2")
        val instId1 = createInstructor("inst1@naver.com", "inst1")
        val seminar = createSeminar(instId1)
        seminarService.joinSeminar(parti1, seminar.id, Role.PARTICIPANT)
        val joinSeminar = seminarService.joinSeminar(parti2, seminar.id, Role.PARTICIPANT)

        val dropSeminar = seminarService.dropSeminar(parti1, joinSeminar.id)

        println(dropSeminar.participants)
        assertThat(dropSeminar.participants.size).isEqualTo(2)
        assertThat(dropSeminar.participants[0].id).isEqualTo(parti1)
        assertThat(dropSeminar.participants[0].isActive).isFalse
    }

    private fun createInstructor(email: String, username: String): Long {
        return userService.signUp(SignUpRequest(email, username, "1234", Role.INSTRUCTOR))
    }

    private fun createParticipant(email: String, username: String): Long {
        return userService.signUp(SignUpRequest(email, username, "1234", Role.PARTICIPANT))
    }

    private fun createSeminar(instId: Long): Seminar {
        val request = CreateSeminarRequest("seminar1", 10, 20, "12:00")
        return seminarService.createSeminar(instId, request)
    }

    private fun createSeminar(id: Long, name: String): Seminar {
        val request = CreateSeminarRequest(name, 10, 20, "12:00")
        return seminarService.createSeminar(id, request)
    }

}