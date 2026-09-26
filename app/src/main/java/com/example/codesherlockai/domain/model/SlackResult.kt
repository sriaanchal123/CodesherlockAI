package com.example.codesherlockai.domain.model

data class SlackResult(
    val channel: String,
    val message: String,
    val notified: Boolean
)
