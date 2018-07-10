package com.pager.teamapp.models

data class Person(
        val name: String,
        val avatar: String,
        val github: String,
        val role: Int,
        val gender: String,
        val languages: List<String>,
        val tags: List<String>,
        val location: String)