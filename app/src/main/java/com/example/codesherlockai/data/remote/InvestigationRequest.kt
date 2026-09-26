package com.example.codesherlockai.data.remote

data class InvestigationRequest(
    val issue_number: Int,
    val repository: String,
    val instruction: String,
    val depth: String = "standard"
)
