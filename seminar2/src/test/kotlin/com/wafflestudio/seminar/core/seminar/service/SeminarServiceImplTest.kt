package com.wafflestudio.seminar.core.seminar.service

import com.wafflestudio.seminar.core.seminar.api.request.*
import com.wafflestudio.seminar.core.seminar.database.*
import com.wafflestudio.seminar.core.user.UserTestHelper
import com.wafflestudio.seminar.core.user.database.UserEntity
import com.wafflestudio.seminar.core.user.database.UserRepository
import com.wafflestudio.seminar.core.user.domain.User
import com.wafflestudio.seminar.global.HibernateQueryCounter
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalTime
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import kotlin.system.measureTimeMillis


@SpringBootTest
internal class SeminarServiceImplTest @Autowired constructor(
    private val seminarService: SeminarService,
    private val seminarRepository: SeminarRepository,
    private val userTestHelper: UserTestHelper,
    private val userSeminarRepository: UserSeminarRepository,
    private val userSeminarRepositorySupport: UserSeminarRepositorySupport,
    private val userRepository: UserRepository,
    private val hibernateQueryCounter: HibernateQueryCounter,
) {
    @BeforeEach
    fun deleteAllUser() {
        userRepository.deleteAll()
        seminarRepository.deleteAll()
    }

    @Test
    @DisplayName("세미나 만들기 기능")
    fun makeSeminar() {
        // given - 진행자가 존재할 때
        val instructor = userTestHelper.createInstructor("instructor@snu.ac.kr")

        // when - 진행자가 세미나를 만들면
        val newSeminar = SeminarRequest("세미나1", 10, 10, LocalTime.of(10, 10))
        val createdSeminar = seminarService.makeSeminar(instructor.id, newSeminar)

        // then - 잘 생성된다
        val foundSeminar = seminarRepository.findByIdOrNull(createdSeminar.id)
        assertThat(createdSeminar.id).isEqualTo(foundSeminar!!.id)
    }

    @Test
    @DisplayName("세미나 수정하기")
    fun editSeminar() {
        // given - 세미나가 존재할 때
        val instructor = userTestHelper.createInstructor("instructor@snu.ac.kr")
        val seminar = createSeminar(instructor)

        // when - 세미나의 정보를 수정하면
        val changeSeminar = EditSeminarRequest(
            seminar.id,
            name = null,
            capacity = 20,
            count = null,
            time = LocalTime.of(10, 20),
            online = null
        )
        val editedSeminar = seminarService.editSeminar(instructor.id, changeSeminar)

        // then - 수정된 정보가 잘 반영된다
        val foundSeminar = seminarRepository.findByIdOrNull(editedSeminar.id)
        assertThat(editedSeminar.capacity).isEqualTo(foundSeminar!!.capacity)
        assertThat(editedSeminar.time).isEqualTo(foundSeminar!!.time)
    }

    @Test
    @DisplayName("세미나 가져오기")
    fun getSeminar() {
        // given - 세미나가 존재할 때
        val instructor = userTestHelper.createInstructor("instructor@snu.ac.kr")
        val seminar = createSeminar(instructor)

        // when - 세미나를 부르면
        val foundSeminar = seminarService.getSeminar(seminar.id)

        // then - 세미나가 조회된다
        assertThat(seminar.toSeminar()).isEqualTo(foundSeminar)
    }

    @Test
    @DisplayName("모든 세미나 가져오기")
    fun getAllSeminars() {
        // given - 여러 세미나가 존재할 때
        (1..10).map {
            var instructor = userTestHelper.createInstructor("instructor$it@naver.com")
            createSeminar(instructor, it)
        }
        val participant = userTestHelper.createParticipant("participant@naver.com")

        // when - 세미나를 모두 부르면
        val pageRequest = PageRequest.of(0, 5)
        val (seminars, queryCount) = hibernateQueryCounter.count {
            seminarService.getAllSeminar("세미나1", null, pageRequest)
        }
        val time = measureTimeMillis {
            seminarService.getAllSeminar("세미나", null, pageRequest)
        }

        // then - 모든 세미나가 조회된다
        assertThat(seminars.size).isEqualTo(2)
        println(queryCount)
        println(time)
    }

    @Test
    @DisplayName("세미나 참여하기")
    fun addSeminar() {
        // given - 참여자와 세미나가 존재할 때
        val participant = userTestHelper.createParticipant("participant@naver.com")
        val instructor = userTestHelper.createInstructor("instructor@naver.com")
        val seminar = createSeminar(instructor)

        // when - 참여자가 세미나에 참여하고자 하면
        val roleRequest = RoleRequest(User.Role.PARTICIPANT)
        val addedSeminar = seminarService.addSeminar(participant.id, seminar!!.id, roleRequest)

        // then - 세미나의 참여자에 추가된다
        val userSeminar = userSeminarRepositorySupport.find(participant.id, seminar.id)
        assertThat(addedSeminar.participants).isNotEmpty
    }

    @Test
    @DisplayName("세미나 드랍하기")
    fun dropSeminar() {
        // given - 세미나에 참여자가 존재할 때
        val participant = userTestHelper.createParticipant("participant@naver.com")
        val instructor = userTestHelper.createInstructor("instructor@naver.com")
        val seminar = createSeminar(instructor)
        val newUserSeminar = UserSeminarEntity(participant, seminar, User.Role.PARTICIPANT, LocalDateTime.now(), null)
        userSeminarRepository.save(newUserSeminar)
        seminar.users.add(newUserSeminar)
        seminarRepository.save(seminar)

        // when - 참여자가 세미나를 드랍하면
        val user = seminarService.dropSeminar(participant.id, seminar.id)

        // then - 유저 세미나의 관계가 바뀐다
        val userSeminar = userSeminarRepositorySupport.find(participant.id, seminar.id)
        assertThat(userSeminar!!.isActive).isEqualTo(false)
    }

    private fun createSeminar(instructor: UserEntity, index: Int = 1): SeminarEntity {
        val seminar = SeminarEntity("세미나$index", 10, 10, LocalTime.of(10, 10), true, instructor.id)
        seminarRepository.save(seminar)
        val userSeminar = UserSeminarEntity(
            instructor,
            seminar,
            User.Role.INSTRUCTOR,
            LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS),
            null,
            isActive = true
        )
        userSeminarRepository.save(userSeminar)
        seminar.users.add(userSeminar)
        return seminar
    }
}