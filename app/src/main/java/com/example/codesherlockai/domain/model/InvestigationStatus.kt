package com.example.codesherlockai.domain.model

enum class InvestigationStatus(val label: String) {
    ROOT_CAUSE_FOUND("Root Cause Found"),
    JIRA_CREATED("Jira Created"),
    INVESTIGATED("Investigated"),
    IN_PROGRESS("In Progress"),
    FAILED("Failed")
}
