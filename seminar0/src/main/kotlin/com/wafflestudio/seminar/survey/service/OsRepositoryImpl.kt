package com.wafflestudio.seminar.survey.service

import com.wafflestudio.seminar.survey.database.OsRepository
import com.wafflestudio.seminar.survey.domain.OperatingSystem
import org.springframework.stereotype.Service

@Service
class OsRepositoryImpl: OsRepository {

    override fun findAll(): List<OperatingSystem> {
        TODO("Not yet implemented")
    }

    override fun findById(id: Long): OperatingSystem {
        TODO("Not yet implemented")
    }

    override fun findByOSName(osName: String): List<OperatingSystem> {
        TODO("Not yet implemented")
    }
}