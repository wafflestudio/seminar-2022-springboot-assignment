package com.wafflestudio.seminar.core.user.service

import com.wafflestudio.seminar.core.user.api.request.CreateInstructorDTO
import com.wafflestudio.seminar.core.user.database.InstructorEntity
import com.wafflestudio.seminar.core.user.database.InstructorRepository
import org.springframework.stereotype.Service

@Service
class InstructorService(
    private val instructorRepository: InstructorRepository,
) {
    fun getParticipant(id: Long) {
        instructorRepository.findById(id)
    }

    fun createInstructor(createInstructorDTO: CreateInstructorDTO): InstructorEntity {
        val newInstructor = InstructorEntity(
            company = createInstructorDTO.company ?: "",
            year = createInstructorDTO.year,
        )
        return instructorRepository.save(newInstructor)
    }
}