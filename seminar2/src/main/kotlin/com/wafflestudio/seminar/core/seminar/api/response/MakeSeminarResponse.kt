package com.wafflestudio.seminar.core.seminar.api.response

import java.util.Arrays

data class MakeSeminarResponse (
        val id: Long,
        val name: String,
        val capacity: Long,
        val count: Long,
        val time: String,
        val online: Boolean,
        val intructors: ArrayList<InstructorResponse>,
        val participants: ArrayList<ParticipantResponse>,
)