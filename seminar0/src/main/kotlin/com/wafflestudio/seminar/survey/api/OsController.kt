package com.wafflestudio.seminar.survey.api

import com.wafflestudio.seminar.survey.domain.OperatingSystem
import com.wafflestudio.seminar.survey.service.OsService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/os")
class OsController(private val osService: OsService) {

    @GetMapping("/{id}")
    fun getOsById(@PathVariable id: Long): OperatingSystem {
        val result = osService.findById(id)
        if(result.isEmpty()) {
            throw SeminarExceptionHandler().SeminarException("Error: Invalid Id", HttpStatus.NOT_FOUND)
        }
        return result[0]
    }

    @GetMapping
    fun getOsByName(@RequestParam name: String): OperatingSystem {
        val result = osService.findByName(name)
        if(result.isEmpty()) {
            throw SeminarExceptionHandler().SeminarException("Error: Invalid name", HttpStatus.NOT_FOUND)
        }
        return result[0]
    }

}