package com.example.codesherlockai.domain.model

data class AgentStep(
    val id: Int,
    val title: String,
    val description: String,
    val status: AgentStepStatus = AgentStepStatus.PENDING,
    val logs: List<String> = emptyList()
)
