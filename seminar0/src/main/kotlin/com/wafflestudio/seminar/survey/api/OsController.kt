package com.wafflestudio.seminar.survey.api

import com.wafflestudio.seminar.survey.domain.OperatingSystem
import com.wafflestudio.seminar.survey.service.OsService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class OsController (
    private val osService: OsService
) {
    
    @GetMapping("/os/{id}")
    fun getById(@PathVariable id: Long): OperatingSystem {
        return osService.findById(id)
    }
    
    @GetMapping("/os")
    fun getByName(@RequestParam(required = false) name: String): OperatingSystem {
        return osService.findByName(name)
    }
}