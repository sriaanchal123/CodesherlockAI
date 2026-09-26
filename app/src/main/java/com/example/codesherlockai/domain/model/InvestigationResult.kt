package com.example.codesherlockai.domain.model

data class EvidenceSummaryItem(
    val title: String,
    val detail: String,
    val iconType: String
)

data class InvestigationResult(
    val issueNumber: String,
    val probableRootCause: String,
    val confidenceScore: Int,
    val confidenceNote: String,
    val evidenceSummary: List<EvidenceSummaryItem>,
    val jiraResult: JiraResult,
    val slackResult: SlackResult,
    val evidenceDetails: EvidenceItem
)
