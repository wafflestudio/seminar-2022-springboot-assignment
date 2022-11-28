package com.wafflestudio.seminar.core.seminar.service

import com.wafflestudio.seminar.common.ErrorCode
import com.wafflestudio.seminar.common.SeminarException
import com.wafflestudio.seminar.core.UserSeminar.domain.UserSeminarEntity
import com.wafflestudio.seminar.core.UserSeminar.repository.UserSeminarRepository
import com.wafflestudio.seminar.core.seminar.api.request.RegisterRequest
import com.wafflestudio.seminar.core.seminar.api.request.SeminarRequest
import com.wafflestudio.seminar.core.seminar.domain.*
import com.wafflestudio.seminar.core.seminar.repository.SeminarRepository
import com.wafflestudio.seminar.core.user.domain.enums.RoleType
import com.wafflestudio.seminar.core.user.domain.enums.RoleType.*
import com.wafflestudio.seminar.core.user.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import org.springframework.transaction.annotation.Transactional

interface SeminarService {
    fun makeSeminar(userId: Long, request: SeminarRequest): SeminarDTO
    fun editSeminar(userId: Long, request: SeminarDTO): SeminarDTO
    
    fun findSeminarsContainingWord(word: String?, order: String?): List<SeminarGroupByDTO>
    fun findSeminarById(seminarId: Long) : SeminarDTO
    
    fun registerSeminar(userId: Long, seminarId: Long, request:RegisterRequest): SeminarDTO
    fun dropSeminar(userId: Long, seminarId: Long): SeminarDTO
}

