package com.wafflestudio.seminar.core.user.service

import com.wafflestudio.seminar.common.Seminar401
import com.wafflestudio.seminar.common.Seminar404
import com.wafflestudio.seminar.common.Seminar409
import com.wafflestudio.seminar.core.user.api.request.RegisterParticipantRequest
import com.wafflestudio.seminar.core.user.api.request.UpdateProfileRequest
import com.wafflestudio.seminar.core.user.api.response.GetProfile
import com.wafflestudio.seminar.core.user.database.*
import com.wafflestudio.seminar.core.user.domain.*
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.transaction.Transactional


@Service
class UserService(
        private val userRepository: UserRepository,
        private val userDslRepository: UserDslRepository,
        private val userSeminarDslRepository: UserSeminarDslRepository

) {
    fun getProfile(id: Long, userId: Long?): GetProfile {

        if (userId != id) {
            throw Seminar401("정보에 접근할 수 없습니다")
        }
        userRepository.findByIdOrNull(userId) ?: throw Seminar404("해당하는 유저가 없습니다")

        return getProfile(userId)
    }

    @Transactional
    fun updateProfile(request: UpdateProfileRequest, userId: Long?): GetProfile {

        val user = userRepository.findByIdOrNull(userId) ?: throw Seminar404("해당하는 유저가 없습니다")

        user.username = request.username
        user.password = request.password
        user.participant?.let { it.university = request.participant?.university }
        user.instructor?.let {
            it.company = request.instructor?.company
            it.year = request.instructor?.year
        }

        return getProfile(userId)

    }


    @Transactional
    fun registerParticipant(request: RegisterParticipantRequest, userId: Long?): GetProfile {
        
        val user = userRepository.findByIdOrNull(userId) ?: throw Seminar404("해당하는 유저가 없습니다")

        if (user.participant != null) {
            throw Seminar409("이미 참가자로 등록되어 있습니다")
        }
        user.participant = ParticipantProfileEntity(user, request.university, request.isRegistered)
        
        return getProfile(userId)
    }

    private fun getProfile(userId: Long?): GetProfile {
        val userProfile = userDslRepository.getUserProfile(userId)
        val userProfileSeminars = userSeminarDslRepository.getProfileSeminars(userId)
        val userProfileInstructingSeminars = userSeminarDslRepository.getProfileInstructingSeminars(userId)

        return GetProfile.of(userProfile, userProfileSeminars, userProfileInstructingSeminars)
    }


}