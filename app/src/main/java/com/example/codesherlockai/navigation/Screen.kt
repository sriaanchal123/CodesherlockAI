package com.example.codesherlockai.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Dashboard : Screen("dashboard")
    object NewInvestigation : Screen("new_investigation")
    object AgentExecution : Screen("agent_execution/{issueNumber}") {
        fun createRoute(issueNumber: String) = "agent_execution/${issueNumber.replace("#", "")}"
    }
    object InvestigationResult : Screen("investigation_result/{issueNumber}") {
        fun createRoute(issueNumber: String) = "investigation_result/${issueNumber.replace("#", "")}"
    }
    object EvidenceDetails : Screen("evidence_details/{issueNumber}") {
        fun createRoute(issueNumber: String) = "evidence_details/${issueNumber.replace("#", "")}"
    }
}
