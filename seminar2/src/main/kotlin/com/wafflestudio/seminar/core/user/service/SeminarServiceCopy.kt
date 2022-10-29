package com.wafflestudio.seminar.core.user.service
/*
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
import com.wafflestudio.seminar.core.user.dto.CreateSeminarInstructorDto
import com.wafflestudio.seminar.core.user.dto.SeminarInfoDto
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class SeminarServiceCopy(
    private val authTokenService: AuthTokenService,
    private val seminarRepository: SeminarRepository,
    private val userSeminarRepository: UserSeminarRepository,
    private val userRepository: UserRepository,
    private val queryFactory: JPAQueryFactory,
    ) {


    private final val qSeminarEntity: QSeminarEntity = QSeminarEntity.seminarEntity
    private final val qUserEntity: QUserEntity = QUserEntity.userEntity
    private final val qUserSeminarEntity: QUserSeminarEntity = QUserSeminarEntity.userSeminarEntity


    val query :JPAQuery<SeminarInfoDto> =queryFactory.select(Projections.constructor(
        SeminarInfoDto::class.java,
        qSeminarEntity,
        qUserEntity,
        qUserSeminarEntity
    ))
        .from(qSeminarEntity)
        .leftJoin(qUserEntity).on(qSeminarEntity.instructors.email.eq(qUserEntity.email))
        .leftJoin(qUserSeminarEntity).on(qUserEntity.id.eq(qUserSeminarEntity.user.id))
        .where(qSeminarEntity.id.eq(qUserSeminarEntity.seminar.id))
        .where(qUserSeminarEntity.user.id.eq(qUserEntity.id))
    
    fun createSeminar(seminar: SeminarRequest, token: String): SeminarInfo {
        //todo: online 여부 외에는 하나라도 빠지면 400으로 응답하며, 적절한 에러 메시지를 포함합니다.
        //todo: name에 0글자가 오는 경우, capacity와 count에 양의 정수가 아닌 값이 오는 경우는 400으로 응답합니다.
        //todo: 세미나 진행자 자격을 가진 User만 요청할 수 있으며, 그렇지 않은 경우 403으로 응답
        seminarRepository.save(SeminarEntity(seminar, token))
        userSeminarRepository.save(UserSeminarInstructorEntity(seminar, token))
        
        val seminarInstructorDto = makeSeminarInstructorDto(seminar, token, qSeminarEntity, qUserEntity, qUserSeminarEntity)

        return makeSeminarInfo(seminarInstructorDto)
        
        
       /* return queryFactory.select(
            Projections.constructor(
                CreateSeminarDto::class.java,
                seminarEntity.id,
                seminarEntity.name,
                seminarEntity.capacity,
                seminarEntity.count,
                seminarEntity.time,
                seminarEntity.online,
                createInstructorEntity
            )
        )
            .from(seminarEntity, createInstructorEntity)
            .where(seminarEntity.id.eq(saveSeminarEntity.id))
            .fetch()
            
        */

        /*queryFactory.selectFrom(QSeminarEntity.seminarEntity)
           .join(QParticipantSeminarEntity.participantSeminarEntity)
           .where(QSeminarEntity.seminarEntity.id.eq(3)).fetch()
   */
    }
    
    fun getSeminarById(id: Long, token: String):SeminarInfo{

       val seminarInstructorDto = getSeminarInstructorDtoById(id, qSeminarEntity, qUserEntity, qUserSeminarEntity)
       return makeSeminarInfo(seminarInstructorDto)
       
   }
   
    fun getSeminars(token: String):List<SeminarInfo> {
        val seminarInstructorDtoList = queryFactory.select(Projections.constructor(
            SeminarInfoDto::class.java,
            qSeminarEntity,
            qUserEntity,
            qUserSeminarEntity
        ))
            .from(qSeminarEntity)
            .leftJoin(qUserEntity).on(qSeminarEntity.instructors.email.eq(qUserEntity.email))
            .leftJoin(qUserSeminarEntity).on(qUserEntity.id.eq(qUserSeminarEntity.user.id))
            .where(qSeminarEntity.id.eq(qUserSeminarEntity.seminar.id))
            .where(qUserEntity.id.eq(qUserSeminarEntity.user.id)).fetch()
        
        val userEntityList = seminarInstructorDtoList[0].userEntity
        
        val newList: List<CreateSeminarInstructorDto> = ""
        return seminarInstructorDtoList.mapIndexed { _, seminarInstructorDto ->
            SeminarInfo(
                seminarInstructorDto.seminarEntity?.id,
                seminarInstructorDto.seminarEntity?.name,
                seminarInstructorDto.seminarEntity?.capacity,
                seminarInstructorDto.seminarEntity?.count,
                seminarInstructorDto.seminarEntity?.time,
                seminarInstructorDto.seminarEntity?.online,
                
                /*
                newList.plus(
                    CreateSeminarInstructorDto(
                        seminarInstructorDtoList[0].userEntity?.id,
                        seminarInstructorDtoList[0].userEntity?.username,
                        seminarInstructorDtoList[0].userEntity?.email,
                        seminarInstructorDtoList[0].userSeminarEntity?.joinedAt
                        
                    )
                )
                
                 */
            )
        }

    }
    
    fun getSeminarByName(name: String, order: String, token: String):List<SeminarInfoByName>{
        val seminarInstructorDto : List<SeminarInfoDto>
        if(order.equals("earliest")) {
            seminarInstructorDto = getSeminarInstructorDtoByNameAsc(name, order, qSeminarEntity, qUserEntity, qUserSeminarEntity)

            return seminarInstructorDto.mapIndexed { _, seminarInstructorDto ->
                SeminarInfoByName(
                    seminarInstructorDto.seminarEntity?.id,
                    seminarInstructorDto.seminarEntity?.name,
                    CreateSeminarInstructorDto(
                        seminarInstructorDto.userEntity?.id,
                        seminarInstructorDto.userEntity?.username,
                        seminarInstructorDto.userEntity?.email,
                        seminarInstructorDto.userSeminarEntity?.joinedAt
                    )
                )
            }
        } else {
            seminarInstructorDto = getSeminarInstructorDtoByNameDesc(name, order, qSeminarEntity, qUserEntity, qUserSeminarEntity)

            return seminarInstructorDto.mapIndexed { _, seminarInstructorDto ->
                SeminarInfoByName(
                    seminarInstructorDto.seminarEntity?.id,
                    seminarInstructorDto.seminarEntity?.name,
                    CreateSeminarInstructorDto(
                        seminarInstructorDto.userEntity?.id,
                        seminarInstructorDto.userEntity?.username,
                        seminarInstructorDto.userEntity?.email,
                        seminarInstructorDto.userSeminarEntity?.joinedAt
                    )
                )
            }
        }
        

            
        
    }
    
    /*
   fun joinSeminar(seminarId: Long, token: String): String {
       participantSeminarRepository.save(ParticipantSeminarEntity(seminarId, token))
       /*
       val result = queryFactory.selectFrom(QSeminarEntity.seminarEntity).fetch()
       */
      return "완료"
   }
   */
     
    /*
    fun profile(token: String): List<QueryProjection>{
        val userEntity : QUserEntity = QUserEntity.userEntity
        val participantSeminar : QParticipantSeminarEntity = QParticipantSeminarEntity.participantSeminarEntity
        return queryFactory.select(Projections.constructor(QueryProjection::class.java, userEntity.username, userEntity.email,participantSeminar.joinedAt))
            .from(userEntity).innerJoin(participantSeminar).on(userEntity.dateJoined.eq(participantSeminar.joinedAt)).fetch()
    }
    
    
     */
    
    private fun makeSeminarInstructorDto(seminar: SeminarRequest, token: String, qSeminarEntity: QSeminarEntity, qUserEntity: QUserEntity, qUserSeminarEntity: QUserSeminarEntity):List<SeminarInfoDto>{
        return query.where(qSeminarEntity.name.eq(seminar.name))
            .where(qUserEntity.email.eq(authTokenService.getCurrentEmail(token))).fetch()
    }

    private fun getSeminarInstructorDtoById(id:Long, qSeminarEntity: QSeminarEntity, qUserEntity: QUserEntity, qUserSeminarEntity: QUserSeminarEntity):List<SeminarInfoDto>{
        return queryFactory.select(Projections.constructor(
            SeminarInfoDto::class.java,
            qSeminarEntity,
            qUserEntity,
            qUserSeminarEntity
        ))
            .from(qSeminarEntity)
            .leftJoin(qUserEntity).on(qSeminarEntity.instructors.email.eq(qUserEntity.email))
            .leftJoin(qUserSeminarEntity).on(qUserEntity.id.eq(qUserSeminarEntity.user.id))
            .where(qSeminarEntity.id.eq(qUserSeminarEntity.seminar.id))
            .where(qUserEntity.id.eq(qUserSeminarEntity.user.id)).where(qSeminarEntity.id.eq(id)).fetch()
    }
    
    private fun makeSeminarInfo(seminarInstructorDto: List<SeminarInfoDto>): SeminarInfo {
        val seminarEntity = seminarInstructorDto[0].seminarEntity
        val userEntity = seminarInstructorDto[0].userEntity
        val userSeminarEntity = seminarInstructorDto[0].userSeminarEntity

        return SeminarInfo(
            seminarEntity?.id,
            seminarEntity?.name,
            seminarEntity?.capacity,
            seminarEntity?.count,
            seminarEntity?.time,
            seminarEntity?.online,
            makeCreateSeminarInstructorDtoList(seminarInstructorDto)

        )
    }
    
    private fun getSeminarInstructorDtoList(qSeminarEntity: QSeminarEntity, qUserEntity: QUserEntity, qUserSeminarEntity: QUserSeminarEntity):List<SeminarInfoDto>{
        return query.fetch()
    }

    private fun getSeminarInstructorDtoByNameAsc(name:String, order: String, qSeminarEntity: QSeminarEntity, qUserEntity: QUserEntity, qUserSeminarEntity: QUserSeminarEntity):List<SeminarInfoDto>{
        
        return query.where(qSeminarEntity.name.contains(name)).orderBy(qUserSeminarEntity.joinedAt.asc()).fetch()
            
    }

    private fun getSeminarInstructorDtoByNameDesc(name:String, order: String, qSeminarEntity: QSeminarEntity, qUserEntity: QUserEntity, qUserSeminarEntity: QUserSeminarEntity):List<SeminarInfoDto> {
        
        return query.where(qSeminarEntity.name.contains(name)).orderBy(qUserSeminarEntity.joinedAt.desc()).fetch()
    
    }



    private fun makeCreateSeminarInstructorDtoList(list: List<SeminarInfoDto>): List<CreateSeminarInstructorDto>{
     
        
        
        return  list.mapIndexed { index, SeminarInfoDto ->
            CreateSeminarInstructorDto(
                SeminarInfoDto.userEntity?.id,
                SeminarInfoDto.userEntity?.username,
                SeminarInfoDto.userEntity?.email,
                SeminarInfoDto.userSeminarEntity?.joinedAt
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
            instructors = userRepository.findByEmail(authTokenService.getCurrentEmail(token))

        )
    }

    private fun UserSeminarInstructorEntity(seminar: SeminarRequest, token: String) : UserSeminarEntity {
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

 */