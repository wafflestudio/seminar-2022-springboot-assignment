package com.wafflestudio.seminar.survey.api

import com.wafflestudio.seminar.survey.service.OsService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/os")
class OsController(private val osService: OsService) {
}