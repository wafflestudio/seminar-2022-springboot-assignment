package com.wafflestudio.seminar.core.seminar

import com.wafflestudio.seminar.common.Seminar400
import com.wafflestudio.seminar.core.seminar.api.request.CreateSeminarDTO
import com.wafflestudio.seminar.core.seminar.api.request.JoinSeminarDTO
import com.wafflestudio.seminar.core.seminar.api.request.UpdateSeminarDTO
import com.wafflestudio.seminar.core.seminar.database.SeminarEntity
import com.wafflestudio.seminar.core.seminar.database.SeminarRepository
import com.wafflestudio.seminar.core.seminar.database.UserSeminarEntity
import com.wafflestudio.seminar.core.seminar.database.UserSeminarRepository
import com.wafflestudio.seminar.core.seminar.domain.Seminar
import com.wafflestudio.seminar.core.seminar.service.SeminarService
import com.wafflestudio.seminar.core.user.UserTestHelper
import com.wafflestudio.seminar.core.user.database.UserEntity
import com.wafflestudio.seminar.core.user.database.UserRepository
import com.wafflestudio.seminar.global.HibernateQueryCounter
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class SeminarServiceTest @Autowired constructor(
    private val seminarService: SeminarService,
    private val seminarRepository: SeminarRepository,
    private val seminarTestHelper: SeminarTestHelper,
    private val userRepository: UserRepository,
    private val userSeminarRepository: UserSeminarRepository,
    private val userTestHelper: UserTestHelper,
    private val hibernateQueryCounter: HibernateQueryCounter,
) {
    @BeforeEach
    fun beforeEachTest() {
        userTestHelper.deleteAllUser()
        seminarTestHelper.deleteAllSeminar()
        userSeminarRepository.deleteAll()
    }
    
    @Test
    fun `세미나 조회 - 전체 세미나를 조회 쿼리 수`() {
        // given
        createFixtures()
        
        // when
        val (result, queryCount) = hibernateQueryCounter.count<List<Seminar>> {
            seminarService.getSeminarList("", "")
        }

        // then
        assertThat(result).hasSize(2)
        assertThat(queryCount).isEqualTo(14)
    }

    @Test
    fun `세미나 조회 - 전체 세미나를 조회할 수 있다`() {
        // given
        createFixtures()

        // when
        val seminarList: List<Seminar> = seminarService.getSeminarList("", "")
        
        // then
        assertThat(seminarList).hasSize(2)
        assertThat(seminarList[0].name).isEqualTo("seminar#2")
        assertThat(seminarList[1].name).isEqualTo("seminar#1")
    }
    
    @Test
    fun `세미나 조회 - 시간 순서대로 정렬할 수 있다`() {
        //given
        createFixtures()
        
        // when
        val seminarList: List<Seminar> = seminarService.getSeminarList("", "earliest")

        // then
        assertThat(seminarList).hasSize(2)
        assertThat(seminarList[0].name).isEqualTo("seminar#1")
        assertThat(seminarList[1].name).isEqualTo("seminar#2")
    }
    
    @Test
    fun `세미나 조회 - 세미나 이름으로 검색할 수 있다`() {
        //given
        createFixtures()

        // when
        val seminarList: List<Seminar> = seminarService.getSeminarList("#1", "")

        // then
        assertThat(seminarList).hasSize(1)
        assertThat(seminarList[0].name).isEqualTo("seminar#1")
    }
    
    @Test
    fun `세미나 생성 - 세미나를 만들 수 있다`() {
        // given
        val instructor: UserEntity = userTestHelper.createInstructor("instructor@email.com", "", "", "", null)
        val createSeminarDTO = CreateSeminarDTO("seminar#1", 100, 10, "00:00", true)
        
        // when
        seminarService.createSeminar(instructor, createSeminarDTO)
        
        // then
        val seminarList: List<SeminarEntity> = seminarRepository.findAll()
        assertThat(seminarList).hasSize(1)
        assertThat(seminarList[0].name).isEqualTo("seminar#1")
    }

    @Test
    fun `세미나 생성 - participant 가 세미나를 만들면 400을 반환한다`() {
        //given
        val participant: UserEntity = userTestHelper.createParticipant("participant@email.com", "", "", "", true)
        val createSeminarDTO = CreateSeminarDTO("seminar#1", 100, 10, "00:00", true)
        
        // when
        val thrown: Throwable = Assertions.catchThrowable{ seminarService.createSeminar(participant, createSeminarDTO) }
        
        // then
        assertThat(thrown).isInstanceOf(Seminar400::class.java)
    }
    
    @Test
    fun `세미나 수정 - instructor 는 세미나를 수정할 수 있다`() {
        // given
        val instructor: UserEntity = userTestHelper.createInstructor("instructor@email.com", "", "", "", null)
        val seminar: SeminarEntity = seminarTestHelper.createSeminar("seminar", 100, 10, "00:00", true)
        seminarTestHelper.joinSeminar(userEntity = instructor, seminarEntity = seminar, role = "instructor")
        
        val newName = "newSeminar"
        val newCapacity = 1000
        val newCount = 100
        val newTime = "00:01"
        val newOnline = false
        val updateSeminarDTO = UpdateSeminarDTO(
            name = newName,
            capacity = newCapacity,
            count = newCount,
            time = newTime,
            online = newOnline
        )
        
        // when
        val newSeminar = seminarService.updateSeminar(instructor, seminar.id, updateSeminarDTO)
        
        // then
        assertThat(newSeminar.name).isEqualTo(newName)
        assertThat(newSeminar.time).isEqualTo(newTime)
        assertThat(newSeminar.count).isEqualTo(newCount)
        assertThat(newSeminar.capacity).isEqualTo(newCapacity)
        assertThat(newSeminar.online).isEqualTo(newOnline)
    }
    
    @Test
    fun `세미나 수정 - participant 가 세미나를 수정하면 400을 반환한다`() {
        // given 
        val participant: UserEntity = userTestHelper.createParticipant("participant@email.com", "", "", "", true)
        val seminar: SeminarEntity = seminarTestHelper.createSeminar("seminar#1", 100, 10, "00:00", true)
        val updateSeminarDTO = UpdateSeminarDTO(name = null, capacity = null, time = null, count = null, online = null)
        
        // when
        val thrown: Throwable = catchThrowable{ seminarService.updateSeminar(participant, seminar.id, updateSeminarDTO) }

        // then
        assertThat(thrown).isInstanceOf(Seminar400::class.java)
    }

    @Test
    fun `세미나 수정 - 참여하지 않는 instructor 가 세미나를 수정하면 400을 반환한다`() {
        // given 
        val instructor: UserEntity = userTestHelper.createInstructor("instructor@email.com", "", "", "", null)
        val seminar: SeminarEntity = seminarTestHelper.createSeminar("seminar#1", 100, 10, "00:00", true)
        val updateSeminarDTO = UpdateSeminarDTO(name = null, capacity = null, time = null, count = null, online = null)

        // when
        val thrown: Throwable = catchThrowable{ seminarService.updateSeminar(instructor, seminar.id, updateSeminarDTO) }

        // then
        assertThat(thrown).isInstanceOf(Seminar400::class.java)
    }
    
    @Test
    fun `세미나 수정 - 현재 인원보다 적은 capacity 로 수정하면 400을 반환한다`() {
        // given
        createFixtures()
        val updateSeminarDTO = UpdateSeminarDTO(name = null, capacity = 1, time = null, count = null, online = null)
        val seminar = seminarRepository.findByName("seminar#1")
        val instructor = userRepository.findByEmail("instructor@email.com") ?: throw Error()

        // when
        val thrown: Throwable = catchThrowable{ seminarService.updateSeminar(instructor, seminar.id, updateSeminarDTO) }

        // then
        assertThat(thrown).isInstanceOf(Seminar400::class.java)
    }
    
    @Test
    fun `세미나 참여 - participant 가 세미나를 참여할 수 있다`() {
        // given
        val participant: UserEntity = userTestHelper.createParticipant("participant@email.com", "", "", "", true)
        val joinSeminarDTO = JoinSeminarDTO(role = "participant")
        val seminar: SeminarEntity = seminarTestHelper.createSeminar("seminar#1", 100, 10, "00:00", true)
        
        // when
        seminarService.joinSeminar(participant, seminar.id, joinSeminarDTO)
        
        // then
        val userSeminarList: List<UserSeminarEntity> = userSeminarRepository.findAll()
        assertThat(userSeminarList).hasSize(1)
        assertThat(userSeminarList[0].user.id).isEqualTo(participant.id)
        assertThat(userSeminarList[0].seminar.id).isEqualTo(seminar.id)
    }

    @Test
    fun `세미나 참여 - 잘못된 role 로 세미나를 참여하면 400을 반환한다`() {
        // given
        val participant: UserEntity = userTestHelper.createParticipant("participant@email.com", "", "", "", true)
        val joinSeminarDTO = JoinSeminarDTO(role = "wrong")
        val seminar: SeminarEntity = seminarTestHelper.createSeminar("seminar#1", 100, 10, "00:00", true)

        // when
        val thrown: Throwable = Assertions.catchThrowable{ seminarService.joinSeminar(participant, seminar.id, joinSeminarDTO) }
        
        // then
        assertThat(thrown).isInstanceOf(Seminar400::class.java)
    }

    @Test
    fun `세미나 참여 - participant 가 동일한 세미나를 참여하면 400을 반환한다`() {
        // given
        val participant: UserEntity = userTestHelper.createParticipant("participant@email.com", "", "", "", true)
        val joinSeminarDTO = JoinSeminarDTO(role = "participant")
        val seminar: SeminarEntity = seminarTestHelper.createSeminar("seminar#1", 100, 10, "00:00", true)

        // when
        seminarService.joinSeminar(participant, seminar.id, joinSeminarDTO)
        val thrown: Throwable = Assertions.catchThrowable{ seminarService.joinSeminar(participant, seminar.id, joinSeminarDTO) }
        
        // then
        assertThat(thrown).isInstanceOf(Seminar400::class.java)
    }

    @Test
    fun `세미나 참여 - capacity 가 다 차면 세미나를 참여할 수 없다`() {
        // given
        val participant1: UserEntity = userTestHelper.createParticipant("participant#1@email.com", "", "", "", true)
        val participant2: UserEntity = userTestHelper.createParticipant("participant#2@email.com", "", "", "", true)
        val joinSeminarDTO = JoinSeminarDTO(role = "participant")
        val seminar: SeminarEntity = seminarTestHelper.createSeminar("seminar#1", 1, 10, "00:00", true)

        // when
        seminarService.joinSeminar(participant1, seminar.id, joinSeminarDTO)
        val thrown: Throwable = Assertions.catchThrowable{ seminarService.joinSeminar(participant2, seminar.id, joinSeminarDTO) }

        // then
        assertThat(thrown).isInstanceOf(Seminar400::class.java)
    }

    @Test
    fun `세미나 참여 - instructor 가 세미나를 참여할 수 있다`() {
        // given
        val instructor: UserEntity = userTestHelper.createInstructor("instructor@email.com", "", "", "", null)
        val joinSeminarDTO = JoinSeminarDTO(role = "instructor")
        val seminar: SeminarEntity = seminarTestHelper.createSeminar("seminar#1", 100, 10, "00:00", true)
        
        // when
        seminarService.joinSeminar(instructor, seminar.id, joinSeminarDTO)
        
        // then
        val userSeminarList: List<UserSeminarEntity> = userSeminarRepository.findAll()
        assertThat(userSeminarList).hasSize(1)
        assertThat(userSeminarList[0].user.id).isEqualTo(instructor.id)
        assertThat(userSeminarList[0].seminar.id).isEqualTo(seminar.id)
    }
    
    @Test
    fun `세미나 참여 - instructor 가 동일한 세미나를 참여하면 400을 반환한다`() {
        // given
        val instructor: UserEntity = userTestHelper.createInstructor("instructor@email.com", "", "", "", null)
        val joinSeminarDTO = JoinSeminarDTO(role = "instructor")
        val seminar: SeminarEntity = seminarTestHelper.createSeminar("seminar#1", 100, 10, "00:00", true)

        // when
        seminarService.joinSeminar(instructor, seminar.id, joinSeminarDTO)
        val thrown: Throwable = Assertions.catchThrowable{ seminarService.joinSeminar(instructor, seminar.id, joinSeminarDTO) }

        // then
        assertThat(thrown).isInstanceOf(Seminar400::class.java)
    }

    @Test
    fun `세미나 드랍 - participant 가 세미나를 드랍할 수 있다`() {
        val participant: UserEntity = userTestHelper.createParticipant("participant@email.com", "", "", "", true)
        val joinSeminarDTO = JoinSeminarDTO(role = "participant")
        val seminar: SeminarEntity = seminarTestHelper.createSeminar("seminar#1", 100, 10, "00:00", true)

        // when
        seminarService.joinSeminar(participant, seminar.id, joinSeminarDTO)
        seminarService.dropSeminar(participant, seminar.id)
        
        // then
        val userSeminarList: List<UserSeminarEntity> = userSeminarRepository.findAll()
        assertThat(userSeminarList).hasSize(1)
        assertThat(userSeminarList[0].user.id).isEqualTo(participant.id)
        assertThat(userSeminarList[0].seminar.id).isEqualTo(seminar.id)
        assertThat(userSeminarList[0].isActive).isFalse()
    }

    @Test
    fun `세미나 드랍 - 잘못된 seminar id 를 주면 400을 반환한다`() {
        val participant: UserEntity = userTestHelper.createParticipant("participant@email.com", "", "", "", true)
        val joinSeminarDTO = JoinSeminarDTO(role = "participant")
        val seminar: SeminarEntity = seminarTestHelper.createSeminar("seminar#1", 100, 10, "00:00", true)

        // when
        seminarService.joinSeminar(participant, seminar.id, joinSeminarDTO)
        val thrown: Throwable = Assertions.catchThrowable{ seminarService.dropSeminar(participant, seminar.id + 1) }
        
        // then
        assertThat(thrown).isInstanceOf(Seminar400::class.java)
    }

    @Test
    fun `세미나 드랍 - 참여하고 있지 않은 세미나면 400을 반환한다`() {
        val participant: UserEntity = userTestHelper.createParticipant("participant@email.com", "", "", "", true)
        val joinSeminarDTO = JoinSeminarDTO(role = "participant")
        val seminar: SeminarEntity = seminarTestHelper.createSeminar("seminar#1", 100, 10, "00:00", true)

        // when
        val thrown: Throwable = Assertions.catchThrowable{ seminarService.dropSeminar(participant, seminar.id + 1) }

        // then
        assertThat(thrown).isInstanceOf(Seminar400::class.java)
    }
    
    @Test
    fun `세미나 드랍 - participant 가 세미나를 두 번 드랍할 수 없다`() {
        val participant: UserEntity = userTestHelper.createParticipant("participant@email.com", "", "", "", true)
        val joinSeminarDTO = JoinSeminarDTO(role = "participant")
        val seminar: SeminarEntity = seminarTestHelper.createSeminar("seminar#1", 100, 10, "00:00", true)

        // when
        seminarService.joinSeminar(participant, seminar.id, joinSeminarDTO)
        seminarService.dropSeminar(participant, seminar.id)
        val thrown: Throwable = Assertions.catchThrowable{ seminarService.dropSeminar(participant, seminar.id) }

        // then
        assertThat(thrown).isInstanceOf(Seminar400::class.java)
    }

    @Test
    fun `세미나 드랍 - 마지막 instructor 는 세미나를 드랍할 수 없다`() {
        val instructor: UserEntity = userTestHelper.createInstructor("instructor@email.com", "", "","", null)
        val joinSeminarDTO = JoinSeminarDTO(role = "instructor")
        val seminar: SeminarEntity = seminarTestHelper.createSeminar("seminar#1", 100, 10, "00:00", true)

        // when
        seminarService.joinSeminar(instructor, seminar.id, joinSeminarDTO)
        val thrown: Throwable = Assertions.catchThrowable{ seminarService.dropSeminar(instructor, seminar.id) }

        // then
        assertThat(thrown).isInstanceOf(Seminar400::class.java)
    }
    
    private fun createFixtures() {
        val seminar = seminarTestHelper.createSeminar("seminar#1")
        val seminar2 = seminarTestHelper.createSeminar("seminar#2")
        val instructor = userTestHelper.createInstructor("instructor@email.com")
        val instructorUserSeminar = UserSeminarEntity(user = instructor, seminar = seminar, role = "instructor")
        userSeminarRepository.save(instructorUserSeminar)
        instructor.userSeminars.add(instructorUserSeminar)
        seminar.userSeminars.add(instructorUserSeminar)

        val participants = (1..10).map { userTestHelper.createParticipant("participant#$it@email.com") }
        participants.map {
            val participantUserSeminar = UserSeminarEntity(user = it, seminar = seminar, role = "participant")
            it.userSeminars.add(participantUserSeminar)
            seminar.userSeminars.add(participantUserSeminar)
            userSeminarRepository.save(participantUserSeminar)
        }
    }
}