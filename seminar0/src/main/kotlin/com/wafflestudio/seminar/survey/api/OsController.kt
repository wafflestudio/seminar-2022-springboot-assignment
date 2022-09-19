package com.wafflestudio.seminar.survey.api

import com.wafflestudio.seminar.survey.domain.OperatingSystem
import com.wafflestudio.seminar.survey.service.OsService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/os")
class OsController(
    private val osService: OsService,
) {
    @GetMapping("/all")
    fun getAllOperatingSystems(): List<OperatingSystem>{
        return osService.getAllOperatingSystems()
    }
    
    @GetMapping("/{id}")
    fun getOsById(
        @PathVariable id: Long,
    ): OperatingSystem{
        return osService.getOsById(id)
    }
    
    @GetMapping
    fun getOsByName(
        @RequestParam name: String,
    ): OperatingSystem{
        return osService.getOsByName(name)
    }
}