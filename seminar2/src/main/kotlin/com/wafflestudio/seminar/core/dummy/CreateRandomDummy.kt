package com.wafflestudio.seminar.core.dummy

import com.wafflestudio.seminar.core.seminar.api.request.ParticipateSeminarRequest
import com.wafflestudio.seminar.core.seminar.api.request.SeminarRequest
import com.wafflestudio.seminar.core.seminar.service.SeminarService
import com.wafflestudio.seminar.core.user.api.request.RegisterParticipantRequest
import com.wafflestudio.seminar.core.user.api.request.SignUpRequest
import com.wafflestudio.seminar.core.user.database.UserRepository
import com.wafflestudio.seminar.core.user.service.AuthToken
import com.wafflestudio.seminar.core.user.service.UserService
import com.wafflestudio.seminar.core.user.type.UserRole
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import kotlin.random.Random

@Component
class CreateRandomDummy(
    private val userService: UserService,
    private val userRepository: UserRepository,
    private val seminarService: SeminarService
) {
    
    @EventListener(ApplicationStartedEvent::class)
    fun createDummyData() {
        val startIndex: Long = userRepository.findTopByOrderByIdDesc()?.id ?: 0
        val seminarIdList : MutableList<Long> = mutableListOf()
        (1 .. 100).forEach { 
            val index = it + startIndex
            var role = if (Random.nextDouble() <= 0.33) {
                UserRole.INSTRUCTOR
            } else {
                UserRole.PARTICIPANT
            }
            val email = "dummy${index}@google.com"
            val authToken: AuthToken = userService.signUp(
                SignUpRequest(
                    email = email,
                    password = "dummy",
                    username = "dummyUser${index}",
                    role = role,
                    university = "SNU",
                    isRegistered = true,
                    company = "WaffleStudio",
                    year = (1..10).random()
                )
            )
            
            
            val user = userRepository.findByEmail(email)
            
            if (role == UserRole.INSTRUCTOR && Random.nextDouble() < 0.5) {
                userService.registerParticipantForInstructor(
                    user.get(),
                    RegisterParticipantRequest(
                        university = "SNU",
                        isRegistered = true
                    )
                )
                role = UserRole.BOTH
            }
            
            var newSeminarId: Long = -1
            if (role != UserRole.PARTICIPANT && Random.nextDouble() < 0.5) {
                seminarService.createSeminar(
                    user.get(),
                    SeminarRequest(
                        name = listOf("Spring", "Django", "React", "Android", "iOS").random() + index.toString(),
                        capacity = (10..30).random(),
                        count = (1..5).random(),
                        time = "15:30",
                        online = Random.nextDouble() <= 0.5
                    )
                ).let { seminarInfo -> 
                    seminarIdList.add(seminarInfo.id) 
                    newSeminarId = seminarInfo.id
                }
            }
            
            if (seminarIdList.size > 0) {
                if (role != UserRole.PARTICIPANT) {
                    if (Random.nextDouble() < 0.5) {
                        for(i: Int in 1 .. (2..4).random()) {
                            try {
                                seminarService.participateSeminar(
                                    seminarId = seminarIdList.filter { id ->
                                        id != newSeminarId
                                    }.random(),
                                    user = user.get(),
                                    request = ParticipateSeminarRequest(
                                        role = listOf(UserRole.INSTRUCTOR, UserRole.PARTICIPANT).random(),
                                    )
                                )
                            } catch (_: Exception) {
                            }
                        }
                    }
                } else {
                    for(i: Int in 1 .. (2..3).random()) {
                        try {
                            seminarService.participateSeminar(
                                seminarId = seminarIdList.filter { id ->
                                    id != newSeminarId
                                }.random(),
                                user = user.get(),
                                request = ParticipateSeminarRequest(
                                    role = UserRole.PARTICIPANT,
                                )
                            )
                        } catch (_: Exception) { }
                    }
                }
            }
        }
    }
}