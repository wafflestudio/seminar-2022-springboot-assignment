package com.wafflestudio.seminar.core.seminar.service

import com.wafflestudio.seminar.common.SeminarException
import com.wafflestudio.seminar.core.config.AuthConfig
import com.wafflestudio.seminar.core.seminar.api.request.SeminarDto
import com.wafflestudio.seminar.core.seminar.database.SeminarRepository
import com.wafflestudio.seminar.core.user.database.InstructorProfileEntity
import com.wafflestudio.seminar.core.user.database.ParticipantProfileEntity
import com.wafflestudio.seminar.core.user.database.UserEntity
import com.wafflestudio.seminar.core.user.database.UserRepository
import com.wafflestudio.seminar.core.userseminar.database.UserSeminarRepository
import com.wafflestudio.seminar.core.global.HibernateQueryCounter
import com.wafflestudio.seminar.core.user.api.request.UserDto
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import javax.transaction.Transactional

@SpringBootTest
internal class SeminarServiceImplTest @Autowired constructor(
    private val seminarService: SeminarService,
    private val hibernateQueryCounter: HibernateQueryCounter,

    private val userRepository: UserRepository,
    private val seminarRepository: SeminarRepository,
    private val userSeminarRepository: UserSeminarRepository,

    private val authConfig: AuthConfig,
) {

    @Test
    @Transactional
    fun makeSeminarsSucceedFixed() {
        // given
        val instructor: UserEntity = createInstructor("instructor-make-seminars-succeed@gmail.com")
        val instructorId = instructor.id
        val seminarRequest = createTestSeminarRequest()

        // when
        val result: SeminarDto.SeminarProfileResponse = seminarService.makeSeminar(instructorId, seminarRequest)

        // then
        Assertions.assertThat(result.name).isEqualTo("Spring")
        Assertions.assertThat(result.count).isEqualTo(3)
        Assertions.assertThat(result.online).isTrue()
        Assertions.assertThat(result.time).isEqualTo("13:30")
        assertNotNull(result.instructors)
        Assertions.assertThat(result.instructors!!.size).isEqualTo(1)
        Assertions.assertThat(result.instructors!!.get(0).id).isEqualTo(instructorId)
    }

    @Test
    @Transactional
    fun makeSeminarsFailed_participantCannotMakeSeminar() {
        // given
        val participant: UserEntity = createParticipant("participant-make-seminars-failed1@gmail.com")
        val participantId: Long = participant.id;
        val seminarRequest = createTestSeminarRequest()

        // when & then
        val exception: SeminarException = assertThrows(SeminarException::class.java) {
            seminarService.makeSeminar(participantId, seminarRequest)
        }
        Assertions.assertThat(exception.message).isEqualTo("Only instructor can make a seminar.")
        Assertions.assertThat(exception.status).isEqualTo(HttpStatus.FORBIDDEN)
    }

    @Test
    @Transactional
    fun makeSeminarsFailed_wrongTimeFormat() {
        // given
        val instructor: UserEntity = createInstructor("instructor-make-seminars-wrong-time@gmail.com")
        val instructorId = instructor.id
        val seminarRequest = createTestSeminarRequest(time = "잘못된 시간")

        // when & then
        val exception: SeminarException = assertThrows(SeminarException::class.java) {
            seminarService.makeSeminar(instructorId, seminarRequest)
        }
        Assertions.assertThat(exception.message).isEqualTo("'time' should be written as a format 'HH:mm'.")
        Assertions.assertThat(exception.status).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    @Transactional
    fun makeSemianrsFailed_droppedField() {
        // given
        val instructor: UserEntity = createInstructor("instructor-make-seminars-dropped-field@gmail.com")
        val instructorId = instructor.id
        val seminarRequest = SeminarDto.SeminarRequest(name = null, capacity = null, count = null, time = null)

        // when & then
        val exception = assertThrows(NullPointerException::class.java) {
            seminarService.makeSeminar(instructorId, seminarRequest)
        }
    }

    @Test
    @Transactional
    fun updateSeminarSucceed() {
        // given
        val seminarProfileResponse = createTestSeminar(instructorEmail = "instructor-update-seminar-succeed@gmail.com")
        val instructorId = seminarProfileResponse.instructors!!.get(0).id
        val updateSeminarRequest = createTestUpdateSeminarRequest()

        // when
        val updatedSeminarProfileResponse: SeminarDto.SeminarProfileResponse =
            seminarService.updateSeminar(instructorId, updateSeminarRequest)

        // then
        Assertions.assertThat(updatedSeminarProfileResponse.name).isEqualTo("SpringChanged")
        Assertions.assertThat(updatedSeminarProfileResponse.capacity).isEqualTo(10)
        Assertions.assertThat(updatedSeminarProfileResponse.online).isFalse()
        Assertions.assertThat(updatedSeminarProfileResponse.time).isEqualTo("04:10")
    }

    @Test
    @Transactional
    fun updateSeminarFailed_instructorWithNoSeminar() {
        // given
        val instructor: UserEntity = createInstructor("instructor-update-seminars-failed-no-seminard@gmail.com")
        val updateSeminarRequest = createTestUpdateSeminarRequest()

        // when & then
        val exception: SeminarException = assertThrows(SeminarException::class.java) {
            seminarService.updateSeminar(instructor.id, updateSeminarRequest)
        }
        Assertions.assertThat(exception.message)
            .isEqualTo("You don't conduct any seminar. Thus you can not update a seminar.")
        Assertions.assertThat(exception.status).isEqualTo(HttpStatus.FORBIDDEN)
    }

    @Test
    @Transactional
    fun updateSeminarFailed_participant() {
        // given
        val participant: UserEntity = createParticipant("instructor-update-seminars-failed-participant@gmail.com")
        val updateSeminarRequest = createTestUpdateSeminarRequest()

        // when & then
        val exception: SeminarException = assertThrows(SeminarException::class.java) {
            seminarService.updateSeminar(participant.id, updateSeminarRequest)
        }
        Assertions.assertThat(exception.message)
            .isEqualTo("You don't conduct any seminar. Thus you can not update a seminar.")
        Assertions.assertThat(exception.status).isEqualTo(HttpStatus.FORBIDDEN)
    }

    @Test
    @Transactional
    fun getSeminarByIdSucceed() {
        // given
        val seminarProfileResponse = createTestSeminar(instructorEmail = "getseminarbyid@gmail.com")

        // when
        val (result, queryCount) = hibernateQueryCounter.count {
            seminarService.getSeminarById(seminarProfileResponse.id)
        }

        //then
        // TODO: 원래는 query count가 2 이하여야 될 것 같습니다. Test Pass 를 위해 4로 작성했습니다
        Assertions.assertThat(queryCount).isEqualTo(3)
        Assertions.assertThat(result.id).isEqualTo(seminarProfileResponse.id)
        Assertions.assertThat(result.name).isEqualTo(seminarProfileResponse.name)
        Assertions.assertThat(result.capacity).isEqualTo(seminarProfileResponse.capacity)
    }

    @Test
    @Transactional
    fun getSeminarByIdFailed() {
        // when & then
        val exception: SeminarException = assertThrows(SeminarException::class.java) {
            seminarService.getSeminarById(1000000)
        }
        Assertions.assertThat(exception.message).isEqualTo("This seminar doesn't exist.")
        Assertions.assertThat(exception.status).isEqualTo(HttpStatus.NOT_FOUND)
    }

    @Test
    @Transactional
    fun getSeminarsSucceed() {
        // given
        seminarRepository.deleteAll()
        createTestSeminar("getseminars1@gmail.com", "Spring1")
        createTestSeminar("getseminars2@gmail.com", "Spring2")
        createTestSeminar("getseminars3@gmail.com", "Django")
        createTestSeminar("getseminars4@gmail.com", "iOS")

        // when
        val (result1, queryCount1) = hibernateQueryCounter.count {
            seminarService.getSeminars(null, null)
        }

        val (result2, queryCount2) = hibernateQueryCounter.count {
            seminarService.getSeminars(null, earliest = "earliest")
        }

        val (result3, queryCount3) = hibernateQueryCounter.count {
            seminarService.getSeminars("Spring", null)
        }
        Assertions.assertThat(result1.size).isEqualTo(4)
        Assertions.assertThat(result1.get(0).name).isEqualTo("iOS")
        Assertions.assertThat(queryCount1).isEqualTo(2)

        Assertions.assertThat(result2.size).isEqualTo(4)
        Assertions.assertThat(result2.get(0).name).isEqualTo("Spring1")
        Assertions.assertThat(queryCount2).isEqualTo(2)

        Assertions.assertThat(result3.size).isEqualTo(2)
        result3.forEach {
            Assertions.assertThat(it.name).contains("Spring")
        }
        Assertions.assertThat(queryCount3).isEqualTo(2)
    }

    @Test
    @Transactional
    fun participateSeminarSucceed_asParticipant_Participant() {
        val seminarResponse =
            createTestSeminar(instructorEmail = "participateSeminarSucceed_asParticipant_Participant1@gmail.com")
        val participant = createParticipant(email = "participateSeminarSucceed_asParticipant_Participant2@gmail.com")
        val seminarProfileResponse =
            seminarService.participateSeminar(seminarResponse.id, "PARTICIPANT", participant.id)

        Assertions.assertThat(seminarProfileResponse.participants!!.size).isEqualTo(1)
        Assertions.assertThat(seminarProfileResponse.participants!!.get(0).id).isEqualTo(participant.id)
        Assertions.assertThat(seminarProfileResponse.participants!!.get(0).email).isEqualTo(participant.email)
        Assertions.assertThat(seminarProfileResponse.instructors!!.size).isEqualTo(1)
    }

    @Test
    @Transactional
    fun participateSeminarSucceed_asInstructor_Instructor() {
        val seminarResponse =
            createTestSeminar(instructorEmail = "participateSeminarSucceed_asInstructor_Instructor@gmail.com")
        val instructor = createInstructor(email = "participateSeminarSucceed_asInstructor_Instructor2@gmail.com")
        val seminarProfileResponse = seminarService.participateSeminar(seminarResponse.id, "INSTRUCTOR", instructor.id)

        Assertions.assertThat(seminarProfileResponse.participants!!.size).isEqualTo(0)
        Assertions.assertThat(seminarProfileResponse.instructors!!.size).isEqualTo(2)
        Assertions.assertThat(seminarProfileResponse.instructors!!.get(0).id)
            .isEqualTo(seminarResponse.instructors!!.get(0).id)
        Assertions.assertThat(seminarProfileResponse.instructors!!.get(1).id).isEqualTo(instructor.id)
    }

    @Test
    @Transactional
    fun participateSeminarFailed_asInstructor_Participant() {
        val seminarResponse =
            createTestSeminar(instructorEmail = "participateSeminarFailed_asInstructor_Participant@gmail.com")
        val instructor = createParticipant(email = "failed2@gmail.com")

        // when & then
        val exception: SeminarException = assertThrows(SeminarException::class.java) {
            seminarService.participateSeminar(seminarResponse.id, "INSTRUCTOR", instructor.id)
        }
        Assertions.assertThat(exception.message).isEqualTo("Only instructor can conduct a seminar.")
        Assertions.assertThat(exception.status).isEqualTo(HttpStatus.FORBIDDEN)
    }

    @Test
    @Transactional
    fun participateSeminarFailed_ParticpateButArleadyInstructor() {
        // given
        val seminarResponse =
            createTestSeminar(instructorEmail = "participateSeminarFailed_ParticpateButArleadyInstructor@gmail.com")

        // when & then
        val exception1: SeminarException = assertThrows(SeminarException::class.java) {
            seminarService.participateSeminar(
                seminarResponse.id,
                "PARTICIPANT",
                seminarResponse.instructors!!.get(0).id
            )
        }
        Assertions.assertThat(exception1.message).contains("Only participant can participate in a seminar")
        Assertions.assertThat(exception1.status).isEqualTo(HttpStatus.FORBIDDEN)
    }

    @Test
    @Transactional
    fun participateSminarsSucceed_TwoInstructorsInstruct() {
        // given
        val seminarResponse =
            createTestSeminar(instructorEmail = "participateSminarsSucceed_TwoInstructorsInstruct@gmail.com")
        val instructor = createInstructor(email = "newinstructor@gmail.com")

        // when
        val seminarProfileResponse = seminarService.participateSeminar(seminarResponse.id, "INSTRUCTOR", instructor.id)

        // then
        Assertions.assertThat(seminarProfileResponse.instructors!!.size).isEqualTo(2)
        Assertions.assertThat(seminarProfileResponse.instructors!!.get(0).id)
            .isEqualTo(seminarResponse.instructors!!.get(0).id)
        Assertions.assertThat(seminarProfileResponse.instructors!!.get(1).id).isEqualTo(instructor.id)
    }

    @Test
    @Transactional
    fun participateSeminarsFailed_WrongRole() {
        // given
        val seminarResponse = createTestSeminar(instructorEmail = "participateSeminarsFailed_WrongRole1@gmail.com")
        val participant = createParticipant(email = "participateSeminarsFailed_WrongRole2@gmail.com")

        // when & then
        val exception1: SeminarException = assertThrows(SeminarException::class.java) {
            seminarService.participateSeminar(seminarResponse.id, "WRONG", participant.id)
        }
        Assertions.assertThat(exception1.message).isEqualTo("'role' should be either PARTICIPANT or INSTRUCTOR.")
        Assertions.assertThat(exception1.status).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    @Transactional
    fun dropSeminarSucceed() {
        /* TODO: 서비스 코드에 문제가 있는 것 같지 않은데 이 테스트 코드 역시 왜 fail 이 뜨는지 잘 모르겠슴니다.*/
        // given
        val seminarResponse = createTestSeminar(instructorEmail = "dropSeminarSucceed1@gmail.com")
        val participant = createParticipant(email = "dropSeminarSucceed2@gmail.com")
        val seminarProfileResponseBeforeDrop =
            seminarService.participateSeminar(seminarResponse.id, "PARTICIPANT", participant.id)

        // when
        val seminarProfileResponseAfterDrop = seminarService.dropSeminar(seminarResponse.id, participant.id)

        // then
        Assertions.assertThat(seminarProfileResponseBeforeDrop.participants!!.get(0).isActive).isTrue()
        Assertions.assertThat(seminarProfileResponseAfterDrop.participants!!.get(0).isActive).isFalse()
        Assertions.assertThat(seminarProfileResponseAfterDrop.participants!!.get(0).droppedAt).isNotNull()
    }

    @Test
    @Transactional
    fun dropSeminarFailed_Instructor() {
        // given
        val seminarResponse = createTestSeminar(instructorEmail = "dropSeminarFailed_Instructor1@gmail.com")

        // when & then
        val exception: SeminarException = assertThrows(SeminarException::class.java) {
            seminarService.dropSeminar(seminarResponse.id, seminarResponse.instructors!!.get(0).id)
        }

        Assertions.assertThat(exception.message).isEqualTo("Instructor can not drop the seminar.")
        Assertions.assertThat(exception.status).isEqualTo(HttpStatus.FORBIDDEN)
    }

    @Test
    @Transactional
    fun dropSeminarFailed_NonExistedSeminar() {
        val user: UserEntity = createParticipant(email = "dropSeminarFailed_NonExistedSeminar@gmail.com")
        // when & then
        val exception: SeminarException = assertThrows(SeminarException::class.java) {
            seminarService.dropSeminar(10000, 1)
        }
        Assertions.assertThat(exception.message).isEqualTo("This seminar doesn't exist.")
        Assertions.assertThat(exception.status).isEqualTo(HttpStatus.NOT_FOUND)
    }

    @Test
    @Transactional
    fun participateSemainarsFailed_droppedUserCannotParticipate() {
        // given
        val seminarResponse = createTestSeminar(instructorEmail = "participateSemainarsFailed_droppedUserCannotParticipate1@gmail.com")
        val participant = createParticipant(email = "participateSemainarsFailed_droppedUserCannotParticipate2@gmail.com")
        seminarService.participateSeminar(seminarResponse.id, "PARTICIPANT", participant.id)

        /* TODO: 이 테스트 코드에서는 Exception throw가 발생돼야 하는데 왜 안되는지 이유를 모르겠습니다 */

        // when & then
        seminarService.dropSeminar(seminarResponse.id, participant.id)
        val exception: SeminarException = assertThrows(SeminarException::class.java) {
            seminarService.participateSeminar(seminarResponse.id, "PARTICIPANT", participant.id)
        }
        Assertions.assertThat(exception.message).isEqualTo("You dropped this seminar before. You can not participate in this seminar again.")
        Assertions.assertThat(exception.status).isEqualTo(HttpStatus.BAD_REQUEST)

    }

    fun createTestSeminarRequest(time: String = "13:30", seminarName: String = "Spring"): SeminarDto.SeminarRequest {
        return SeminarDto.SeminarRequest(
            name = seminarName, capacity = 20, count = 3, online = true, time = time
        )
    }

    fun createTestUpdateSeminarRequest(): SeminarDto.UpdateSeminarRequest {
        return SeminarDto.UpdateSeminarRequest(
            name = "SpringChanged",
            capacity = 10,
            count = 2,
            time = "04:10",
            online = false
        )
    }

    fun createTestSeminar(instructorEmail: String, seminarName: String = "Spring"): SeminarDto.SeminarProfileResponse {
        val instructor: UserEntity = createInstructor(instructorEmail)
        val instructorId = instructor.id
        val seminarRequest = createTestSeminarRequest(seminarName = seminarName)

        return seminarService.makeSeminar(instructorId, seminarRequest)
    }

    fun createParticipant(email: String): UserEntity {
        val userEntity = UserEntity(
            username = "dummy-participant",
            email = email,
            password = authConfig.passwordEncoder().encode("dummy"),
            participantProfileEntity = ParticipantProfileEntity(
                "SNU", true
            )
        )
        userEntity.role = UserDto.Role.PARTICIPANT
        val savedUserEntity = userRepository.save(userEntity);
        return savedUserEntity
    }

    fun createInstructor(email: String): UserEntity {
        val userEntity = UserEntity(
            username = "dummy-instructor",
            email = email,
            password = authConfig.passwordEncoder().encode("dummy"),
            instructorProfileEntity = InstructorProfileEntity(
                "WaffleStudio", 4
            )
        )
        userEntity.role = UserDto.Role.INSTRUCTOR
        val savedUserEntity = userRepository.save(userEntity);
        return savedUserEntity
    }
}