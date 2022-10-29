package com.wafflestudio.seminar.core.user.service

import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQuery
import com.querydsl.jpa.impl.JPAQueryFactory
import com.wafflestudio.seminar.core.user.api.request.SeminarRequest
import com.wafflestudio.seminar.core.user.api.response.SeminarInfo
import com.wafflestudio.seminar.core.user.api.response.SeminarInfoByName
import com.wafflestudio.seminar.core.user.database.*
import com.wafflestudio.seminar.core.user.domain.QSeminarEntity
import com.wafflestudio.seminar.core.user.domain.QUserEntity
import com.wafflestudio.seminar.core.user.domain.QUserSeminarEntity
import com.wafflestudio.seminar.core.user.domain.UserSeminarEntity
import com.wafflestudio.seminar.core.user.dto.SeminarInfoDto
import com.wafflestudio.seminar.core.user.dto.TeacherDto
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
        val saveSeminarEntity = seminarRepository.save(SeminarEntity(seminar, token))
        val saveUserSeminarEntity = userSeminarRepository.save(userSeminarInstructorEntity(seminar, token))

        val seminarInfoDto = queryFactory.select(Projections.constructor(
            SeminarInfoDto::class.java,
            qSeminarEntity,
            qUserSeminarEntity,
            qUserEntity
        ))
            .from(qSeminarEntity)
            .leftJoin(qUserSeminarEntity).on(qSeminarEntity.id.eq(qUserSeminarEntity.seminar.id))
            .leftJoin(qUserEntity).on(qUserSeminarEntity.user.id.eq(qUserEntity.id))
            .where(qSeminarEntity.id.eq(saveSeminarEntity.id))
            .fetch()
        

        val seminarEntity = seminarInfoDto[0].seminarEntity

        return SeminarInfo(
            seminarEntity?.id,
            seminarEntity?.name,
            seminarEntity?.capacity,
            seminarEntity?.count,
            seminarEntity?.time,
            seminarEntity?.online,
            seminarInfoDto.mapIndexed { _, SeminarInfoDto ->
                TeacherDto(
                    SeminarInfoDto.userEntity?.id,
                    SeminarInfoDto.userEntity?.username,
                    SeminarInfoDto.userEntity?.email,
                    SeminarInfoDto.userSeminarEntity?.joinedAt
                )

            }

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
            .innerJoin(qUserEntity).on(qUserSeminarEntity.user.id.eq(qUserEntity.id))
            
            .where(qSeminarEntity.id.eq(id))
            .fetch()
        
        val seminarEntity = seminarInfoDto[0].seminarEntity
        val userSeminarEntity = seminarInfoDto[0].userSeminarEntity
        val userEntity = seminarInfoDto[0].userEntity
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

            }.distinct()

        )

    }

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
        
        val newList = mutableListOf<SeminarInfo>()
        for(i in 0 until seminarInfoDtoList.size){
            val seminarEntity = seminarInfoDtoList[i].seminarEntity
            val userSeminarEntity = seminarInfoDtoList[i].userSeminarEntity
            val userEntity = seminarInfoDtoList[i].userEntity

            newList.add( SeminarInfo(
                seminarEntity?.id,seminarEntity?.name,seminarEntity?.capacity,seminarEntity?.count,seminarEntity?.time,seminarEntity?.online,
                List(seminarInfoDtoList.size){TeacherDto(
                    userEntity?.id,
                    userEntity?.username,
                    userEntity?.email,
                    userSeminarEntity?.joinedAt
                )}.distinct())
            )
        }
            
       return newList 

    }

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
                .innerJoin(qUserEntity).on(qUserSeminarEntity.user.id.eq(qUserEntity.id))
                .where(qSeminarEntity.name.contains(name))
                .orderBy(qUserSeminarEntity.joinedAt.asc()).fetch()

            val seminarEntity = seminarInfoDto[0].seminarEntity
            val userSeminarEntity = seminarInfoDto[0].userSeminarEntity
            val userEntity = seminarInfoDto[0].userEntity
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
                .innerJoin(qUserEntity).on(qUserSeminarEntity.user.id.eq(qUserEntity.id))
                .where(qSeminarEntity.name.contains(name))
                .orderBy(qUserSeminarEntity.joinedAt.desc()).fetch()

            val seminarEntity = seminarInfoDto[0].seminarEntity
            val userSeminarEntity = seminarInfoDto[0].userSeminarEntity
            val userEntity = seminarInfoDto[0].userEntity
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

            )
        }

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
