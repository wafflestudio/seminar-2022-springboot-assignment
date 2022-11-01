package com.wafflestudio.seminar.core.seminar.api

import com.wafflestudio.seminar.common.Seminar400
import com.wafflestudio.seminar.core.seminar.api.request.CreateSeminarDTO
import com.wafflestudio.seminar.core.seminar.database.SeminarEntity
import com.wafflestudio.seminar.core.seminar.service.SeminarService
import com.wafflestudio.seminar.core.user.service.UserService
import org.springframework.web.bind.annotation.*

@RestController
class SeminarController(
    private val seminarService: SeminarService,
    private val userService: UserService
) {
    @PostMapping("/api/v1/seminar")
    fun createSeminar(@RequestAttribute userId: Long, @RequestBody createSeminarDTO: CreateSeminarDTO): SeminarEntity {
        val user = userService.getUser(userId)
        if (user.instructor == null) {
            throw Seminar400("세미나는 강사만 생성할 수 있습니다.")
        }
        return  seminarService.createSeminar(user, createSeminarDTO)
    }
    
    @GetMapping("/api/v1/seminar/{seminarId}")
    fun getSeminar(@PathVariable seminarId: Long) {
//        return seminarService.getSeminar(seminarId)
    }
}