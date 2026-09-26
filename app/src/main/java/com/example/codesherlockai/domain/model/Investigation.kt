package com.example.codesherlockai.domain.model

data class Investigation(
    val id: String,
    val issueNumber: String,
    val title: String,
    val description: String,
    val status: InvestigationStatus,
    val timeAgo: String,
    val repository: String,
    val instruction: String,
    val depth: InvestigationDepth = InvestigationDepth.STANDARD,
    val result: InvestigationResult? = null
)
