package com.wafflestudio.seminar.survey.service

import com.wafflestudio.seminar.survey.database.OsRepository
import com.wafflestudio.seminar.survey.domain.OperatingSystem
import com.wafflestudio.seminar.survey.exception.APIException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service


@Service
class OsService(val osRepository: OsRepository) {
    
    fun getAllOs(): List<OperatingSystem> {
        return osRepository.findAll()
    }
    
    fun getOsById(id: Long): OperatingSystem {
        val operatingSystem: OperatingSystem? = osRepository.findById(id)
        if (operatingSystem != null) {
            return operatingSystem
        } else {
            throw APIException(HttpStatus.BAD_REQUEST, "BAD_REQUEST(400) - Invalid OS Id: $id")
        }
    }
    
    fun getOsByName(osName: String) : OperatingSystem {
        val operatingSystem: OperatingSystem? = osRepository.findByName(osName)
        if (operatingSystem != null) {
            return operatingSystem
        } else {
            throw APIException(HttpStatus.BAD_REQUEST, "BAD_REQUEST(400) - Invalid OS Name: $osName")
        }
    }
}