@Service
class SeminarServiceImpl(
    private val userRepository: UserRepository,
    private val seminarRepository: SeminarRepository,
    private val userSeminarRepository: UserSeminarRepository
): SeminarService {
    
    private fun SeminarEntity.toDTO(
        userSeminarList: MutableList<UserSeminarEntity>? = null
    ): SeminarDTO {
        val userSeminars = userSeminarList ?: userSeminarRepository.findAllBySeminar_Id(id)
        
        val userIds = userSeminars.map { it.user.id }
        val users = userRepository.findWithProfiles(userIds).associateBy { it.id }
        val (instructors, participants) = userSeminars.partition { it.role == INSTRUCTOR }
        
        val instructorProjections = instructors.map { inst -> SeminarInstructorDTO.of(users[inst.user.id]!!, inst) }
        // 아직 해당 세미나의 참여자가 없을 수도 있음
        val participantProjections = 
            with(participants) {
                if (participants.isEmpty()) null
                else this.map { part -> SeminarParticipantDTO.of(users[part.user.id]!!, part) }
            }
        
        return SeminarDTO.of(this, instructorProjections, participantProjections)
    }
    
    private fun SeminarEntity.toGroupDto(userSeminarList: MutableList<UserSeminarEntity>?): SeminarGroupByDTO
        = SeminarGroupByDTO.of(this.toDTO(userSeminarList))
    
    
    private fun checkInstructingSeminars(userId: Long): Int {
        val seminars = userSeminarRepository.findAllByUser_Id(userId)
        return if (seminars.isEmpty()) 0
            else seminars.filter { (it.role == INSTRUCTOR) && it.isActive }.size
    }
    
    private fun checkCapacity(seminarId: Long): Int {
        val participants = userSeminarRepository.findAllBySeminar_Id(seminarId)
            .filter{ (it.role == PARTICIPANT) && (it.isActive) }
        return participants.size
    }
    
    
    
    @Transactional
    override fun makeSeminar(userId: Long, request: SeminarRequest): SeminarDTO {
        val entity = userRepository.findById(userId).get()
        if (entity.role != INSTRUCTOR)
            throw SeminarException(ErrorCode.MAKE_SEMINAR_FORBIDDEN)
        if (checkInstructingSeminars(entity.id) > 0)
            throw SeminarException(ErrorCode.ALREADY_INSTRUCTED)

        // 진행자 자격 여부 확인 완료
        var newSeminar: SeminarEntity = request.run {
            SeminarEntity(
                name = this.name!!,
                instructor = entity.username,
                capacity = this.capacity!!,
                count = this.count!!,
                time = this.time!!,
                online = this.online ?: true
            )
        }
        newSeminar.userSeminarList = mutableListOf(UserSeminarEntity(user = entity, seminar = newSeminar))
        val seminar = seminarRepository.save(newSeminar)
        
        return seminar.toDTO()
    }

    
    @Transactional
    override fun editSeminar(userId: Long, request: SeminarDTO): SeminarDTO {
        // request body에 seminar_id 값이 들어오지 않을 수도 있음
        // 현재 이 API를 요청한 유저가 instructor인지 확인
        val instructor = userRepository.findById(userId).get()
        if (instructor.role != INSTRUCTOR) throw SeminarException(ErrorCode.EDIT_SEMINAR_FORBIDDEN)
        // 이 instructor가 가르치고 있는 세미나 확인
        val instructingSeminar = userSeminarRepository.findAllByUser_Id(instructor.id).run {
            this.find{ it.role == INSTRUCTOR && it.isActive }?.seminar
        }?: throw SeminarException(ErrorCode.EDIT_SEMINAR_FORBIDDEN)  // 담당 세미나 x. 즉, 수정 가능한 세미나 없음 -> 403 에러
        
        // 만약 request에 id (=seminar_id) 값이 들어왔다면, 유저가 가르치는 세미나의 id와 일치하는지 확인
        if (request.id != null && instructingSeminar.id != request.id) {
            throw SeminarException(ErrorCode.EDIT_SEMINAR_FORBIDDEN)
        } else {
            if(request.name != null) instructingSeminar.name = request.name!!
            if(request.capacity != null) instructingSeminar.capacity = request.capacity!!
            if(request.count != null) instructingSeminar.count = request.count!!
            if(request.time != null) instructingSeminar.time = request.time!!
            if(request.online != null) instructingSeminar.online = request.online!!
        }
        
        // 수정된 버전, repository에다가 저장
        val seminar = seminarRepository.save(instructingSeminar)
        return seminar.toDTO()
    }
    

    // 세미나명에 name이 포함된 세미나가 전혀 없다면 빈 리스트를 반환
    @Transactional(readOnly = true)
    override fun findSeminarsContainingWord(word: String?, order: String?): List<SeminarGroupByDTO> {
        val seminars = seminarRepository.findSeminarsContainingWord(word, order)
        return seminars.map { seminar -> seminar.toGroupDto(seminar.userSeminarList) }
    }
        

    @Transactional(readOnly = true)
    override fun findSeminarById(seminarId: Long): SeminarDTO {
        val seminar = seminarRepository.findByIdOrNull(seminarId) ?:
            throw SeminarException(ErrorCode.SEMINAR_NOT_FOUND)
        
        return seminar.toDTO()
    }
    
    
    @Transactional
    override fun registerSeminar(userId: Long, seminarId: Long, request: RegisterRequest): SeminarDTO {
        // (예외 처리 1) 세미나 정보가 존재하는지 확인 -> 없으면 404 에러
        var seminar = seminarRepository.findByIdOrNull(seminarId)?:
            throw SeminarException(ErrorCode.SEMINAR_NOT_FOUND)
        // 유저 정보 찾기
        var user = userRepository.findByIdOrNull(userId)!!
         
        // 기존 수강 이력 존재? - 중도 포기 or 현재 참여중
        val oldReg = userSeminarRepository.findByUser_IdAndSeminar_Id(user.id, seminarId)?.also {
            // (예외 처리 2) 수강생, 기 드랍 세미나의 재 수강 요청
            if (it.role == PARTICIPANT && !it.isActive)
                throw SeminarException(ErrorCode.ALREADY_DROPPED)
            // (예외 처리 3) 수강생, 현재 참여하고 있는 세미나에 또 다시 수강 요청
            else
                throw SeminarException(ErrorCode.ALREADY_PARTICIPATE)
        } // -> null이면 해당 강좌를 이전에 수강한 이력이 없는 거니까 상관없음. 그게 맞는 거임.
        
        // 세미나 참여 & 세미나 함께 진행
        var newReg = UserSeminarEntity(user, seminar)
        
        when (RoleType.valueOf(request.role!!)) {
            PARTICIPANT -> {
                // 정말로 이 유저가 참여자인지 확인 -> ParticipantProfile을 가지고 있는지
                if(user.participantProfile == null)
                    throw SeminarException(ErrorCode.INVALID_REGISTER_REQUEST)
                // 활성회원 여부 확인
                if (user.participantProfile!!.isRegistered == false)
                    throw SeminarException(ErrorCode.NOT_REGISTERED)
                // 세미나 정원 확인
                if(checkCapacity(seminarId) >= seminar.capacity)
                    throw SeminarException(ErrorCode.FULL_CAPACITY)
                
                // 최종적으로 세미나 등록
                newReg.role = PARTICIPANT
            }
            INSTRUCTOR -> {
                // 정말로 진행자 자격이 있는지 확인
                if(user.instructorProfile == null)
                    throw SeminarException(ErrorCode.INVALID_REGISTER_REQUEST)
                // 담당하고 있는 세미나가 존재하는지 확인
                if(checkInstructingSeminars(user.id) > 0)
                    throw SeminarException(ErrorCode.ALREADY_INSTRUCTED)
                
                // 없다면 세미나 함께 진행하도록 등록
                newReg.role = INSTRUCTOR
            }
        }
        //userSeminarRepository.save(newReg)
        user.seminarList!!.add(newReg)
        seminar.userSeminarList!!.add(newReg)
        
        return seminar.toDTO()
    }
    
    
    @Transactional
    override fun dropSeminar(userId: Long, seminarId: Long): SeminarDTO {
        // (예외 처리 1) 해당 세미나가 존재하지 않음 -> 404 에러
        val seminar = seminarRepository.findByIdOrNull(seminarId) ?:
            throw SeminarException(ErrorCode.SEMINAR_NOT_FOUND)
        
        // 드랍하려는 유저
        var user = userRepository.findById(userId).get()
        var entity = userSeminarRepository.findByUser_IdAndSeminar_Id(user.id, seminarId) ?:
            // (예외 처리 2) 본 세미나 참여 이력이 없음에도 드랍 요청 -> 그냥 무시 (status 200)
            throw SeminarException(ErrorCode.NOT_PARTICIPATED_SEMINAR)
        
        // (예외 처리 3) 본 세미나 진행자의 드랍 요청 -> 400 에러
        if(entity.role == INSTRUCTOR)
            throw SeminarException(ErrorCode.INSTRUCTOR_CANNOT_DROP)
        
        // 드랍처리
        // (예외 처리 3) 이미 드랍한 강의인데 재 드랍 요청 -> 400 에러
        if(!entity.isActive) throw SeminarException(ErrorCode.ALREADY_DROPPED)
        // 정상적인 드랍 요청 수행
        entity.isActive = false
        entity.droppedAt = LocalDateTime.parse(
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        )
        userSeminarRepository.save(entity)
        
        return seminar.toDTO()
    }
}