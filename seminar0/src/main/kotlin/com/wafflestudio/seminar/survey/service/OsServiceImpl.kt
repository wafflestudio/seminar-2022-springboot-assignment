package com.wafflestudio.seminar.survey.service

import com.wafflestudio.seminar.survey.database.OsRepository
import com.wafflestudio.seminar.survey.domain.OperatingSystem
import org.springframework.stereotype.Service

@Service
class OsServiceImpl(private val osRepository: OsRepository) : OsService {
    override fun findById(id: Long): OperatingSystem {
        return osRepository.findById(id)
    }

    override fun findByName(name: String): OperatingSystem {
        return osRepository.findByName(name)
    }
}