package com.wafflestudio.seminar.core.user.service

import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQuery
import com.querydsl.jpa.impl.JPAQueryFactory
import com.wafflestudio.seminar.core.user.api.request.SeminarRequest
import com.wafflestudio.seminar.core.user.api.response.JoinSeminarInfo
import com.wafflestudio.seminar.core.user.api.response.SeminarInfo
import com.wafflestudio.seminar.core.user.api.response.SeminarInfoByName
import com.wafflestudio.seminar.core.user.api.response.UpdateSeminarInfo
import com.wafflestudio.seminar.core.user.database.*
import com.wafflestudio.seminar.core.user.domain.QSeminarEntity
import com.wafflestudio.seminar.core.user.domain.QUserEntity
import com.wafflestudio.seminar.core.user.domain.QUserSeminarEntity
import com.wafflestudio.seminar.core.user.domain.UserSeminarEntity
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


    val query : JPAQuery<SeminarInfoDto> = queryFactory.select(Projections.constructor(
        SeminarInfoDto::class.java,
        qSeminarEntity,
        qUserSeminarEntity,
        qUserEntity
    ))
        .from(qSeminarEntity)
        .innerJoin(qUserSeminarEntity).on(qSeminarEntity.id.eq(qUserSeminarEntity.seminar.id))
        .innerJoin(qUserEntity).on(qUserSeminarEntity.user.id.eq(qUserEntity.id))
    
    
    fun createSeminar(seminar: SeminarRequest, token: String): SeminarInfo {
        //todo: online 여부 외에는 하나라도 빠지면 400으로 응답하며, 적절한 에러 메시지를 포함합니다.
        //todo: name에 0글자가 오는 경우, capacity와 count에 양의 정수가 아닌 값이 오는 경우는 400으로 응답합니다.
        //todo: 세미나 진행자 자격을 가진 User만 요청할 수 있으며, 그렇지 않은 경우 403으로 응답
        val saveSeminarEntity = seminarRepository.save(SeminarEntity(seminar, token))
        userSeminarRepository.save(userSeminarInstructorEntity(seminar, token))

        val seminarInfoDto = query.where(qSeminarEntity.id.eq(saveSeminarEntity.id))
            .where(qUserSeminarEntity.role.eq("instructor")).fetch()
        

        val seminarEntity = seminarInfoDto[0].seminarEntity
        val userSeminarEntity = seminarInfoDto[0].userSeminarEntity
        val userEntity = seminarInfoDto[0].userEntity

        val studentList = query.where(qSeminarEntity.name.eq(seminar.name)).where(qUserSeminarEntity.role.eq("participant")).fetch()

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
        
        val seminarEntity = seminarRepository.findById(seminar.id).get()
        
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

        val studentList = query.where(qSeminarEntity.id.eq(id))
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


 
    fun joinSeminar(id: Long, token: String): JoinSeminarInfo {
        val saveUserSeminarEntity = userSeminarRepository.save(
            UserSeminarEntity(
                user = userRepository.findById(authTokenService.getCurrentUserId(token)).get(),
                seminar = seminarRepository.findById(id).get(),
                role = "participant",
                joinedAt = LocalDateTime.now(),
                isActive = true,
                droppedAt = null
                )
        )
        val teacherList = query.where(qSeminarEntity.id.eq(id))
            .where(qUserSeminarEntity.role.eq("instructor")).fetch()
        
        
        val teacherSeminarEntity = teacherList[0].userSeminarEntity
        val teacherEntity = teacherList[0].userEntity
        
        val studentList = query.where(qSeminarEntity.id.eq(id))
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
