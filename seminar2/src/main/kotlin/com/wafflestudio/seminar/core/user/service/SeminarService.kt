package com.wafflestudio.seminar.core.user.service

import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import com.wafflestudio.seminar.common.Seminar400
import com.wafflestudio.seminar.common.Seminar403
import com.wafflestudio.seminar.common.Seminar404
import com.wafflestudio.seminar.core.user.api.request.SeminarRequest
import com.wafflestudio.seminar.core.user.api.response.*
import com.wafflestudio.seminar.core.user.database.SeminarRepository
import com.wafflestudio.seminar.core.user.database.UserRepository
import com.wafflestudio.seminar.core.user.database.UserSeminarRepository
import com.wafflestudio.seminar.core.user.domain.*
import com.wafflestudio.seminar.core.user.dto.seminar.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*


@Service
class SeminarService(
    private val authTokenService: AuthTokenService,
    private val seminarRepository: SeminarRepository,
    private val userSeminarRepository: UserSeminarRepository,
    private val userRepository: UserRepository,
    private val queryFactory: JPAQueryFactory,
) {


    private final val qSeminarEntity: QSeminarEntity = QSeminarEntity.seminarEntity
    private final val qUserSeminarEntity: QUserSeminarEntity = QUserSeminarEntity.userSeminarEntity
    private final val qUserEntity: QUserEntity = QUserEntity.userEntity

    
    
    // Query Count 예상: 8, 실제: 10
    fun createSeminar(seminar: SeminarRequest, token: String): GetSeminarInfo {
        //todo: online 여부 외에는 하나라도 빠지면 400으로 응답하며, 적절한 에러 메시지를 포함합니다.
        //todo: name에 0글자가 오는 경우, capacity와 count에 양의 정수가 아닌 값이 오는 경우는 400으로 응답합니다.
        //todo: 세미나 진행자 자격을 가진 User만 요청할 수 있으며, 그렇지 않은 경우 403으로 응답
        if(seminar.name == null || seminar.capacity == null || seminar.count == null || seminar.time == null ) {
            throw Seminar400("입력하지 않은 값이 있습니다")
            
            
        } else {
            if(seminar.name == "" || seminar.capacity <= 0 || seminar.count <= 0) {
                throw Seminar400("형식에 맞지 않게 입력하지 않은 값이 있습니다")
            }
        }

        // Query #1 -> [N+1] but was 2: fetching instructor profile
        if(userRepository.findByEmail(authTokenService.getCurrentEmail(token))?.instructor == null) {
            throw Seminar403("진행자만 세미나를 생성할 수 있습니다")
        }

        // Query #2
        val saveSeminarEntity = seminarRepository.save(SeminarEntity(seminar, token))
        // Query #3, #4, #5 -> [N+1] but was 4:  fetching instructor profile
        userSeminarRepository.save(userSeminarInstructorEntity(seminar, token))

        // Query #6, #7
        val seminarInfoDto = queryFactory.select(Projections.constructor(
            SeminarInfoDto::class.java,
            qSeminarEntity,
            qUserSeminarEntity,
            qUserEntity
        ))
            .from(qSeminarEntity)
            .innerJoin(qUserSeminarEntity).on(qSeminarEntity.id.eq(qUserSeminarEntity.seminar.id))
            .innerJoin(qUserEntity).on(qUserSeminarEntity.user.id.eq(qUserEntity.id)).where(qSeminarEntity.id.eq(saveSeminarEntity.id))
            .where(qUserSeminarEntity.role.eq("instructor")).fetch()
        

        val seminarEntity = seminarInfoDto[0].seminarEntity
        val userSeminarEntity = seminarInfoDto[0].userSeminarEntity
        val userEntity = seminarInfoDto[0].userEntity

        // Query #8
        val studentList = queryFactory.select(Projections.constructor(
            SeminarInfoDto::class.java,
            qSeminarEntity,
            qUserSeminarEntity,
            qUserEntity
        ))
            .from(qSeminarEntity)
            .innerJoin(qUserSeminarEntity).on(qSeminarEntity.id.eq(qUserSeminarEntity.seminar.id))
            .innerJoin(qUserEntity).on(qUserSeminarEntity.user.id.eq(qUserEntity.id)).where(qSeminarEntity.name.eq(seminar.name)).where(qUserSeminarEntity.role.eq("participant")).fetch()

        val newList = mutableListOf<StudentDto>()

        for(i in 0 until studentList.size){
            val studentEntity = studentList[i].userEntity
            val studentSeminarEntity = studentList[i].userSeminarEntity
            newList.add(
                // companion object와 queryDSL의 projection을 함께 사용하여 보다 간결하게 작성할 수 있을 것 같습니다.
                // 과제 레포의 (branch: asmt2) Seminar.of 및 SeminarEntity.toDto()를 참고하시면 좋을 것 같습니다.
                StudentDto(
                    studentEntity?.id,
                    studentEntity?.username,
                    studentEntity?.email,
                    studentSeminarEntity?.joinedAt,
                    studentSeminarEntity?.isActive,
                    studentSeminarEntity?.droppedAt

                )
            )
        }

        // companion object와 queryDSL의 projection을 함께 사용하여 보다 간결하게 작성할 수 있을 것 같습니다.
        // 과제 레포의 (branch: asmt2) Seminar.of 및 SeminarEntity.toDto()를 참고하시면 좋을 것 같습니다.
        return GetSeminarInfo(
            seminarEntity?.id,
            seminarEntity?.name,
            seminarEntity?.capacity,
            seminarEntity?.count,
            seminarEntity?.time,
            seminarEntity?.online,
            List(seminarInfoDto.size) { _ ->
                TeacherDto(
                    userEntity?.id,
                    userEntity?.username,
                    userEntity?.email,
                    userSeminarEntity?.joinedAt
                )

            },newList

        )
    }

    // Query Count 예상: 4, 실제: 5
    fun updateSeminar(seminar: SeminarRequest, token: String): UpdateSeminarInfo {

        if(seminar.name == null || seminar.capacity == null || seminar.count == null || seminar.time == null ) {
            throw Seminar400("입력하지 않은 값이 있습니다")


        } else {
            if(seminar.name == "" || seminar.capacity <= 0 || seminar.count <= 0) {
                throw Seminar400("형식에 맞지 않게 입력하지 않은 값이 있습니다")
            }
        }

        // Query #1 -> [N+1] but was 2: fetching instructor profile
        if(userRepository.findByEmail(authTokenService.getCurrentEmail(token))?.instructor == null) {
            throw Seminar403("세미나를 수정할 자격이 없습니다")
        }

        /*
        * TODO: 스펙에 따르면 해당 세미나를 만든 사람이 아니면 세미나를 수정할 수 없는데
        *       그 부분을 처리하는 코드가 없습니다.
        *       지금으로서는 instructor 자격만 있으면
        *       누구나 세미나를 수정할 수 있게 구현되어 있습니다.
        */
        
        // Query #2
        val seminarEntity = seminarRepository.findByName(seminar.name)
        
        seminarEntity.let { 
            it.name = seminar.name
            it.capacity = seminar.capacity
            it.count = seminar.count
            it.time = seminar.time
            it.online = seminar.online
        }
        
        // Query #3, #4
        // query count와는 상관없는 얘기지만, @Transactional을 사용하여 save문을 생략하실 수도 있습니다.
        seminarRepository.save(seminarEntity)
        
        return UpdateSeminarInfo(
            seminarEntity.id,
            seminarEntity.name,
            seminarEntity.capacity,
            seminarEntity.count,
            seminarEntity.time,
            seminarEntity.online,
        )
    }

    // Query Count 예상: 4, 실제: 15
    fun getSeminarById(id: Long, token: String):GetSeminarInfo{
        // Query #1
        if(seminarRepository.findById(id).isEmpty){
            throw Seminar404("해당하는 세미나가 없습니다")
        }
        // Query #2
        val seminarList = queryFactory.select(Projections.constructor(
            SeminarDto::class.java,
            qSeminarEntity
        ))
            .from(qSeminarEntity)
            .innerJoin(qUserSeminarEntity).on(qSeminarEntity.id.eq(qUserSeminarEntity.seminar.id))
            .innerJoin(qUserEntity).on(qUserSeminarEntity.user.id.eq(qUserEntity.id))
            .where(qSeminarEntity.id.eq(id)).fetch()
        
        val seminarEntity = seminarList[0].seminarEntity

        // Query #3 -> [N+1] but was 2: fetch join을 사용하지 않았습니다.
        // 과제 레포의 (branch: asmt2) findAllWithProfiles()를 참고하시면 좋을 것 같습니다.
        val instructorList = queryFactory.select(Projections.constructor(
            UserSeminarAndUserDto::class.java,
            qUserSeminarEntity,
            qUserEntity
        ))
            .from(qUserSeminarEntity)
            .innerJoin(qUserEntity).on(qUserSeminarEntity.user.id.eq(qUserEntity.id))
            .where(qUserSeminarEntity.seminar.id.eq(id))
            .where(qUserSeminarEntity.role.eq("instructor")).fetch()

        val teacherList = mutableListOf<TeacherDto>()

        for(i in 0 until instructorList.size){
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
        
        /*
        * instructorList는 UserSeminar Table에서 UserEntity를 inner join하여 fetch하는 반면
        * participantList는 Seminar Table에서 UserSeminarEntity를 inner join하고
        * 다시 UserSeminarEntity에 대해 UserEntity를 inner join하고 있습니다.
        * participantList도 같은 방식으로 UserSeminar Table에서 UserEntity를 inner join하여 바로 fetch해도 될 것 같습니다.
        */
        // Query #4 -> [N+1] but was 11: fetch join을 사용하지 않았습니다.
        val participantList = queryFactory.select(Projections.constructor(
            SeminarInfoDto::class.java,
            qSeminarEntity,
            qUserSeminarEntity,
            qUserEntity
        ))
            .from(qSeminarEntity)
            .innerJoin(qUserSeminarEntity).on(qSeminarEntity.id.eq(qUserSeminarEntity.seminar.id))
            .innerJoin(qUserEntity).on(qUserSeminarEntity.user.id.eq(qUserEntity.id)).where(qSeminarEntity.id.eq(id))
            .where(qUserSeminarEntity.role.eq("participant")).fetch()
        
        val studentList = mutableListOf<StudentDto>()

        for(i in 0 until participantList.size){
            val studentEntity = participantList[i].userEntity
            val studentSeminarEntity = participantList[i].userSeminarEntity
            studentList.add(
                StudentDto(
                    studentEntity?.id,
                    studentEntity?.username,
                    studentEntity?.email,
                    studentSeminarEntity?.joinedAt,
                    studentSeminarEntity?.isActive,
                    studentSeminarEntity?.droppedAt
                )
            )
        }
        return GetSeminarInfo(
            seminarEntity?.id,
            seminarEntity?.name,
            seminarEntity?.capacity,
            seminarEntity?.count,
            seminarEntity?.time,
            seminarEntity?.online,
            teacherList,
            studentList
        )
    }

    // Query Count 예상: 1, 실제: 1
    fun getSeminars(token: String): List<GetSeminars> {
        // Query #1
        val seminarList = queryFactory.select(Projections.constructor(
            SeminarDto::class.java,
            qSeminarEntity
        ))
            .from(qSeminarEntity)
            .fetch()
        
        val seminars = mutableListOf<GetSeminars>()

        for(i in 0 until seminarList.size) {
            val seminarEntity = seminarList[i].seminarEntity
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
        return seminars
    }

    // Query Count 예상: 2, 실제: 65
    // fetch시 orderBy 부분만 다르고 중복되는 코드가 너무 많습니다.
    // 일괄적으로 orderBy로 fetch한 후 reverse()를 사용하시면 좋을 것 같습니다.
    fun getSeminarByName(name: String, order: String, token: String):GetSeminarInfoByName{
        val seminarInfoDto : List<SeminarInfoDto>

        if(order=="earliest") {
            // Query #1 -> [N+1] fetch join을 사용하지 않았습니다.
            seminarInfoDto = queryFactory.select(Projections.constructor(
                SeminarInfoDto::class.java,
                qSeminarEntity,
                qUserSeminarEntity,
                qUserEntity
            ))
                .from(qSeminarEntity)
                .innerJoin(qUserSeminarEntity).on(qSeminarEntity.id.eq(qUserSeminarEntity.seminar.id))
                .innerJoin(qUserEntity).on(qUserSeminarEntity.user.id.eq(qUserEntity.id)).where(qSeminarEntity.name.contains(name))
                .orderBy(qUserSeminarEntity.joinedAt.asc()).fetch()

            val seminarEntity = seminarInfoDto[0].seminarEntity
            val userSeminarEntity = seminarInfoDto[0].userSeminarEntity
            val userEntity = seminarInfoDto[0].userEntity

            // Query #2 -> [N+1] fetch join을 사용하지 않았습니다.
            val studentList = queryFactory.select(Projections.constructor(
                SeminarInfoDto::class.java,
                qSeminarEntity,
                qUserSeminarEntity,
                qUserEntity
            ))
                .from(qSeminarEntity)
                .innerJoin(qUserSeminarEntity).on(qSeminarEntity.id.eq(qUserSeminarEntity.seminar.id))
                .innerJoin(qUserEntity).on(qUserSeminarEntity.user.id.eq(qUserEntity.id)).where(qSeminarEntity.name.contains(name))
                .where(qUserSeminarEntity.role.eq("participant")).fetch()

            val newList = mutableListOf<StudentDto>()

            for(i in 0 until studentList.size){
                val studentEntity = studentList[i].userEntity
                val studentSeminarEntity = studentList[i].userSeminarEntity
                newList.add(
                    StudentDto(
                        studentEntity?.id,
                        studentEntity?.username,
                        studentEntity?.email,
                        studentSeminarEntity?.joinedAt,
                        studentSeminarEntity?.isActive,
                        studentSeminarEntity?.droppedAt
                    )
                )
            }
            
            return GetSeminarInfoByName(
                seminarEntity?.id,
                seminarEntity?.name,
                List(seminarInfoDto.size) { _ ->
                    TeacherDto(
                        userEntity?.id,
                        userEntity?.username,
                        userEntity?.email,
                        userSeminarEntity?.joinedAt
                    )

                }.distinct()
                ,newList.filter { it.isActive ==true }.size
            )
        } else {
            seminarInfoDto = queryFactory.select(Projections.constructor(
                SeminarInfoDto::class.java,
                qSeminarEntity,
                qUserSeminarEntity,
                qUserEntity
            ))
                .from(qSeminarEntity)
                .innerJoin(qUserSeminarEntity).on(qSeminarEntity.id.eq(qUserSeminarEntity.seminar.id))
                .innerJoin(qUserEntity).on(qUserSeminarEntity.user.id.eq(qUserEntity.id)).where(qSeminarEntity.name.contains(name)).orderBy(qUserSeminarEntity.joinedAt.desc()).fetch()

            val seminarEntity = seminarInfoDto[0].seminarEntity
            val userSeminarEntity = seminarInfoDto[0].userSeminarEntity
            val userEntity = seminarInfoDto[0].userEntity

            val studentList = queryFactory.select(Projections.constructor(
                SeminarInfoDto::class.java,
                qSeminarEntity,
                qUserSeminarEntity,
                qUserEntity
            ))
                .from(qSeminarEntity)
                .innerJoin(qUserSeminarEntity).on(qSeminarEntity.id.eq(qUserSeminarEntity.seminar.id))
                .innerJoin(qUserEntity).on(qUserSeminarEntity.user.id.eq(qUserEntity.id)).where(qSeminarEntity.name.contains(name)).where(qUserSeminarEntity.role.eq("participant")).fetch()

            val newList = mutableListOf<StudentDto>()

            for(i in 0 until studentList.size){
                val studentEntity = studentList[i].userEntity
                val studentSeminarEntity = studentList[i].userSeminarEntity
                newList.add(
                    StudentDto(
                        studentEntity?.id,
                        studentEntity?.username,
                        studentEntity?.email,
                        studentSeminarEntity?.joinedAt,
                        studentSeminarEntity?.isActive,
                        studentSeminarEntity?.droppedAt

                    )
                )
            }
            
            return GetSeminarInfoByName(
                seminarEntity?.id,
                seminarEntity?.name,
                List(seminarInfoDto.size) { _ ->
                    TeacherDto(
                        userEntity?.id,
                        userEntity?.username,
                        userEntity?.email,
                        userSeminarEntity?.joinedAt
                    )

                }.distinct(),
                newList.filter { it.isActive ==true }.size

            )
        }

    }

    // Query Count 예상: 17, 실제: 29
    fun joinSeminar(id: Long, role: Map<String, String>, token: String): JoinSeminarInfo {
        // Query #1
        val seminarFindByIdEntity = seminarRepository.findById(id)
        // Query #2
        val userFindByIdEntity = userRepository.findById(authTokenService.getCurrentUserId(token))

        // Query #3 -> [N+1]
        if(userSeminarRepository.findByUser(userFindByIdEntity.get())?.filter { 
            it.seminar.id == id && it.isActive == true
                
            } != emptyList<UserSeminarEntity>()) {
            
            throw Seminar400("이미 세미나에 참여하고 있습니다")
        }
        if(seminarFindByIdEntity.isEmpty) {
            
            throw Seminar404("해당하는 세미나가 없습니다.")
            
        }
        if(userFindByIdEntity.get().participant != null) {

            // TODO 비활성(미등록) 사용자의 경우 400이 아니라 403으로 응답해야 합니다.
            if(userFindByIdEntity.get().participant?.isRegistered == false) {
                throw Seminar400("등록되어 있지 않습니다")
            }
        }

        // Query #4
        if(userSeminarRepository.findByUser(userFindByIdEntity.get())?.filter {
                it.isActive == false
            } != emptyList<UserSeminarEntity>()) {

            throw Seminar400("드랍한 세미나는 다시 신청할 수 없습니다")
        }
        val saveUserSeminarEntity : UserSeminarEntity
        
        if(role["role"] == "participant"){
            
            if(userFindByIdEntity.get().participant != null) {
                // Query #5
                saveUserSeminarEntity = userSeminarRepository.save(
                    UserSeminarEntity(
                        // Query #6
                        // 불필요한 query인 것 같습니다.
                        // 위에서 이미 구해놓으신 userFindByIdEntity를 사용하시면 될 것 같습니다.
                        user = userRepository.findById(authTokenService.getCurrentUserId(token)).get(),
                        // Query #7
                        // 마찬가지로 seminarFindByIdEntity를 사용하시면 될 것 같습니다.
                        seminar = seminarRepository.findById(id).get(),
                        role = "participant",
                        joinedAt = LocalDateTime.now(),
                        isActive = true,
                        droppedAt = null
                    )
                )
                
            } else {
                throw Seminar403("수강생이 아닙니다")
            }
            
        } else if (role["role"] == "instructor") {
            if(userFindByIdEntity.get().instructor != null){
                if(userSeminarRepository.findByUser(userFindByIdEntity.get()) == emptyList<UserSeminarEntity>()) {
                    saveUserSeminarEntity = userSeminarRepository.save(
                        UserSeminarEntity(
                            user = userRepository.findById(authTokenService.getCurrentUserId(token)).get(),
                            seminar = seminarRepository.findById(id).get(),
                            role = "instructor",
                            joinedAt = LocalDateTime.now(),
                            isActive = null,
                            droppedAt = null
                        )
                    ) 
                } else {
                    throw Seminar400("이미 다른 세미나를 진행하고 있습니다.")
                }
                
            } else {
                throw Seminar403("진행자가 아닙니다")
            }
            
        } else {
            throw Seminar400("진행자 혹은 수강자가 아닙니다.")
        }

        // Query #8
        val seminarList = queryFactory.select(Projections.constructor(
            SeminarDto::class.java,
            qSeminarEntity
        ))
            .from(qSeminarEntity)
            .where(qSeminarEntity.id.eq(id)).fetch()

        val seminarEntity = seminarList[0].seminarEntity

        // Query #9 -> [N+1] but was 2
        val instructorList = queryFactory.select(Projections.constructor(
            UserSeminarAndUserDto::class.java,
            qUserSeminarEntity,
            qUserEntity
        ))
            .from(qUserSeminarEntity)
            .innerJoin(qUserEntity).on(qUserSeminarEntity.user.id.eq(qUserEntity.id))
            .where(qUserSeminarEntity.seminar.id.eq(id))
            .where(qUserSeminarEntity.role.eq("instructor")).fetch()

        
        val teacherSeminarEntity = instructorList[0].userSeminarEntity
        val teacherEntity = instructorList[0].userEntity

        val teacherList = mutableListOf<TeacherDto>()

        for(i in 0 until instructorList.size){
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

        // Query #10 -> [N+1] but was 11
        val participantList = queryFactory.select(Projections.constructor(
            SeminarInfoDto::class.java,
            qSeminarEntity,
            qUserSeminarEntity,
            qUserEntity
        ))
            .from(qSeminarEntity)
            .innerJoin(qUserSeminarEntity).on(qSeminarEntity.id.eq(qUserSeminarEntity.seminar.id))
            .innerJoin(qUserEntity).on(qUserSeminarEntity.user.id.eq(qUserEntity.id)).where(qSeminarEntity.id.eq(id))
            .where(qUserSeminarEntity.role.eq("participant")).fetch()


        val studentList = mutableListOf<StudentDto>()
        
        for(i in 0 until participantList.size){
            val studentEntity = participantList[i].userEntity
            val studentSeminarEntity = participantList[i].userSeminarEntity
            studentList.add(
                StudentDto(
                    studentEntity?.id,
                    studentEntity?.username,
                    studentEntity?.email,
                    studentSeminarEntity?.joinedAt,
                    studentSeminarEntity?.isActive,
                    studentSeminarEntity?.droppedAt
                    
                )
            )
        }

        // Query #11
        if(studentList.size > seminarRepository.findById(id).get().capacity!!) {
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
            instructors = teacherList,
            
            participants = studentList
        )
    }
    
    fun dropSeminar(id: Long, token: String) : GetSeminarInfo{
        val findByEmailEntity = userRepository.findByEmail(authTokenService.getCurrentEmail(token))

        if(findByEmailEntity?.let {
                    userSeminarRepository.findByUser(it)?.filter {
                        it.seminar.id == id
                    }
                } == emptyList<UserSeminarEntity>()){
            throw Seminar404("해당 세미나를 신청한 적이 없습니다")
        }
        
        if(findByEmailEntity?.let {
                    userSeminarRepository.findByUser(it)?.filter {
                        it.role == "instructor"
                    }
                } != emptyList<UserSeminarEntity>()){
            throw Seminar403("진행자는 세미나를 드랍할 수 없습니다")
        }
        val seminarInfoDto = queryFactory.select(Projections.constructor(
            SeminarInfoDto::class.java,
            qSeminarEntity,
            qUserSeminarEntity,
            qUserEntity
        ))
            .from(qSeminarEntity)
            .innerJoin(qUserSeminarEntity).on(qSeminarEntity.id.eq(qUserSeminarEntity.seminar.id))
            .innerJoin(qUserEntity).on(qUserSeminarEntity.user.id.eq(qUserEntity.id))
            .where(qSeminarEntity.id.eq(id))
            .where(qUserEntity.email.eq(authTokenService.getCurrentEmail(token))).fetch()

        val seminarEntity = seminarInfoDto[0].seminarEntity
        var userSeminarEntity = seminarInfoDto[0].userSeminarEntity
        val userEntity = seminarInfoDto[0].userEntity
        
        userSeminarEntity?.isActive = false
        userSeminarEntity?.droppedAt = LocalDateTime.now()
        userSeminarEntity?.let { userSeminarRepository.save(it) }
        val studentList = queryFactory.select(Projections.constructor(
            SeminarInfoDto::class.java,
            qSeminarEntity,
            qUserSeminarEntity,
            qUserEntity
        ))
            .from(qSeminarEntity)
            .innerJoin(qUserSeminarEntity).on(qSeminarEntity.id.eq(qUserSeminarEntity.seminar.id))
            .innerJoin(qUserEntity).on(qUserSeminarEntity.user.id.eq(qUserEntity.id)).where(qSeminarEntity.id.eq(id)).where(qUserSeminarEntity.role.eq("participant")).fetch()

        val newList = mutableListOf<StudentDto>()

        for(i in 0 until studentList.size){
            val studentEntity = studentList[i].userEntity
            val studentSeminarEntity = studentList[i].userSeminarEntity
            newList.add(
                StudentDto(
                    studentEntity?.id,
                    studentEntity?.username,
                    studentEntity?.email,
                    studentSeminarEntity?.joinedAt,
                    studentSeminarEntity?.isActive,
                    studentSeminarEntity?.droppedAt

                )
            )
        }

        return GetSeminarInfo(
            seminarEntity?.id,
            seminarEntity?.name,
            seminarEntity?.capacity,
            seminarEntity?.count,
            seminarEntity?.time,
            seminarEntity?.online,
            List(seminarInfoDto.size) { _ ->
                TeacherDto(
                    userEntity?.id,
                    userEntity?.username,
                    userEntity?.email,
                    userSeminarEntity?.joinedAt
                )

            },newList

        )
    }
    
    
     
     
    
    private fun SeminarEntity(seminar: SeminarRequest, token: String) = seminar.run{
        com.wafflestudio.seminar.core.user.domain.SeminarEntity(

            name = seminar.name,
            capacity = seminar.capacity,
            count = seminar.count,
            time = seminar.time,//LocalTime.parse(seminar.time, DateTimeFormatter.ISO_TIME),
            online = true,
          

        )
    }

    private fun userSeminarInstructorEntity(seminar: SeminarRequest, token: String) : UserSeminarEntity {
        return UserSeminarEntity(
            user = userRepository.findByEmail(authTokenService.getCurrentEmail(token)),
            seminar= seminarRepository.findByName(seminar.name),
            role = "instructor",
            joinedAt = LocalDateTime.now(),
            isActive = true,
            droppedAt = null
        )
    }

   

}
