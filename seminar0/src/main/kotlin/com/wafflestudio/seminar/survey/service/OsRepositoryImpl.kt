package com.wafflestudio.seminar.survey.service

import com.wafflestudio.seminar.survey.database.OsDB
import com.wafflestudio.seminar.survey.database.OsRepository
import com.wafflestudio.seminar.survey.domain.OperatingSystem
import com.wafflestudio.seminar.survey.exception.OSNameNotFoundException
import org.springframework.stereotype.Service

@Service
class OsRepositoryImpl(
    private val osDB: OsDB
) : OsRepository {

    override fun findAll(): List<OperatingSystem> {
        return osDB.getOperatingSystems()
    }

    override fun findById(id: Long): OperatingSystem {
        try {
            return osDB.getOperatingSystemById(id)
        } catch (e: OSNameNotFoundException) {
            throw e
        }
    }

    override fun findByOSName(osName: String): OperatingSystem {
        try {
            return osDB.getOperatingSystemByOSName(osName)
        } catch (e: OSNameNotFoundException) {
            throw e
        }
    }
}