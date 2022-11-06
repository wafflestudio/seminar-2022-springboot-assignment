package com.wafflestudio.seminar.core.seminar

import com.wafflestudio.seminar.core.seminar.api.request.CreateSeminarRequest
import com.wafflestudio.seminar.core.seminar.database.SeminarEntity
import com.wafflestudio.seminar.core.seminar.database.SeminarRepository
import com.wafflestudio.seminar.core.seminar.database.UserSeminarEntity
import com.wafflestudio.seminar.core.seminar.service.SeminarService
import com.wafflestudio.seminar.core.user.UserTestHelper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.stereotype.Component

@SpringBootTest
internal class SeminarServiceTest @Autowired constructor(
        private val seminarService: SeminarService,
        private val seminarRepository: SeminarRepository,
        private val userTestHelper: UserTestHelper
){
    
    @Test
    fun `세미나를 등록할 수 있다`(){
        
        // given
        val user = userTestHelper.createInstructorUser("w1@ffle.com", password = "1234")
        val seminar = CreateSeminarRequest("seminar1",30,16,"12:30")
        
        // when
        val result = seminarService.createSeminar(user.id,seminar)
        
        // then
        assertThat(seminarRepository.findAll()).hasSize(1)
        assertThat(result.name).isEqualTo("seminar1")
    }


  
}