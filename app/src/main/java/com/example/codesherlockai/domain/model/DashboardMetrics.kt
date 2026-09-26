package com.example.codesherlockai.domain.model

data class DashboardMetrics(
    val totalInvestigations: Int = 12,
    val rootCausesFound: Int = 8,
    val jiraTasksCreated: Int = 6,
    val teamAlertsSent: Int = 9
)
