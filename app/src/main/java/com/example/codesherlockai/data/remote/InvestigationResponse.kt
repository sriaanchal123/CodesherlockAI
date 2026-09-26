package com.example.codesherlockai.data.remote

data class InvestigationResponse(
    val status: String,
    val issue_number: Int,
    val repository: String,
    val instruction: String,
    val depth: String
)
