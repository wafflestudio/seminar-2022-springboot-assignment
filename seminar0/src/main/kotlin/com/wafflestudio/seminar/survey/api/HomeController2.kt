package com.wafflestudio.seminar.survey.api

import org.springframework.web.bind.annotation.RequestMapping
import lombok.RequiredArgsConstructor
import com.wafflestudio.seminar.survey.service.StudentService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import com.wafflestudio.seminar.survey.domain.SurveyResponse
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
/*
@Controller
@RequestMapping("/items")
@RequiredArgsConstructor
class HomeController2 {
    private val itemService: StudentService? = null
    @GetMapping
    fun items(@ModelAttribute("itemSearch") itemSearch: SurveyResponse?, model: Model): String {
        val items = itemService!!.findAll(itemSearch!!)
        model.addAttribute("items", items)
        return "items"
    }
}*/