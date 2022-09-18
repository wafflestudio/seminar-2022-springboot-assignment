package com.wafflestudio.seminar.survey.api

import com.wafflestudio.seminar.survey.domain.OperatingSystem
import com.wafflestudio.seminar.survey.service.OsService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class OsController(
    private val osService: OsService,
) {
    @GetMapping("/os/{id}")
    fun getOsById(
        @PathVariable id: Long,
    ): OperatingSystem{
        return osService.getOsById(id)
    }
    
    @GetMapping("/os")
    fun getOsByName(
        @RequestParam name: String,
    ): OperatingSystem{
        return osService.getOsByName(name)
    }
}