package com.wafflestudio.seminar.core.seminar.service


import com.wafflestudio.seminar.common.*
import com.wafflestudio.seminar.core.seminar.api.request.CreateSeminarRequest
import com.wafflestudio.seminar.core.seminar.api.request.UpdateSeminarRequest
import com.wafflestudio.seminar.core.seminar.database.SeminarEntity
import com.wafflestudio.seminar.core.seminar.database.SeminarRepository
import com.wafflestudio.seminar.core.seminar.domain.Seminar
import com.wafflestudio.seminar.core.user.api.request.ParticipateRequest
import com.wafflestudio.seminar.core.user.api.request.SignUpRequest
import com.wafflestudio.seminar.core.user.database.UserEntity
import com.wafflestudio.seminar.core.user.database.UserRepository
import com.wafflestudio.seminar.core.user.domain.Participant
import com.wafflestudio.seminar.core.user.domain.Profile
import com.wafflestudio.seminar.core.user.domain.User
import org.hibernate.sql.Update
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.time.LocalDateTime

interface SeminarService{
    fun create(request: CreateSeminarRequest, userId: Long)
    fun update(request: UpdateSeminarRequest, userId: Long)
    fun get(seminarId : Long)
    fun getAll()
    fun getAllDesc()
    fun getByName(name : String)
    fun getByNameDesc(name : String)
}

@Service
class SeminarServiceImpl(
    private val seminarRepository: SeminarRepository,
) : SeminarService{
    override fun create(request: CreateSeminarRequest, userId : Long) {
        validateCreation(request)
        val entity = SeminarEntity(
            name = request.name,
            capacity = request.capacity,
            count = request.count,
            time = request.time,
            online = request.online,
            madeBy = userId
        )
        seminarRepository.save(entity)
    }

    override fun update(request: UpdateSeminarRequest, userId: Long) {
        val seminar = seminarRepository.findByIdOrNull(request.id)?: throw Seminar404("없는 seminar_id 입니다.")
        if(seminar.madeBy != userId){
            throw Seminar403("만든 사람만이 세미나를 수정할 수 있습니다.")
        }
        
        seminar.count = request.count
        seminar.name = request.name
        seminar.capacity = request.capacity
        seminar.count = request.count
        seminar.time = request.time
        seminar.online = request.online
        seminarRepository.save(seminar)
    }

    override fun get(seminarId: Long) {
        val seminar = seminarRepository.findByIdOrNull(seminarId)?: throw Seminar404("세미나가 존재하지 않습니다.")
        throw Seminar200(seminar.toString())
    }

    override fun getAll() {
        val seminars  = seminarRepository.findAll()
        throw Seminar200(seminars.map{it.toString()}.toString())
    }

    override fun getAllDesc() {
        val seminars  = seminarRepository.findAll()
        val comparator : Comparator<SeminarEntity> = compareBy{it.createdAt}
        throw Seminar200(seminars.sortedWith(comparator).map{it.toString()}.toString())
    }

    override fun getByName(name: String) {
        val seminars = seminarRepository.findByName(name)
        val comparator : Comparator<SeminarEntity> = compareByDescending{it.createdAt}
        throw Seminar200(seminars.sortedWith(comparator).map{toString()}.toString())
        
    }

    override fun getByNameDesc(name: String) {
        val seminars = seminarRepository.findByName(name)
        val comparator : Comparator<SeminarEntity> = compareBy{it.createdAt}
        throw Seminar200(seminars.sortedWith(comparator).map{toString()}.toString())
    }

    fun validateCreation(request: CreateSeminarRequest){
        if(request.name == "" || request.capacity <= 0 || request.count <= 0){
            throw Seminar400("잘못된 값을 입력했습니다.")
        }
    }


}