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


//    private final val qSeminarEntity: QSeminarEntity = QSeminarEntity.seminarEntity
//    private final val qUserSeminarEntity: QUserSeminarEntity = QUserSeminarEntity.userSeminarEntity
//    private final val qUserEntity: QUserEntity = QUserEntity.userEntity


    @Transactional
    // Query Count 예상: 8, 실제: 10
    fun createSeminar(seminar: SeminarRequest, token: String): GetSeminarInfo {
        if (seminar.name == null || seminar.capacity == null || seminar.count == null || seminar.time == null) {
            throw Seminar400("입력하지 않은 값이 있습니다")


        } else {
            if (seminar.name == "" || seminar.capacity <= 0 || seminar.count <= 0) {
                throw Seminar400("형식에 맞지 않게 입력하지 않은 값이 있습니다")
            }
        }

        // Query #1 -> [N+1] but was 2: fetching instructor profile
        if (userRepository.findByEmail(authTokenService.getCurrentEmail(token))?.instructor == null) {
            throw Seminar403("진행자만 세미나를 생성할 수 있습니다")
        }


        // Query #2
        val saveSeminarEntity = seminarRepository.save(SeminarEntity(seminar, token))

        // Query #3, #4, #5 -> [N+1] but was 4:  fetching instructor profile
        userSeminarRepository.save(userSeminarInstructorEntity(seminar, token))

        // Query #6, #7

        val seminarInfoDto = queryFactory.select(seminarEntity).from(seminarEntity)
                .where(seminarEntity.id.eq(saveSeminarEntity.id)).fetch()


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
    fun updateSeminar(seminar: SeminarRequest, token: String): UpdateSeminarInfo {

        if (seminar.name == null || seminar.capacity == null || seminar.count == null || seminar.time == null) {
            throw Seminar400("입력하지 않은 값이 있습니다")


        } else {
            if (seminar.name == "" || seminar.capacity <= 0 || seminar.count <= 0) {
                throw Seminar400("형식에 맞지 않게 입력하지 않은 값이 있습니다")
            }
        }

        // Query #1 -> [N+1] but was 2: fetching instructor profile
        if (userRepository.findByEmail(authTokenService.getCurrentEmail(token))?.instructor == null) {
            throw Seminar403("세미나를 수정할 자격이 없습니다")
        }


        // Query #2

        val seminarEntity = seminarRepository.findByName(seminar.name)

        if (userSeminarRepository.findAll().none { it.user?.email == authTokenService.getCurrentEmail(token) }) {
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
    fun getSeminarById(id: Long, token: String): GetSeminarInfo {
        // Query #1
        if (seminarRepository.findById(id).isEmpty) {
            throw Seminar404("해당하는 세미나가 없습니다")
        }
        // Query #2

        val seminarList = queryFactory.select(seminarEntity).from(seminarEntity)
                .where(seminarEntity.id.eq(id)).fetch()

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


        /*
        * instructorList는 UserSeminar Table에서 UserEntity를 inner join하여 fetch하는 반면
        * participantList는 Seminar Table에서 UserSeminarEntity를 inner join하고
        * 다시 UserSeminarEntity에 대해 UserEntity를 inner join하고 있습니다.
        * participantList도 같은 방식으로 UserSeminar Table에서 UserEntity를 inner join하여 바로 fetch해도 될 것 같습니다.
        */
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
    fun getSeminarList(name: String?, order: String?, token: String): List<GetSeminarInfo> {
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
        /*
    return seminarList.map { 
        seminarEntity ->
        val seminarUser = seminarEntity.userSeminars
        println("1")
        val instructors = seminarUser?.filter { it.role == "INSTRUCTOR" }?.map { 
            TeacherDto(it.user?.id, it.user?.username, it.user?.email, it.joinedAt)
        } ?: throw Seminar400("가르치는 사람이 없음")
        println("2")
        val participants = seminarUser?.filter { it.role == "PARTICIPANT" }?.map {
//                StudentDto(
//                        it.user?.id, it.user?.username, it.user?.email,
//                        it.joinedAt, it.isActive, it.droppedAt
//                )
            
           StudentDto.of(it.user!!, it)
        } ?: throw Seminar400("배우는 사람이 없음")
        println("3")
        GetSeminarInfo.of(seminarEntity, instructors,participants)
        
         
    }*/


        /*
        val seminars = mutableListOf<GetSeminars>()

        for (i in 0 until seminarList.size) {
            val seminarEntity = seminarList[i]
            seminars.add(
                    GetSeminars(
                            seminarEntity?.id,
                            seminarEntity?.name,
                            seminarEntity?.capacity,
                            seminarEntity?.count,
                            seminarEntity?.time,
                            seminarEntity?.online
                    )
            )
        }
        return emptyList()
        
         */


    }

    @Transactional
    // Query Count 예상: 17, 실제: 29
    fun joinSeminar(id: Long, role: Map<String, String>, token: String): JoinSeminarInfo {

        // Query #1
        val seminarFindByIdEntity = seminarRepository.findByIdOrNull(id) ?: throw Seminar404("해당하는 세미나가 없습니다.")
        // Query #2
        val userFindByIdEntity = userRepository.findByIdOrNull(authTokenService.getCurrentUserId(token))
                ?: throw Seminar403("등록되어 있지 않습니다")

        // Query #3 -> [N+1]
        if (!userSeminarRepository.findByUser(userFindByIdEntity)?.filter { it.seminar.id == id && it.isActive == true }.isNullOrEmpty()) {
            throw Seminar400("이미 세미나에 참여하고 있습니다")
        }


        // Query #4
        if (!userSeminarRepository.findByUser(userFindByIdEntity)?.filter { it.isActive == false }.isNullOrEmpty()) {
            throw Seminar400("드랍한 세미나는 다시 신청할 수 없습니다")
        }
        val saveUserSeminarEntity: UserSeminarEntity

        if (role["role"] == "PARTICIPANT") {

            if (userFindByIdEntity.participant == null) {
                throw Seminar403("참가자로 등록되어 있지 않습니다")
                // TODO 비활성(미등록) 사용자의 경우 400이 아니라 403으로 응답해야 합니다.

            } else {
                if (userFindByIdEntity.participant?.isRegistered == false) {
                    throw Seminar403("활성회원이 아닙니다")
                }
            }
            // Query #5
            saveUserSeminarEntity = userSeminarRepository.save(
                    UserSeminarEntity(
                            // Query #6
                            // 불필요한 query인 것 같습니다.
                            // 위에서 이미 구해놓으신 userFindByIdEntity를 사용하시면 될 것 같습니다.
                            user = userFindByIdEntity,
                            // Query #7
                            // 마찬가지로 seminarFindByIdEntity를 사용하시면 될 것 같습니다.
                            seminar = seminarFindByIdEntity,
                            role = "PARTICIPANT",
                            joinedAt = LocalDateTime.now(),
                            isActive = true,
                            droppedAt = null
                    )
            )


        } else if (role["role"] == "INSTRUCTOR") {
            if (userFindByIdEntity.instructor == null) {
                throw Seminar403("진행자로 등록되어 있지 않습니다")
            }
            if (!userSeminarRepository.findByUser(userFindByIdEntity).isNullOrEmpty()) {
                throw Seminar400("이미 다른 세미나를 진행하고 있습니다.")
            }

            saveUserSeminarEntity = userSeminarRepository.save(
                    UserSeminarEntity(
                            user = userFindByIdEntity,
                            seminar = seminarFindByIdEntity,
                            role = "INSTRUCTOR",
                            joinedAt = LocalDateTime.now(),
                            isActive = null,
                            droppedAt = null
                    )
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
        /*
        val instructorList = queryFactory.select(Projections.constructor(
                UserSeminarAndUserDto::class.java,
                userSeminarEntity,
                userEntity
        ))
                .from(userSeminarEntity)
                .innerJoin(userEntity).on(userSeminarEntity.user.id.eq(userEntity.id))
                .where(userSeminarEntity.seminar.id.eq(id))
                .where(userSeminarEntity.role.eq("INSTRUCTOR")).fetch()


        val teacherList = mutableListOf<TeacherDto>()

        for (i in 0 until instructorList.size) {
            val teacherEntity = instructorList[i].userEntity
            val teacherSeminarEntity = instructorList[i].userSeminarEntity
            teacherList.add(
                    TeacherDto(
                            teacherEntity?.id,
                            teacherEntity?.username,
                            teacherEntity?.email,
                            teacherSeminarEntity?.joinedAt
                    )
            )
        }
        
         */
        val studentDto = queryFactory.select(Projections.constructor(
                StudentDto::class.java,
                userEntity.id, userEntity.username, userEntity.email,
                userSeminarEntity.joinedAt, userSeminarEntity.isActive, userSeminarEntity.droppedAt
        ))
                .from(userEntity)
                .innerJoin(userSeminarEntity).on(userSeminarEntity.user.id.eq(userEntity.id)).fetchJoin()
                .where(userSeminarEntity.seminar.id.eq(id), userSeminarEntity.role.eq("PARTICIPANT"))
                .fetch()
        /*
        // Query #10 -> [N+1] but was 11
        val participantList = queryFactory.select(Projections.constructor(
                SeminarInfoDto::class.java,
                seminarEntity,
                userSeminarEntity,
                userEntity
        ))
                .from(seminarEntity)
                .innerJoin(userSeminarEntity).on(seminarEntity.id.eq(userSeminarEntity.seminar.id))
                .innerJoin(userEntity).on(userSeminarEntity.user.id.eq(userEntity.id)).where(seminarEntity.id.eq(id))
                .where(userSeminarEntity.role.eq("PARTICIPANT")).fetch()


        val studentList = mutableListOf<StudentDto>()

        for (i in 0 until participantList.size) {
            val studentEntity = participantList[i].userEntity ?: throw Seminar400("없음")
            val studentSeminarEntity = participantList[i].userSeminarEntity ?: throw Seminar400("나도 없음")
            studentList.add(
                    /*
                StudentDto(
                    studentEntity?.id,
                    studentEntity?.username,
                    studentEntity?.email,
                    studentSeminarEntity?.joinedAt,
                    studentSeminarEntity?.isActive,
                    studentSeminarEntity?.droppedAt
                    
                )
                
                     */
                    StudentDto.of(studentEntity, studentSeminarEntity)
            )
        }
        
         */

        // Query #11
        if (studentDto.size > seminarRepository.findById(id).get().capacity!!) {
            userSeminarRepository.delete(saveUserSeminarEntity)
            throw Seminar400("세미나의 인원이 다 찼습니다")
        }

        // Query #12 ~ 17
        return JoinSeminarInfo(
                id = seminarRepository.findById(id).get().id,
                name = seminarRepository.findById(id).get().name,
                capacity = seminarRepository.findById(id).get().capacity,
                count = seminarRepository.findById(id).get().count,
                time = seminarRepository.findById(id).get().time,
                online = seminarRepository.findById(id).get().online,
                instructors = teacherDto,

                participants = studentDto
        )
    }
    
 

    // Query Count 예상: 7, 실제: 19
    @Transactional
    fun dropSeminar(id: Long, token: String): GetSeminarInfo {
        // Query #1 -> [N+1] but was 2: fetching participant profile
        val findByEmailEntity = userRepository.findByEmail(authTokenService.getCurrentEmail(token))

        if (findByEmailEntity?.let {
                    // Query #2
                    userSeminarRepository.findByUser(it)?.filter {
                        it.seminar.id == id
                    }
                } == emptyList<UserSeminarEntity>()) {
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
        // Query #4 -> [N+1] but was 2: fetching participant profile
        val seminarInfoDto = queryFactory.select(Projections.constructor(
                SeminarInfoDto::class.java,
                seminarEntity,
                userSeminarEntity,
                userEntity
        ))
                .from(seminarEntity)
                .innerJoin(userSeminarEntity).on(seminarEntity.id.eq(userSeminarEntity.seminar.id))
                .innerJoin(userEntity).on(userSeminarEntity.user.id.eq(userEntity.id))
                .where(seminarEntity.id.eq(id))
                .where(userEntity.email.eq(authTokenService.getCurrentEmail(token))).fetch()

        val seminarEntity1 = seminarInfoDto[0].seminarEntity
        var userSeminarEntity1 = seminarInfoDto[0].userSeminarEntity
        val userEntity1 = seminarInfoDto[0].userEntity

        userSeminarEntity1?.isActive = false
        userSeminarEntity1?.droppedAt = LocalDateTime.now()
        // Query #5, #6
        userSeminarEntity1?.let { userSeminarRepository.save(it) }
        // Query #7 -> [N+1] but was 11
        val studentList = queryFactory.select(Projections.constructor(
                SeminarInfoDto::class.java,
                seminarEntity,
                userSeminarEntity,
                userEntity
        ))
                .from(seminarEntity)
                .innerJoin(userSeminarEntity).on(seminarEntity.id.eq(userSeminarEntity.seminar.id))
                .innerJoin(userEntity).on(userSeminarEntity.user.id.eq(userEntity.id)).where(seminarEntity.id.eq(id)).where(userSeminarEntity.role.eq("PARTICIPANT")).fetch()

        val newList = mutableListOf<StudentDto>()

        for (i in 0 until studentList.size) {
            val studentEntity = studentList[i].userEntity!!
            val studentSeminarEntity = studentList[i].userSeminarEntity!!
            newList.add(
                    /*
                StudentDto(
                    studentEntity?.id,
                    studentEntity?.username,
                    studentEntity?.email,
                    studentSeminarEntity?.joinedAt,
                    studentSeminarEntity?.isActive,
                    studentSeminarEntity?.droppedAt
                )
                
                     */
                    StudentDto.of(studentEntity, studentSeminarEntity)
            )
        }

        return GetSeminarInfo(
                seminarEntity1?.id,
                seminarEntity1?.name,
                seminarEntity1?.capacity,
                seminarEntity1?.count,
                seminarEntity1?.time,
                seminarEntity1?.online,
                List(seminarInfoDto.size) { _ ->
                    TeacherDto(
                            userEntity1?.id,
                            userEntity1?.username,
                            userEntity1?.email,
                            userSeminarEntity1?.joinedAt
                    )
                }, newList
        )
    }
    
 


    private fun SeminarEntity(seminar: SeminarRequest, token: String) = seminar.run {
        com.wafflestudio.seminar.core.user.domain.SeminarEntity(
                name = seminar.name,
                capacity = seminar.capacity,
                count = seminar.count,
                time = seminar.time,//LocalTime.parse(seminar.time, DateTimeFormatter.ISO_TIME),
                online = true,
        )
    }

    private fun userSeminarInstructorEntity(seminar: SeminarRequest, token: String): UserSeminarEntity {
        return UserSeminarEntity(
                user = userRepository.findByEmail(authTokenService.getCurrentEmail(token)),
                seminar = seminarRepository.findByName(seminar.name),
                role = "INSTRUCTOR",
                joinedAt = LocalDateTime.now(),
                isActive = true,
                droppedAt = null
        )
    }
}
