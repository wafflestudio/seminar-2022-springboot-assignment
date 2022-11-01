package com.wafflestudio.seminar.core.seminar.service

import com.wafflestudio.seminar.common.SeminarNotFoundException
import com.wafflestudio.seminar.core.seminar.api.dto.CreateSeminarRequest
import com.wafflestudio.seminar.core.seminar.api.dto.CreateSeminarResponse
import com.wafflestudio.seminar.core.seminar.database.SeminarRepository
import com.wafflestudio.seminar.core.seminar.domain.SeminarInfo
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

interface SeminarService {
    fun createSeminar(createSeminarRequest: CreateSeminarRequest): CreateSeminarResponse
    fun getSeminarOption(name: String?, order: String?): List<SeminarInfo>
    fun getSeminarById(seminar_id: Long): SeminarInfo
}

@Service
class SeminarServiceImpl(
    private val seminarRepository: SeminarRepository
): SeminarService {

    override fun createSeminar(createSeminarRequest: CreateSeminarRequest): CreateSeminarResponse {
        return seminarRepository.save(
            createSeminarRequest.toSeminarEntity()
        ).toCreateSeminarResponse()
    }

    override fun getSeminarOption(name: String?, order: String?): List<SeminarInfo> {
        val seminarEntityList = seminarRepository.findByNameLatest(name)
        
        if (order == "earliest") {
            seminarEntityList.reverse()
        }
        
        val seminarInfoList = mutableListOf<SeminarInfo>()
        seminarEntityList.forEach {
            seminarInfoList.add(it.toSeminarInfo())
        }
        
        return seminarInfoList
    }

    override fun getSeminarById(seminar_id: Long): SeminarInfo {
        return seminarRepository.findByIdOrNull(seminar_id)
            ?.toSeminarInfo()
            ?: throw SeminarNotFoundException
    }
    
}
