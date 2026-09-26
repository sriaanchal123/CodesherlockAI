package com.example.codesherlockai.domain.model

data class TimelineStep(
    val title: String,
    val description: String
)

data class EvidenceItem(
    val githubIssue: String,
    val relatedPr: String,
    val relevantCommit: String,
    val retrievedFiles: List<String>,
    val aiReasoning: String,
    val timeline: List<TimelineStep>
)
