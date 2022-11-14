package com.wafflestudio.seminar.core.seminar.api

import com.wafflestudio.seminar.common.Authenticated
import com.wafflestudio.seminar.common.UserContext
import com.wafflestudio.seminar.core.seminar.api.request.EditSeminarRequest
import com.wafflestudio.seminar.core.seminar.api.request.RoleRequest
import com.wafflestudio.seminar.core.seminar.api.request.SeminarRequest
import com.wafflestudio.seminar.core.seminar.domain.Seminar
import com.wafflestudio.seminar.core.seminar.service.SeminarService
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/seminar")
class SeminarController(
    private val seminarService: SeminarService
) {
    @Authenticated
    @PostMapping("/")
    fun makeSeminar(
        @UserContext userId: Long,
        @Valid @RequestBody seminarRequest: SeminarRequest,
    ) = seminarService.makeSeminar(userId, seminarRequest)

    @Authenticated
    @PutMapping("/")
    fun editSeminar(
        @UserContext userId: Long,
        @Valid @RequestBody seminarRequest: EditSeminarRequest,
    ) = seminarService.editSeminar(userId, seminarRequest)

    @Authenticated
    @GetMapping("/{seminar_id}/")
    fun getSeminar(
        @UserContext userId: Long,
        @PathVariable("seminar_id") seminarId: Long,
    ) = seminarService.getSeminar(seminarId)

    @Authenticated
    @GetMapping("/")
    fun getAllSeminar(
        @UserContext userId: Long,
        @RequestParam("name", required = false) name: String?,
        @RequestParam("order", required = false) order: String?,
        @RequestParam("page", required = false, defaultValue = "1") page: Int
    ): List<Seminar> {
        val pageRequest = PageRequest.of(page - 1, 50)
        return seminarService.getAllSeminar(name, order, pageRequest)
    }

    @Authenticated
    @PostMapping("/{seminar_id}/user/")
    fun addSeminar(
        @UserContext userId: Long,
        @PathVariable("seminar_id") seminarId: Long,
        @RequestBody roleRequest: RoleRequest,
    ) = seminarService.addSeminar(userId, seminarId, roleRequest)

    @Authenticated
    @DeleteMapping("/{seminar_id}/user/")
    fun dropSeminar(
        @UserContext userId: Long,
        @PathVariable("seminar_id") seminarId: Long,
    ) = seminarService.dropSeminar(userId, seminarId)
}