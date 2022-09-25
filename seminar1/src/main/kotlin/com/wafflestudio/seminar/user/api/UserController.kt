/*package com.wafflestudio.seminar.user.api

import com.wafflestudio.seminar.user.domain.User1
import com.wafflestudio.seminar.user.service.UserService
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/v1")
class UserController(private val service: UserService) {
    
    @GetMapping("/abc")
    fun index(){
        
    }
    
    @GetMapping("/find")
    fun find(user1: User1): List<User1>? {
        var user1 :List<User1>? = service.findAll()
        return user1
    }
    
    @PostMapping("/user")
    fun user(
    ) {
        val user = User1(1, "abc", "dfe", "1234")
        return service.save(user)
    }
    
     
}
*/

