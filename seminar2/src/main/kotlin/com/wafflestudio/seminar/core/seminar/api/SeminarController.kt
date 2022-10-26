package com.wafflestudio.seminar.core.seminar.api

import com.wafflestudio.seminar.common.Authenticated
import com.wafflestudio.seminar.common.LogExecutionTime
import com.wafflestudio.seminar.common.UserContext
import com.wafflestudio.seminar.core.seminar.api.request.ParticipateSeminarRequest
import com.wafflestudio.seminar.core.seminar.api.request.SeminarRequest
import com.wafflestudio.seminar.core.seminar.api.response.SeminarInfo
import com.wafflestudio.seminar.core.seminar.api.response.SeminarsQueryResponse
import com.wafflestudio.seminar.core.seminar.service.SeminarService
import com.wafflestudio.seminar.core.user.database.UserEntity
import com.wafflestudio.seminar.core.user.service.AuthException
import org.springframework.data.domain.Page
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.validation.Valid

@RestController
class SeminarController(
    private val seminarService: SeminarService
) {
    
    @LogExecutionTime
    @Authenticated
    @PostMapping("/api/v1/seminar")
    fun createSeminar(
        @UserContext user: Optional<UserEntity>,
        @Valid @RequestBody seminarRequest: SeminarRequest
    ) : ResponseEntity<SeminarInfo> {
        if (user.isEmpty) {
            throw AuthException("유저를 찾을 수 없습니다")
        }
        return ResponseEntity.ok(seminarService.createSeminar(user.get(), seminarRequest))
    }

    @LogExecutionTime
    @Authenticated
    @PutMapping("/api/v1/seminar")
    fun updateSeminar(
        @UserContext user: Optional<UserEntity>,
        @Valid @RequestBody seminarRequest: SeminarRequest
    ) : ResponseEntity<SeminarInfo> {
        if (user.isEmpty) {
            throw AuthException("유저를 찾을 수 없습니다")
        }
        return ResponseEntity.ok(seminarService.updateSeminar(user.get(), seminarRequest))
    }
    
    @LogExecutionTime
    @Authenticated
    @PostMapping("/api/v1/seminar/{seminarId}/user")
    fun participateSeminar(
        @UserContext user: Optional<UserEntity>,
        @PathVariable seminarId: Long,
        @Valid @RequestBody request: ParticipateSeminarRequest
    ) : ResponseEntity<SeminarInfo> {
        if (user.isEmpty) {
            throw AuthException("유저를 찾을 수 없습니다")
        }
        return ResponseEntity.ok(seminarService.participateSeminar(seminarId, user.get(), request))
    }
    
    @LogExecutionTime
    @Authenticated
    @DeleteMapping("/api/v1/seminar/{seminarId}/user")
    fun dropSeminar(
        @UserContext user: Optional<UserEntity>,
        @PathVariable seminarId: Long,
    ) : ResponseEntity<SeminarInfo?> {
        if (user.isEmpty) {
            throw AuthException("유저를 찾을 수 없습니다")
        }
        return ResponseEntity.ok(seminarService.dropSeminar(user.get(), seminarId))
    }
    
    @LogExecutionTime
    @Authenticated
    @GetMapping("/api/v1/seminar/{seminarId}")
    fun getSeminar(@PathVariable seminarId: Long) : ResponseEntity<SeminarInfo> {
        return ResponseEntity.ok(seminarService.getSeminar(seminarId));
    }
    
    @LogExecutionTime
    @Authenticated
    @GetMapping("/api/v1/seminar/")
    fun getSeminars(
        @RequestParam(required = false, value = "name") name: String?,
        @RequestParam(required = false, value = "order") order: String?,
        @RequestParam(required = false, value = "page", defaultValue = "0") page: Int,
        @RequestParam(required = false, value = "size", defaultValue = "50") size: Int
    ) : ResponseEntity<Page<SeminarsQueryResponse>> {
        return ResponseEntity.ok(seminarService.getSeminarsByQueryParam(name, order, page, size))
    }
    
    @LogExecutionTime
    @Authenticated
    @DeleteMapping("api/v1/seminar/{seminarId}")
    fun deleteSeminar(
        @UserContext user: Optional<UserEntity>,
        @PathVariable seminarId: Long
    ) : ResponseEntity<Map<String, Long>> {
        if (user.isEmpty) {
            throw AuthException("유저를 찾을 수 없습니다")
        }
        seminarService.deleteSeminar(user.get(), seminarId)
        return ResponseEntity.ok(
            mapOf(
                "deleted_seminar_id" to seminarId 
            )
        )
        
    }
    
}