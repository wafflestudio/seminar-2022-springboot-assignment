package com.wafflestudio.seminar.survey.service

import com.wafflestudio.seminar.survey.database.OsRepository
import com.wafflestudio.seminar.survey.domain.OperatingSystem
import org.springframework.stereotype.Service

@Service
class OsServiceImpl(
    private val osRepository: OsRepository
) : OsService {
    override fun findAll(): List<OperatingSystem> {
        return osRepository.findAll()
    }

    override fun findById(id: Long): OperatingSystem {
        return osRepository.findById(id) ?: throw NoSuchElementException("해당하는 id의 os가 없습니다")
    }

    override fun findByName(name: String): OperatingSystem {
        return osRepository.findByName(name) ?: throw NoSuchElementException("해당하는 이름의 os가 없습니다")
    }
}