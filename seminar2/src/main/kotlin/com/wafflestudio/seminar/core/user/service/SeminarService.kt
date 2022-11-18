package com.wafflestudio.seminar.core.user.service

import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import com.wafflestudio.seminar.common.Seminar400
import com.wafflestudio.seminar.common.Seminar403
import com.wafflestudio.seminar.common.Seminar404
import com.wafflestudio.seminar.core.user.domain.QSeminarEntity.seminarEntity
import com.wafflestudio.seminar.core.user.domain.QUserSeminarEntity.userSeminarEntity
import com.wafflestudio.seminar.core.user.domain.QUserEntity.userEntity
import com.wafflestudio.seminar.core.user.api.request.SeminarRequest
import com.wafflestudio.seminar.core.user.api.response.*
import com.wafflestudio.seminar.core.user.database.*
//import com.wafflestudio.seminar.core.user.database.QUserEntity
import com.wafflestudio.seminar.core.user.domain.*
import com.wafflestudio.seminar.core.user.dto.seminar.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*
import javax.transaction.Transactional


@Service
class SeminarService(
        private val authTokenService: AuthTokenService,
        private val seminarRepository: SeminarRepository,
        private val userSeminarRepository: UserSeminarRepository,
        private val userRepository: UserRepository,
        private val queryFactory: JPAQueryFactory,
        private val seminarDslRepository: SeminarDslRepository,
        private val userSeminarDslRepository: UserSeminarDslRepository
) {


    @Transactional
    // Query Count 예상: 8, 실제: 10
    fun createSeminar(seminar: SeminarRequest, userId: Long): GetSeminarInfo {
        if (seminar.name == null || seminar.capacity == null || seminar.count == null || seminar.time == null) {
            throw Seminar400("입력하지 않은 값이 있습니다")


        } else {
            if (seminar.name == "" || seminar.capacity <= 0 || seminar.count <= 0) {
                throw Seminar400("형식에 맞지 않게 입력하지 않은 값이 있습니다")
            }
        }

        // Query #1 -> [N+1] but was 2: fetching instructor profile
        if (userRepository.findByIdOrNull(userId)?.instructor == null) {
            throw Seminar403("진행자만 세미나를 생성할 수 있습니다")
        }


        // Query #2
        val saveSeminarEntity = seminarRepository.save(SeminarEntity(seminar, userId))
        // Query #3, #4, #5 -> [N+1] but was 4:  fetching instructor profile
        userSeminarRepository.save(userSeminarInstructorEntity(seminar, userId))
        // Query #6, #7

        val seminarInfoDto = seminarDslRepository.getListById(saveSeminarEntity.id)
        val teacherDto = queryFactory.select(Projections.constructor(
                TeacherDto::class.java,
                userEntity.id, userEntity.username, userEntity.email, userSeminarEntity.joinedAt
        )).from(userEntity)
                .innerJoin(userSeminarEntity).on(userSeminarEntity.user.id.eq(userEntity.id))
                .where(userSeminarEntity.seminar.name.eq(seminar.name), userSeminarEntity.role.eq("INSTRUCTOR")).fetch()

        val seminarEntity = seminarInfoDto[0]

        // Query #8
        // companion object와 queryDSL의 projection을 함께 사용하여 보다 간결하게 작성할 수 있을 것 같습니다.
        // 과제 레포의 (branch: asmt2) Seminar.of 및 SeminarEntity.toDto()를 참고하시면 좋을 것 같습니다.
        return GetSeminarInfo.of(seminarEntity, teacherDto, emptyList())

    }


    // Query Count 예상: 4, 실제: 5
    @Transactional
    fun updateSeminar(seminar: SeminarRequest, userId: Long): UpdateSeminarInfo {

        if (seminar.name == null || seminar.capacity == null || seminar.count == null || seminar.time == null) {
            throw Seminar400("입력하지 않은 값이 있습니다")


        } else {
            if (seminar.name == "" || seminar.capacity <= 0 || seminar.count <= 0) {
                throw Seminar400("형식에 맞지 않게 입력하지 않은 값이 있습니다")
            }
        }

        // Query #1 -> [N+1] but was 2: fetching instructor profile
        if (userRepository.findByIdOrNull(userId)?.instructor == null) {
            throw Seminar403("세미나를 수정할 자격이 없습니다")
        }


        // Query #2

        val seminarEntity = seminarRepository.findByName(seminar.name)

        if (userSeminarRepository.findAll().none { it.user?.id == userId }) {
            throw Seminar403("진행자만 세미나를 생성할 수 있습니다")
        }

        seminarEntity.let {
            it.name = seminar.name
            it.capacity = seminar.capacity
            it.count = seminar.count
            it.time = seminar.time
            it.online = seminar.online
        }

        // Query #3, #4
        // query count와는 상관없는 얘기지만, @Transactional을 사용하여 save문을 생략하실 수도 있습니다.

        return UpdateSeminarInfo.of(seminarEntity)
    }

    // Query Count 예상: 4, 실제: 15
    @Transactional
    fun getSeminarById(id: Long): GetSeminarInfo {
        // Query #1
        if (seminarRepository.findById(id).isEmpty) {
            throw Seminar404("해당하는 세미나가 없습니다")
        }
        // Query #2

        val seminarList = seminarDslRepository.getListById(id)

        val seminarEntity = seminarList[0]

        // Query #3 -> [N+1] but was 2: fetch join을 사용하지 않았습니다.
        // 과제 레포의 (branch: asmt2) findAllWithProfiles()를 참고하시면 좋을 것 같습니다.
        val teacherDto = queryFactory.select(Projections.constructor(
                TeacherDto::class.java,
                userEntity.id, userEntity.username, userEntity.email, userSeminarEntity.joinedAt
        ))
                .from(userEntity)
                .innerJoin(userSeminarEntity).on(userEntity.id.eq(userSeminarEntity.user.id)).fetchJoin()
                .where(userSeminarEntity.seminar.id.eq(id), userSeminarEntity.role.eq("INSTRUCTOR"))
                .fetch()

        
        // Query #4 -> [N+1] but was 11: fetch join을 사용하지 않았습니다.
        val studentDto = queryFactory.select(Projections.constructor(
                StudentDto::class.java,
                userEntity.id, userEntity.username, userEntity.email,
                userSeminarEntity.joinedAt, userSeminarEntity.isActive, userSeminarEntity.droppedAt
        ))
                .from(userEntity)
                .innerJoin(userSeminarEntity).on(userSeminarEntity.user.id.eq(userEntity.id)).fetchJoin()
                .where(userSeminarEntity.seminar.id.eq(id), userSeminarEntity.role.eq("PARTICIPANT"))
                .fetch()

        return GetSeminarInfo.of(seminarEntity, teacherDto, studentDto)


    }

    // Query Count 예상: 1, 실제: 1
    @Transactional
    fun getSeminarList(name: String?, order: String?): List<GetSeminarInfo> {
        // Query #1

        val seminarList = seminarDslRepository.getListByNameAndOrder(name, order)
        val teacherList = userSeminarDslRepository.getInstructorList(name, order)
        val studentList = userSeminarDslRepository.getParticipantList(name, order)
        
        val newTeacherList = teacherList.groupBy { it -> it[userSeminarEntity.seminar.id] }
        val newStudentList = studentList.groupBy { it -> it[userSeminarEntity.seminar.id] }

        return seminarList.map { seminarEntity ->

            val instructors = newTeacherList?.filter { it.key == seminarEntity.id }?.getOrDefault(seminarEntity.id, null)
            val teacherDto = instructors?.map { TeacherDto(it[userEntity.id], it[userEntity.username], it[userEntity.email], it[userSeminarEntity.joinedAt]) }
            val participants = newStudentList?.filter { it.key == seminarEntity.id }?.getOrDefault(seminarEntity.id, null)
            val studentDto = participants?.map { StudentDto(it[userEntity.id], it[userEntity.username], it[userEntity.email], it[userSeminarEntity.joinedAt], it[userSeminarEntity.isActive], it[userSeminarEntity.droppedAt]) }

            GetSeminarInfo.of(seminarEntity, teacherDto, studentDto)
        }

    }

    @Transactional
    // Query Count 예상: 17, 실제: 29
    fun joinSeminar(id: Long, role: Map<String, String>, userId: Long): GetSeminarInfo {

        // Query #1
        val seminar = seminarRepository.findByIdOrNull(id) ?: throw Seminar404("해당하는 세미나가 없습니다.")
        // Query #2
        val user = userRepository.findByIdOrNull(userId)
                ?: throw Seminar403("등록되어 있지 않습니다")

        // Query #3 -> [N+1]
        if (!userSeminarRepository.findByUser(user)?.filter { it.seminar.id == id && it.isActive == true }.isNullOrEmpty()) {
            throw Seminar400("이미 세미나에 참여하고 있습니다")
        }

        // Query #4
        if (!userSeminarRepository.findByUser(user)?.filter { it.isActive == false }.isNullOrEmpty()) {
            throw Seminar400("드랍한 세미나는 다시 신청할 수 없습니다")
        }

        val saveUserSeminarEntity: UserSeminarEntity

        if (role["role"] == "PARTICIPANT") {

            if (user.participant == null) {
                throw Seminar403("참가자로 등록되어 있지 않습니다")

            } else {
                if (user.participant?.isRegistered == false) {
                    throw Seminar403("활성회원이 아닙니다")
                }
            }
            // Query #5
            saveUserSeminarEntity = userSeminarRepository.save(
                    UserSeminarEntity.participant(user, seminar)
            )


        } else if (role["role"] == "INSTRUCTOR") {
            if (user.instructor == null) {
                throw Seminar403("진행자로 등록되어 있지 않습니다")
            }
            if (!userSeminarRepository.findByUser(user).isNullOrEmpty()) {
                throw Seminar400("이미 다른 세미나를 진행하고 있습니다.")
            }

            saveUserSeminarEntity = userSeminarRepository.save(
                    UserSeminarEntity.instructor(user, seminar)
            )

        } else {
            throw Seminar400("진행자 혹은 수강자가 아닙니다.")
        }


        // Query #9 -> [N+1] but was 2

        val teacherDto = queryFactory.select(Projections.constructor(
                TeacherDto::class.java,
                userEntity.id, userEntity.username, userEntity.email, userSeminarEntity.joinedAt
        ))
                .from(userEntity)
                .innerJoin(userSeminarEntity).on(userEntity.id.eq(userSeminarEntity.user.id)).fetchJoin()
                .where(userSeminarEntity.seminar.id.eq(id), userSeminarEntity.role.eq("INSTRUCTOR"))
                .fetch()

        val studentDto = queryFactory.select(Projections.constructor(
                StudentDto::class.java,
                userEntity.id, userEntity.username, userEntity.email,
                userSeminarEntity.joinedAt, userSeminarEntity.isActive, userSeminarEntity.droppedAt
        ))
                .from(userEntity)
                .innerJoin(userSeminarEntity).on(userSeminarEntity.user.id.eq(userEntity.id)).fetchJoin()
                .where(userSeminarEntity.seminar.id.eq(id), userSeminarEntity.role.eq("PARTICIPANT"))
                .fetch()


        // Query #11
        if (studentDto.size > seminarRepository.findById(id).get().capacity!!) {
            userSeminarRepository.delete(saveUserSeminarEntity)
            throw Seminar400("세미나의 인원이 다 찼습니다")
        }

        // Query #12 ~ 17
        return GetSeminarInfo.of(seminar, teacherDto, studentDto)
    }


    // Query Count 예상: 7, 실제: 19
    @Transactional
    fun dropSeminar(id: Long, userId: Long): GetSeminarInfo {
        // Query #1 -> [N+1] but was 2: fetching participant profile
        val findByEmailEntity = userRepository.findByIdOrNull(userId)

        if (findByEmailEntity?.let {
                    // Query #2
                    userSeminarRepository.findByUser(it)?.filter {
                        it.seminar.id == id
                    }
                }.isNullOrEmpty()) {
            throw Seminar404("해당 세미나를 신청한 적이 없습니다")
        }

        if (findByEmailEntity?.let {
                    // Query #3
                    userSeminarRepository.findByUser(it)?.filter {
                        it.role == "INSTRUCTOR"
                    }
                } != emptyList<UserSeminarEntity>()) {
            throw Seminar403("진행자는 세미나를 드랍할 수 없습니다")
        }
        
        // Query #5, #6
        // Query #7 -> [N+1] but was 11
        val seminar = seminarRepository.findByIdOrNull(id) ?: throw Seminar404("")
        val userSeminars = userSeminarRepository.findAllBySeminar(seminar)
        val userSeminar = userSeminars?.find { it.user?.id == userId }
        userSeminar?.isActive = false
        userSeminar?.droppedAt = LocalDateTime.now()

        val seminarList = seminarDslRepository.getListById(id)
        val seminarEntity = seminarList[0]

        val teacherDto = queryFactory.select(Projections.constructor(
                TeacherDto::class.java,
                userEntity.id, userEntity.username, userEntity.email, userSeminarEntity.joinedAt
        ))
                .from(userEntity)
                .innerJoin(userSeminarEntity).on(userEntity.id.eq(userSeminarEntity.user.id)).fetchJoin()
                .where(userSeminarEntity.seminar.id.eq(id), userSeminarEntity.role.eq("INSTRUCTOR"))
                .fetch()

        val studentDto = queryFactory.select(Projections.constructor(
                StudentDto::class.java,
                userEntity.id, userEntity.username, userEntity.email,
                userSeminarEntity.joinedAt, userSeminarEntity.isActive, userSeminarEntity.droppedAt
        ))
                .from(userEntity)
                .innerJoin(userSeminarEntity).on(userSeminarEntity.user.id.eq(userEntity.id)).fetchJoin()
                .where(userSeminarEntity.seminar.id.eq(id), userSeminarEntity.role.eq("PARTICIPANT"))
                .fetch()

        return GetSeminarInfo.of(seminarEntity, teacherDto, studentDto)
    }


    private fun SeminarEntity(seminar: SeminarRequest, userId: Long) = seminar.run {
        com.wafflestudio.seminar.core.user.domain.SeminarEntity(
                name = seminar.name,
                capacity = seminar.capacity,
                count = seminar.count,
                time = seminar.time,//LocalTime.parse(seminar.time, DateTimeFormatter.ISO_TIME),
                online = true,
        )
    }

    private fun userSeminarInstructorEntity(seminar: SeminarRequest, userId: Long): UserSeminarEntity {
        return UserSeminarEntity(
                user = userRepository.findByIdOrNull(userId),
                seminar = seminarRepository.findByName(seminar.name),
                role = "INSTRUCTOR",
                joinedAt = LocalDateTime.now(),
                isActive = true,
                droppedAt = null
        )
    }
}
