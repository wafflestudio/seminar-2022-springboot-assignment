package com.wafflestudio.seminar.survey.api

import com.wafflestudio.seminar.survey.domain.OperatingSystem
import com.wafflestudio.seminar.survey.service.OsService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class OsController(val osService: OsService) {
    
    @GetMapping("/os-all")
    fun getOsAll(): ResponseEntity<List<OperatingSystem>> {
        return ResponseEntity.ok().body(osService.getAllOs())
    }
    
    @GetMapping("/os/{osId}")
    fun getOsById(@PathVariable("osId") osId: Long) : ResponseEntity<OperatingSystem> {
        return ResponseEntity.ok().body(osService.getOsById(osId))
    }
    
    @GetMapping("/os")
    fun getOsByName(@RequestParam name: String): ResponseEntity<OperatingSystem> {
        return ResponseEntity.ok().body(osService.getOsByName(name))
    }
}