package com.wafflestudio.seminar.survey.api

import com.wafflestudio.seminar.survey.domain.OperatingSystem
import com.wafflestudio.seminar.survey.service.OsService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/os")
class OsController(
    private val osService: OsService,
) {
    
    @GetMapping("/id={id}")
    fun getListById(
        @PathVariable id: Long,
    ): OperatingSystem {
        return osService.findById(id)
    }
    
    @GetMapping("/name")
    fun getListByName(
        @RequestParam osName: String,
    ): OperatingSystem {
        return osService.findByName(osName)
    }
    
}