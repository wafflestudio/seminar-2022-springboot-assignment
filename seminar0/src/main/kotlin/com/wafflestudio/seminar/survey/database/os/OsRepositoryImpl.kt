package com.wafflestudio.seminar.survey.database.os

import com.wafflestudio.seminar.survey.domain.OperatingSystem
import org.springframework.stereotype.Repository

@Repository
class OsRepositoryImpl: OsRepository {
    override fun findAll(): List<OperatingSystem> {
        TODO("Not yet implemented")
    }

    override fun findById(id: Long): OperatingSystem {
        TODO("Not yet implemented")
    }
}