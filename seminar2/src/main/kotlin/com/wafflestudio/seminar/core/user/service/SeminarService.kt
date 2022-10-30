package com.wafflestudio.seminar.core.user.service

import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import com.wafflestudio.seminar.common.Seminar400
import com.wafflestudio.seminar.common.Seminar403
import com.wafflestudio.seminar.common.Seminar404
import com.wafflestudio.seminar.common.Seminar409
import com.wafflestudio.seminar.core.user.api.request.SeminarRequest
import com.wafflestudio.seminar.core.user.api.response.JoinSeminarInfo
import com.wafflestudio.seminar.core.user.api.response.SeminarInfo
import com.wafflestudio.seminar.core.user.api.response.SeminarInfoByName
import com.wafflestudio.seminar.core.user.api.response.UpdateSeminarInfo
import com.wafflestudio.seminar.core.user.database.SeminarRepository
import com.wafflestudio.seminar.core.user.database.UserRepository
import com.wafflestudio.seminar.core.user.database.UserSeminarRepository
import com.wafflestudio.seminar.core.user.domain.*
import com.wafflestudio.seminar.core.user.dto.seminar.SeminarInfoDto
import com.wafflestudio.seminar.core.user.dto.seminar.StudentDto
import com.wafflestudio.seminar.core.user.dto.seminar.TeacherDto
import org.springframework.stereotype.Service
import java.time.LocalDateTime

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

    
    
    fun createSeminar(seminar: SeminarRequest, token: String): SeminarInfo {
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
        
        if(userRepository.findByEmail(authTokenService.getCurrentEmail(token)).instructor == null) {
            throw Seminar403("진행자만 세미나를 생성할 수 있습니다")
        }
        
        val saveSeminarEntity = seminarRepository.save(SeminarEntity(seminar, token))
        userSeminarRepository.save(userSeminarInstructorEntity(seminar, token))

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
        
        return SeminarInfo(
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
    
    fun updateSeminar(seminar: SeminarRequest, token: String): UpdateSeminarInfo {

        if(seminar.name == null || seminar.capacity == null || seminar.count == null || seminar.time == null ) {
            throw Seminar400("입력하지 않은 값이 있습니다")


        } else {
            if(seminar.name == "" || seminar.capacity <= 0 || seminar.count <= 0) {
                throw Seminar400("형식에 맞지 않게 입력하지 않은 값이 있습니다")
            }
        }

        if(userRepository.findByEmail(authTokenService.getCurrentEmail(token)).instructor == null) {
            throw Seminar403("진행자만 세미나를 생성할 수 있습니다")
        }
        
        val seminarEntity = seminarRepository.findByName(seminar.name)
        
        seminarEntity.let { 
            it.name = seminar.name
            it.capacity = seminar.capacity
            it.count = seminar.count
            it.time = seminar.time
            it.online = seminar.online
        }
        
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


    fun getSeminarById(id: Long, token: String):SeminarInfo{

        val seminarInfoDto = queryFactory.select(Projections.constructor(
            SeminarInfoDto::class.java,
            qSeminarEntity,
            qUserSeminarEntity,
            qUserEntity
        ))
            .from(qSeminarEntity)
            .innerJoin(qUserSeminarEntity).on(qSeminarEntity.id.eq(qUserSeminarEntity.seminar.id))
            .innerJoin(qUserEntity).on(qUserSeminarEntity.user.id.eq(qUserEntity.id)).where(qSeminarEntity.id.eq(id)).fetch()
        
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
            .innerJoin(qUserEntity).on(qUserSeminarEntity.user.id.eq(qUserEntity.id)).where(qSeminarEntity.id.eq(id))
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
        return SeminarInfo(
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

            }.distinct(),
            newList

        )

    }

    /*
    fun getSeminars(token: String):List<SeminarInfo>? {
        val seminarInfoDtoList = queryFactory.select(Projections.constructor(
            SeminarInfoDto::class.java,
            qSeminarEntity,
            qUserSeminarEntity,
            qUserEntity

        ))
            .from(qSeminarEntity)
            .innerJoin(qUserSeminarEntity).on(qSeminarEntity.id.eq(qUserSeminarEntity.seminar.id))
            .innerJoin(qUserEntity).on(qUserSeminarEntity.user.id.eq(qUserEntity.id))

            .fetch()
        
        
        for(i in 0 until seminarInfoDtoList.size){
            val studentEntity = studentList[i].userEntity
            
            
            val seminarEntity = seminarInfoDtoList[i].seminarEntity
            val userSeminarEntity = seminarInfoDtoList[i].userSeminarEntity
            val userEntity = seminarInfoDtoList[i].userEntity

            newList.add( SeminarInfo(
                seminarEntity?.id,seminarEntity?.name,seminarEntity?.capacity,seminarEntity?.count,seminarEntity?.time,seminarEntity?.online,
                List(seminarInfoDtoList.size){
                    TeacherDto(
                    userEntity?.id,
                    userEntity?.username,
                    userEntity?.email,
                    userSeminarEntity?.joinedAt
                )
                })
            )
        }
            
       return newList 

    }
    
     */

    fun getSeminarByName(name: String, order: String, token: String):SeminarInfoByName{
        val seminarInfoDto : List<SeminarInfoDto>
        
        if(order=="earliest") {
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
            
            return SeminarInfoByName(
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
                ,newList.size
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
            
            return SeminarInfoByName(
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
                newList.size

            )
        }

    }


 
    fun joinSeminar(id: Long, role: Map<String, String>, token: String): JoinSeminarInfo {
        
        val seminarEntity = seminarRepository.findById(id)
        
        val userEntity = userRepository.findById(authTokenService.getCurrentUserId(token))


        if(userSeminarRepository.findByUser(userEntity.get())?.seminar?.id == id) {
            
            throw Seminar400("이미 세미나에 참여하고 있습니다")
        }
        if(seminarEntity.isEmpty) {
            
            throw Seminar404("해당하는 세미나가 없습니다.")
            
        }
        if(userEntity.get().participant != null) {
            
            if(userEntity.get().participant?.isRegistered == false) {
                throw Seminar400("등록되어 있지 않습니다")
            }
        }

        val saveUserSeminarEntity : UserSeminarEntity
        
        if(role["role"] == "participant"){
            
            if(userEntity.get().participant != null) {
                saveUserSeminarEntity = userSeminarRepository.save(
                    UserSeminarEntity(
                        user = userRepository.findById(authTokenService.getCurrentUserId(token)).get(),
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
            if(userEntity.get().instructor != null){
                if(userSeminarRepository.findByUser(userEntity.get())?.seminar == null) {
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
                    throw Seminar400("이미 다른 수업을 진행하고 있습니다.")
                }
                
            } else {
                throw Seminar403("진행자가 아닙니다")
            }
            
        } else {
            throw Seminar400("진행자 혹은 수강자가 아닙니다.")
        }
            
        val teacherList = queryFactory.select(Projections.constructor(
            SeminarInfoDto::class.java,
            qSeminarEntity,
            qUserSeminarEntity,
            qUserEntity
        ))
            .from(qSeminarEntity)
            .innerJoin(qUserSeminarEntity).on(qSeminarEntity.id.eq(qUserSeminarEntity.seminar.id))
            .innerJoin(qUserEntity).on(qUserSeminarEntity.user.id.eq(qUserEntity.id)).where(qSeminarEntity.id.eq(id))
            .where(qUserSeminarEntity.role.eq("instructor")).fetch()
        
        
        val teacherSeminarEntity = teacherList[0].userSeminarEntity
        val teacherEntity = teacherList[0].userEntity
        
        val studentList = queryFactory.select(Projections.constructor(
            SeminarInfoDto::class.java,
            qSeminarEntity,
            qUserSeminarEntity,
            qUserEntity
        ))
            .from(qSeminarEntity)
            .innerJoin(qUserSeminarEntity).on(qSeminarEntity.id.eq(qUserSeminarEntity.seminar.id))
            .innerJoin(qUserEntity).on(qUserSeminarEntity.user.id.eq(qUserEntity.id)).where(qSeminarEntity.id.eq(id))
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
        
        if(newList.size > seminarRepository.findById(id).get().capacity!!) {
                userSeminarRepository.delete(saveUserSeminarEntity)
                throw Seminar400("세미나의 인원이 다 찼습니다")
        }
        
        return JoinSeminarInfo(
            id = seminarRepository.findById(id).get().id,
            name = seminarRepository.findById(id).get().name,
            capacity = seminarRepository.findById(id).get().capacity,
            count = seminarRepository.findById(id).get().count,
            time = seminarRepository.findById(id).get().time,
            online = seminarRepository.findById(id).get().online,
            instructors = List(teacherList.size) {
                
                    TeacherDto(
                        teacherEntity?.id,
                        teacherEntity?.username,
                        teacherEntity?.email,
                        teacherSeminarEntity?.joinedAt
                    )
                
            },
            
            participants = newList
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
