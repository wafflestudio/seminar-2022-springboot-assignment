package com.wafflestudio.seminar.survey.service

import com.wafflestudio.seminar.survey.api.Seminar404
import com.wafflestudio.seminar.survey.api.SeminarExceptionType
import com.wafflestudio.seminar.survey.database.OperatingSystemEntity
import com.wafflestudio.seminar.survey.database.OsRepository
import com.wafflestudio.seminar.survey.domain.OperatingSystem
import org.springframework.stereotype.Service

interface OSService {
    fun osForName(name: String): OperatingSystem
    fun osForId(id: Long): OperatingSystem
}

@Service
class DefaultOSService(
    val repository: OsRepository
): OSService {
    override fun osForName(name: String): OperatingSystem {
        val entity = repository.findByOsName(name) ?: throw Seminar404(SeminarExceptionType.NotExistOSForName)
        return OperatingSystem(entity)
    }

    override fun osForId(id: Long): OperatingSystem {
        val entity = repository.findById(id).orElseThrow { throw Seminar404(SeminarExceptionType.NotExistOSForId) }
        return OperatingSystem(entity)
    }

    private fun OperatingSystem(entity: OperatingSystemEntity) = entity.run {
        OperatingSystem(id, osName, price, desc)
    }
}