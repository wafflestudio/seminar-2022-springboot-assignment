package com.wafflestudio.seminar.core.user.service

import com.querydsl.core.Tuple
import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import com.wafflestudio.seminar.core.user.database.*
import com.wafflestudio.seminar.core.user.domain.QueryProjection
import com.wafflestudio.seminar.core.user.domain.Seminar
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.persistence.Entity

@Service
class SeminarService(
    private val authTokenService: AuthTokenService,
    private val instructorProfileRepository: InstructorProfileRepository,
    private val seminarRepository: SeminarRepository,
    private val participantSeminarRepository: ParticipantSeminarRepository,
    private val userRepository: UserRepository,
    private val queryFactory: JPAQueryFactory
    ) {

    fun createSeminar(seminar: Seminar, token: String): List<SeminarEntity>{
        //todo: online 여부 외에는 하나라도 빠지면 400으로 응답하며, 적절한 에러 메시지를 포함합니다.
        //todo: name에 0글자가 오는 경우, capacity와 count에 양의 정수가 아닌 값이 오는 경우는 400으로 응답합니다.
        //todo: 세미나 진행자 자격을 가진 User만 요청할 수 있으며, 그렇지 않은 경우 403으로 응답
        seminarRepository.save(SeminarEntity(seminar, token))


        
        return  queryFactory.select(QSeminarEntity.seminarEntity).from(QSeminarEntity.seminarEntity)
            .innerJoin(QParticipantSeminarEntity.participantSeminarEntity)
            .on(QSeminarEntity.seminarEntity.id.eq(3))
            .fetchJoin()
             .fetch()
        
        
        
        /*queryFactory.selectFrom(QSeminarEntity.seminarEntity)
            .join(QParticipantSeminarEntity.participantSeminarEntity)
            .where(QSeminarEntity.seminarEntity.id.eq(3)).fetch()
    */
    }
         
   fun getSeminarById(seminarId: Long):List<SeminarEntity>{
       val seminar :QSeminarEntity = QSeminarEntity.seminarEntity
       val participantSeminar : QParticipantSeminarEntity = QParticipantSeminarEntity.participantSeminarEntity
       
       val count = queryFactory.selectFrom(participantSeminar)
           .where(participantSeminar.seminar.id.eq(seminarId))
           .fetch().size
       
       return queryFactory.selectFrom(seminar)
           .where(seminar.id.eq(seminarId))
           .fetch()
       
   }
   fun joinSeminar(seminarId: Long, token: String): String {
       participantSeminarRepository.save(ParticipantSeminarEntity(seminarId, token))
       /*
       val result = queryFactory.selectFrom(QSeminarEntity.seminarEntity).fetch()
       */
      return "완료"
   }
    
    fun profile(token: String): List<QueryProjection>{
        val userEntity : QUserEntity = QUserEntity.userEntity
        val participantSeminar : QParticipantSeminarEntity = QParticipantSeminarEntity.participantSeminarEntity
        return queryFactory.select(Projections.constructor(QueryProjection::class.java, userEntity.username, userEntity.email,participantSeminar.joinedAt))
            .from(userEntity).innerJoin(participantSeminar).on(userEntity.dateJoined.eq(participantSeminar.joinedAt)).fetch()
    }
    
    
    private fun SeminarEntity(seminar: Seminar, token: String) = seminar.run{
        SeminarEntity(
            name = seminar.name,
            capacity = seminar.capacity,
            count = seminar.count,
            time = LocalTime.parse(seminar.time, DateTimeFormatter.ISO_TIME),
            online = true,
            instructors = userRepository.findByEmail(authTokenService.getCurrentEmail(token))
            
        )
    }
    
    private fun ParticipantSeminarEntity(seminarId: Long, token: String) :ParticipantSeminarEntity{
        return ParticipantSeminarEntity(
            user = userRepository.findByEmail(authTokenService.getCurrentEmail(token)),
            seminar= seminarRepository.findById(seminarId).orElse(null),
            joinedAt = LocalDateTime.now(),
            isActive = true,
            droppedAt = null
        )
    }

    
}