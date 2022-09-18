package com.wafflestudio.seminar.survey.service

import com.wafflestudio.seminar.survey.database.OsRepository
import com.wafflestudio.seminar.survey.domain.OperatingSystem
import com.wafflestudio.seminar.survey.exception.NotFoundException
import org.springframework.stereotype.Service

@Service
class OsServiceImpl(
    private val osRepository: OsRepository,
): OsService {
    override fun getOsById(id: Long): OperatingSystem{
        return osRepository.findById(id) ?: throw NotFoundException("There is no operating system with the id.")
    }

    override fun getOsByName(name: String): OperatingSystem {
        return osRepository.findByName(name) ?: throw NotFoundException("There is no operating system with the name.")
    }
}