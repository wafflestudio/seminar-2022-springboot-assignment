package com.wafflestudio.seminar.core.seminar.api

import com.wafflestudio.seminar.common.AuthInstructor
import com.wafflestudio.seminar.common.Authenticated
import com.wafflestudio.seminar.common.UserContext
import com.wafflestudio.seminar.core.seminar.api.request.CreateSeminarRequest
import com.wafflestudio.seminar.core.seminar.api.request.JoinSeminarRequest
import com.wafflestudio.seminar.core.seminar.api.request.UpdateSeminarRequest
import com.wafflestudio.seminar.core.seminar.domain.SeminarDetailInfo
import com.wafflestudio.seminar.core.seminar.domain.SeminarInfo
import com.wafflestudio.seminar.core.seminar.service.SeminarService
import com.wafflestudio.seminar.core.user.domain.UserRole
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
class SeminarController(
    private val seminarService: SeminarService
) {

    @Authenticated
    @AuthInstructor
    @PostMapping("/api/v1/seminar")
    fun createSeminar(
        @UserContext userid: Long,
        @RequestBody @Valid createSeminarRequest: CreateSeminarRequest,
    ): SeminarDetailInfo {
        return seminarService.createSeminar(userid, createSeminarRequest)
    }

    @Authenticated
    @PutMapping("/api/v1/seminar")
    fun updateSeminar(
        @UserContext userid: Long,
        @RequestBody @Valid updateSeminarRequest: UpdateSeminarRequest,
    ): SeminarDetailInfo {
        return seminarService.updateSeminar(userid, updateSeminarRequest)
    }

    @Authenticated
    @GetMapping("/api/v1/seminar")
    fun getSeminarAll(
        @RequestParam("name") name: String?,
        @RequestParam("order") order: String?,
    ): List<SeminarInfo> {
        return seminarService.getSeminarOption(name, order)
    }

    @Authenticated
    @GetMapping("/api/v1/seminar/{seminar_id}")
    fun getSeminarById(
        @PathVariable seminar_id: Long
    ): SeminarInfo {
        return seminarService.getSeminarById(seminar_id)
    }

    @Authenticated
    @PostMapping("/api/v1/seminar/{seminar_id}/user")
    fun joinSeminar(
        @UserContext user_id: Long,
        @PathVariable seminar_id: Long,
        @RequestBody @Valid joinSeminarRequest: JoinSeminarRequest,
    ): SeminarDetailInfo {
        return when (joinSeminarRequest.role) {
            UserRole.PARTICIPANT -> seminarService.participateSeminar(user_id, seminar_id)
            UserRole.INSTRUCTOR -> seminarService.instructSeminar(user_id, seminar_id)
        }
    }

    @Authenticated
    @DeleteMapping("/api/v1/seminar/{seminar_id}/user")
    fun dropSeminar(
        @UserContext user_id: Long,
        @PathVariable seminar_id: Long,
    ): SeminarDetailInfo {
        return seminarService.dropSeminar(user_id, seminar_id)
    }

}