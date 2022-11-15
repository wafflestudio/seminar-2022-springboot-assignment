package com.wafflestudio.seminar.core.seminar.service

import com.wafflestudio.seminar.common.SeminarException
import com.wafflestudio.seminar.core.global.HibernateQueryCounter
import com.wafflestudio.seminar.core.global.TestHelper
import com.wafflestudio.seminar.core.seminar.api.request.ParticipateSeminarRequest
import com.wafflestudio.seminar.core.seminar.api.request.SeminarRequest
import com.wafflestudio.seminar.core.seminar.api.request.UpdateSeminarRequest
import com.wafflestudio.seminar.core.seminar.database.SeminarEntity
import org.springframework.beans.factory.annotation.Autowired
import com.wafflestudio.seminar.core.seminar.database.SeminarRepository
import com.wafflestudio.seminar.core.seminar.database.UserSeminarRepository
import com.wafflestudio.seminar.core.user.database.UserEntity
import com.wafflestudio.seminar.core.user.type.UserRole
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import javax.transaction.Transactional

@SpringBootTest
internal class SeminarServiceTest @Autowired constructor(
    private val seminarService: SeminarService,
    private val hibernateQueryCounter: HibernateQueryCounter,
    private val seminarRepository: SeminarRepository,
    private val testHelper: TestHelper,
    private val userSeminarRepository: UserSeminarRepository,
) {

    @Test
    @Transactional
    fun `테스트 1_1 - 세미나를 생성할 수 있다`() {
        // given
        val instructor = testHelper.createInstructor(email = "test1-1@gmail.com")
        val request = SeminarRequest("seminar-test1-1", 100, 5, "12:30")

        // when
        val ret = seminarService.createSeminar(instructor, request)

        // then
        assertThat(ret.name).isEqualTo(request.name)
        assertThat(ret.instructors.get(0).id).isEqualTo(instructor.id)
        assertThat(ret.instructors.get(0).email).isEqualTo(instructor.email)
    }

    @Test
    @Transactional
    fun `테스트 1_2 - 세미나를 생성 실패 - 참여자는 생성 불가`() {
        // given
        val participant = testHelper.createParticipant(email = "test1-2@gmail.com")
        val request = SeminarRequest("seminar-test1-2", 100, 5, "12:30")

        // when & then
        val exception: SeminarException = assertThrows(SeminarException::class.java) {
            seminarService.createSeminar(participant, request)
        }
        assertThat(exception.message).isEqualTo("참가자는 세미나를 생성하거나 업데이트 할 수 없습니다")
        assertThat(exception.status).isEqualTo(HttpStatus.FORBIDDEN)
    }

    @Test
    @Transactional
    fun `테스트 1_3 - 세미나를 생성 실패 - 중복된 세미나 이름 생성 불가`() {
        // given
        val instructor = testHelper.createInstructor(email = "test1-3-1@gmail.com")
        val instructor2 = testHelper.createInstructor(email = "test1-3-2@gmail.com")
        val request = SeminarRequest("seminar-test1-1", 100, 5, "12:30")
        seminarService.createSeminar(instructor, request)
        
        // when & then
        val exception: SeminarException = assertThrows(SeminarException::class.java) {
            seminarService.createSeminar(instructor2, request)

        }
        assertThat(exception.message).isEqualTo("중복된 세미나 이름입니다")
        assertThat(exception.status).isEqualTo(HttpStatus.CONFLICT)
    }


    @Test
    @Transactional
    fun `테스트 2_1 - 세미나를 수정할 수 있다`() {
        // given
        val instructor = testHelper.createInstructor(email = "test2-1@gmail.com")
        val seminar = testHelper.createSeminar(name = "seminar-test2-1", userEntity = instructor)
        val request = createDummyUpdateSeminarRequest(seminar.id, "seminar-test2-1-updated")

        // when
        seminarService.updateSeminar(instructor, request)
        val updatedSeminar = seminarRepository.findById(seminar.id)
        
        // then
        assertThat(updatedSeminar.get().name).isEqualTo(request.name)
        assertThat(updatedSeminar.get().capacity).isEqualTo(request.capacity)
        assertThat(updatedSeminar.get().count).isEqualTo(request.count)
        assertThat(updatedSeminar.get().time).isEqualTo(request.time)
        assertThat(updatedSeminar.get().online).isEqualTo(request.online)
    }

    @Test
    @Transactional
    fun `테스트 2_2 - 세미나를 수정 실패 - 잘못된 유저 아이디 or 세미나 아이디`() {
        // given
        val instructor = testHelper.createInstructor(email = "test2-2@gmail.com")
        val seminar = testHelper.createSeminar(name = "seminar-test2-2", userEntity = instructor)
        val request = createDummyUpdateSeminarRequest(seminar.id + 1, "seminar-test2-2-updated" )
        val instructor2 = testHelper.createInstructor(email = "test2-2-2@gmail.com")
        
        // when & then
        val exception1: SeminarException = assertThrows(SeminarException::class.java) {
            seminarService.updateSeminar(instructor, request)
        }
        val exception2: SeminarException = assertThrows(SeminarException::class.java) {
            seminarService.updateSeminar(instructor2, request)
        }
        
        assertThat(exception1.message).isEqualTo("세미나 아이디가 잘못됐습니다")
        assertThat(exception1.status).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(exception2.message).isEqualTo("진행하고 있는 세미나가 없습니다.")
        assertThat(exception2.status).isEqualTo(HttpStatus.NOT_FOUND)
    }

    @Test
    @Transactional
    fun `테스트 2_3 - 세미나를 수정 실패 - 이미 수강중인 학생수보다 작은 수로 capcity 변경`() {
        // given
        val nParticipant = 5
        val instructor = testHelper.createInstructor(email = "test2-3@gmail.com")
        val seminar = createSeminarWithParticipants(
            instructor, "seminar-2-3", nParticipant
        )
        val request = createDummyUpdateSeminarRequest(seminar.id, "seminar-test2-3-updated", nParticipant-1)

        // when & then
        val exception: SeminarException = assertThrows(SeminarException::class.java) {
            seminarService.updateSeminar(instructor, request)
        }

        assertThat(exception.message).isEqualTo("현재 수강생이 $nParticipant 명이기 때문에 capacity를 이보다 적게 변경할 수 없습니다")
        assertThat(exception.status).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    @Transactional
    fun `테스트 3_1 - 세미나에 참여할 수 있다`() {
        // given
        val instructor = testHelper.createInstructor(email = "test3-1@gmail.com")
        val seminar = testHelper.createSeminar(name = "seminar-test3-1", userEntity = instructor)
        val participant = testHelper.createParticipant(email = "test3-1-2@gmail.com")
        val request = ParticipateSeminarRequest(participant.role)

        // when
        seminarService.participateSeminar(seminar.id, participant, request)

        // then
        assertThat(userSeminarRepository.findUserSeminarByUserIdAndSeminarId(participant.id, seminar.id)).isNotNull
    }

    @Test
    @Transactional
    fun `테스트 3_2 - 세미나를 함께 진행할 수 있다`() {
        // given
        val instructor = testHelper.createInstructor(email = "test3-2@gmail.com")
        val seminar = testHelper.createSeminar(name = "seminar-test3-2", userEntity = instructor)
        val instructor2 = testHelper.createParticipant(email = "test3-2-2.com")
        val request = ParticipateSeminarRequest(instructor2.role)

        // when
        seminarService.participateSeminar(seminar.id, instructor2, request)

        // then
        assertThat(userSeminarRepository.findUserSeminarByUserIdAndSeminarId(instructor2.id, seminar.id))
    }
    
    @Test
    @Transactional
    fun `테스트 3_3 - 세미나를 참여 실패 - 잘못된 role`() {
        // given
        val instructor = testHelper.createInstructor(email = "test3-3@gmail.com")
        val participant = testHelper.createParticipant(email = "test3-3-2@gmail.com")
        val seminar = testHelper.createSeminar(name = "seminar-test3-3", userEntity = instructor)
        
        val participantRoleRequest = ParticipateSeminarRequest(UserRole.PARTICIPANT)
        val instructorRoleRequest = ParticipateSeminarRequest(UserRole.INSTRUCTOR)

        // when

        // when & then
        val exception1: SeminarException = assertThrows(SeminarException::class.java) {
            seminarService.participateSeminar(seminar.id, instructor, participantRoleRequest)
        }
        val exception2: SeminarException = assertThrows(SeminarException::class.java) {
            seminarService.participateSeminar(seminar.id, participant, instructorRoleRequest)
        }
        assertThat(exception1.message).isEqualTo("ParticipateProfile이 없는 유저입니다")
        assertThat(exception1.status).isEqualTo(HttpStatus.FORBIDDEN)
        assertThat(exception2.message).isEqualTo("해당 유저는 Instructor 권한이 없습니다")
        assertThat(exception2.status).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    @Transactional
    fun `테스트 3_4 - 세미나를 참여 실패 - 정원초과`() {
        // given
        val seminar = createSeminarWithParticipants("test3-4@gmail.com", "seminar-test3-4", 20, 20)
        val participant = testHelper.createParticipant(email = "test3-4-2@gmail.com")
        val participantRoleRequest = ParticipateSeminarRequest(UserRole.PARTICIPANT)

        // when & then
        val exception: SeminarException = assertThrows(SeminarException::class.java) {
            seminarService.participateSeminar(seminar.id, participant, participantRoleRequest)
        }
        assertThat(exception.message).isEqualTo("정원이 다 찼습니다")
        assertThat(exception.status).isEqualTo(HttpStatus.BAD_REQUEST)
    }
    
    @Test
    @Transactional
    fun `테스트 3_5 - 세미나를 참여 실패 - 중도 포기한 세미나에 참여 불가`() {
        // given
        // given
        val instructor = testHelper.createInstructor(email = "test3-5@gmail.com")
        val participant = testHelper.createParticipant(email = "test3-5-2@gmail.com")
        val seminar = testHelper.createSeminar(name = "seminar-test3-5", userEntity = instructor)
        val participantRoleRequest = ParticipateSeminarRequest(UserRole.PARTICIPANT)
        seminarService.participateSeminar(seminar.id, participant, participantRoleRequest)
        seminarService.dropSeminar(participant, seminar.id)


        // when & then
        val exception: SeminarException = assertThrows(SeminarException::class.java) {
            seminarService.participateSeminar(seminar.id, participant, participantRoleRequest)
        }
        assertThat(exception.message).isEqualTo("중도 포기한 세미나에 다시 참여할 수 없습니다")
        assertThat(exception.status).isEqualTo(HttpStatus.BAD_REQUEST)
    }

    @Test
    @Transactional
    fun `테스트 3_6 - 세미나를 참여 실패 - 이미 참여중`() {
        // given
        // given
        val instructor = testHelper.createInstructor(email = "test3-6@gmail.com")
        val participant = testHelper.createParticipant(email = "test3-6-2@gmail.com")
        val seminar = testHelper.createSeminar(name = "seminar-test3-6", userEntity = instructor)
        val participantRoleRequest = ParticipateSeminarRequest(UserRole.PARTICIPANT)
        seminarService.participateSeminar(seminar.id, participant, participantRoleRequest)

        // when & then
        val exception: SeminarException = assertThrows(SeminarException::class.java) {
            seminarService.participateSeminar(seminar.id, participant, participantRoleRequest)
        }
        assertThat(exception.message).isEqualTo("이미 이 세미나에 참여중입니다")
        assertThat(exception.status).isEqualTo(HttpStatus.BAD_REQUEST)
    }
    
    

    @Test
    fun `테스트 4_1 - 세미나를 드랍할 수 있다`() {
        // given
        val instructor = testHelper.createInstructor(email = "test4-1@gmail.com")
        val seminar = testHelper.createSeminar(name = "seminar-test4-1", userEntity = instructor)
        val participant = testHelper.createParticipant(email = "test4-1-2@gmail.com")
        testHelper.createUserSeminar(participant, seminar)

        // when
        seminarService.dropSeminar(participant, seminar.id)
        val userSeminar = userSeminarRepository.findUserSeminarByUserIdAndSeminarId(participant.id, seminar.id)

        // then
        assertThat(userSeminar).isNotNull
        assertEquals(userSeminar!!.isActive, false)
        assertThat(userSeminar!!.droppedAt).isNotNull
    }

    @Test
    fun `테스트 4_2 - 세미나를 드랍 실패 - 진행자는 드랍할 수 없음`() {
        // given
        val instructor = testHelper.createInstructor(email = "test4-2@gmail.com")
        val seminar = testHelper.createSeminar(name = "seminar-test4-2", userEntity = instructor)

        // when & then
        val exception: SeminarException = assertThrows(SeminarException::class.java) {
            seminarService.dropSeminar(instructor, seminar.id)
        }
        assertThat(exception.message).isEqualTo("세미나 진행자는 세미나를 드랍할 수 없습니다")
        assertThat(exception.status).isEqualTo(HttpStatus.FORBIDDEN)
    }
    
    @Test
    @Transactional
    fun `테스트 5_1 - 세미나를 없앨 수 있다`() {
        // given
        val instructor = testHelper.createInstructor(email = "test5-1@gmail.com")
        val seminar = testHelper.createSeminar(name = "seminar-test5-1", userEntity = instructor)
        val participant = testHelper.createParticipant(email = "test5-1-2@gmail.com")
        testHelper.createUserSeminar(participant, seminar)

        // when
        seminarService.deleteSeminar(instructor, seminar.id)
        
        // then
        assertThat(seminarRepository.findById(seminar.id)).isEmpty
    }

    @Test
    @Transactional
    fun `테스트 5_2 - 세미나 delete 실패 - 참여자는 없앨 수 없다`() {
        // given
        val instructor = testHelper.createInstructor(email = "test5-2@gmail.com")
        val seminar = testHelper.createSeminar(name = "seminar-test5-2", userEntity = instructor)
        val participant = testHelper.createParticipant(email = "test5-2-2@gmail.com")
        testHelper.createUserSeminar(participant, seminar)

        // when & then
        val exception: SeminarException = assertThrows(SeminarException::class.java) {
            seminarService.deleteSeminar(participant, seminar.id)
        }
        assertThat(exception.message).isEqualTo("참여자는 세미나를 없앨 권한이 없습니다")
        assertThat(exception.status).isEqualTo(HttpStatus.FORBIDDEN)
    }

    @Test
    @Transactional
    fun `테스트 5_3 - 세미나 delete 실패 - 다른 instructor는 없앨 수 없다`() {
        // given
        val instructor = testHelper.createInstructor(email = "test5-3@gmail.com")
        val seminar = testHelper.createSeminar(name = "seminar-test5-3", userEntity = instructor)
        val instructorOther = testHelper.createInstructor(email = "test5-3-2@gmail.com")


        // when & then
        val exception: SeminarException = assertThrows(SeminarException::class.java) {
            seminarService.deleteSeminar(instructorOther, seminar.id)
        }
        assertThat(exception.message).isEqualTo("세미나를 없앨 권한이 없습니다")
        assertThat(exception.status).isEqualTo(HttpStatus.FORBIDDEN)
    }

    @Test
    @Transactional
    fun `테스트 6_1 - 세미나 id를 통해 세미나를 불러오기 성공`() {
        // given
        val seminar = createFixtures("test6-1")

        // when
        val (result, queryCount) = hibernateQueryCounter.count {
            seminarService.getSeminar(seminar.id)
        }
        assertThat(queryCount).isEqualTo(1) // TODO!: 질문! query count가 2 여야 될 것 같은데 오히려 1이 나옴
        assertThat(result.id).isEqualTo(seminar.id)
    }

    @Test
    @Transactional
    fun `테스트 6_2 - 세미나 불러오기 실패 - 존재하지 않는 세미나 id`() {
        // when & then
        val exception: SeminarException = assertThrows(SeminarException::class.java) {
            seminarService.getSeminar(10000000)
        }
        assertThat(exception.message).isEqualTo("해당 seminar id가 존재하지 않습니다")
        assertThat(exception.status).isEqualTo(HttpStatus.NOT_FOUND)
    }


    @Test
    @Transactional
    fun `테스트 7_1 - 특정 이름에 맞는 세미나를 원하는 순서로 불러오기 성공`() {
        // given
        createSeminarWithParticipants("test7-1-1@gmail.com", "Spring1", 3)
        createSeminarWithParticipants("test7-1-2@gmail.com", "Spring2", 2)
        createSeminarWithParticipants("test7-1-3@gmail.com", "Spring3", 2)
        createSeminarWithParticipants("test7-1-4@gmail.com", "Django", 3)
        createSeminarWithParticipants("test7-1-5@gmail.com", "iOS", 5)
        createSeminarWithParticipants("test7-1-6@gmail.com", "iOS2", 5)


        // when
        val (result1, queryCount1) = hibernateQueryCounter.count {
            seminarService.getSeminarsByQueryParam(name = "pring", "earliest", 0, 20)
        }

        val (result2, queryCount2) = hibernateQueryCounter.count {
            seminarService.getSeminarsByQueryParam(name = "pring", null, 0, 20)
        }

        val (result3, queryCount3) = hibernateQueryCounter.count {
            seminarService.getSeminarsByQueryParam(name = "Django", null, 0, 20)
        }
        
        // then
        assertThat(result1.content.size).isEqualTo(3)
        assertThat(result1.content.get(0).name).isEqualTo("Spring1")
        assertThat(queryCount1).isEqualTo(2)

        assertThat(result2.content.size).isEqualTo(3)
        assertThat(result2.content.get(0).name).isEqualTo("Spring3")
        assertThat(queryCount2).isEqualTo(2)
        
        assertThat(result3.content.size).isEqualTo(1)
        assertThat(result3.content.get(0).name).isEqualTo("Django")
        assertThat(queryCount3).isEqualTo(2)

    }

    private fun createFixtures(testName:String): SeminarEntity {
        val instructor = testHelper.createInstructor(email = "${testName}@gmail.com")
        val participants = (1..5).map { testHelper.createParticipant(email = "participant#${testName}$it@gmail.com") }
        val seminar = testHelper.createSeminar(name = "seminarForTest", userEntity = instructor)
        participants.map { testHelper.createUserSeminar(it, seminar) }

        val instructor2 = testHelper.createInstructor(email = "instructor2.com")
        val participants2 = (6..10).map { testHelper.createParticipant(email = "participant#${testName}$it@gmail.com") }
        val seminar2 = testHelper.createSeminar(name = "seminarForTest2", userEntity = instructor2)
        participants2.map { testHelper.createUserSeminar(it, seminar2)}

        return seminar
    }
    
    fun createSeminarWithParticipants(
        instructorEmail: String,
        seminarNme: String = "Spring",
        nParticipant: Int,
        capacity: Int = 20
    ) : SeminarEntity {
        val instructor = testHelper.createInstructor(email = instructorEmail)
        val participants = (1 .. nParticipant).map { testHelper.createParticipant(email= "participant-$it-$instructorEmail") }

        val seminar = testHelper.createSeminar(name=seminarNme, userEntity = instructor, capacity = capacity)

        participants.forEach {
            testHelper.createUserSeminar(it, seminar)   
        }

        return seminar
    }
    fun createSeminarWithParticipants(
        instructor: UserEntity,
        seminarNme: String = "Spring",
        nParticipant: Int
    ) : SeminarEntity {
        val participants = (1 .. nParticipant).map { testHelper.createParticipant(email= "participant-$it-${instructor.email}") }

        val seminar = testHelper.createSeminar(name=seminarNme, userEntity = instructor)

        participants.forEach {
            testHelper.createUserSeminar(it, seminar)
        }

        return seminar
    }
    
    fun createDummyUpdateSeminarRequest(
        seminarId: Long,
        name: String = "Spring",
        capacity: Int = 40
    ) : UpdateSeminarRequest {
        return  UpdateSeminarRequest(
            id = seminarId,
            name = name,
            capacity = capacity,
            count = 20,
            time = "12:30",
            online = false
        )
    }
}