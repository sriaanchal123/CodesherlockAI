package com.example.codesherlockai.data.repository

import android.util.Log
import com.example.codesherlockai.data.remote.InvestigationRequest
import com.example.codesherlockai.data.remote.InvestigationResponse
import com.example.codesherlockai.data.remote.RetrofitClient
import com.example.codesherlockai.domain.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class InvestigationRepository {

    private val _investigations = MutableStateFlow<List<Investigation>>(getInitialMockInvestigations())
    val investigations: StateFlow<List<Investigation>> = _investigations.asStateFlow()

    private val _metrics = MutableStateFlow(
        DashboardMetrics(
            totalInvestigations = 12,
            rootCausesFound = 8,
            jiraTasksCreated = 6,
            teamAlertsSent = 9
        )
    )
    val metrics: StateFlow<DashboardMetrics> = _metrics.asStateFlow()

    suspend fun investigateBackend(
        issueNumber: Int,
        repository: String,
        instruction: String,
        depth: String = "standard"
    ): Result<InvestigationResponse> {
        return try {
            Log.d("CodeSherlockAPI", "Request started: issue_number=$issueNumber, repository=$repository, instruction=$instruction, depth=$depth")
            val request = InvestigationRequest(
                issue_number = issueNumber,
                repository = repository,
                instruction = instruction,
                depth = depth
            )
            val response = RetrofitClient.api.investigate(request)
            Log.d("CodeSherlockAPI", "Request successful: status=${response.status}, issue_number=${response.issue_number}")
            Result.success(response)
        } catch (e: java.net.ConnectException) {
            Log.e("CodeSherlockAPI", "Request failed: Connection refused", e)
            Result.failure(Exception("Cannot connect to local server at http://10.0.2.2:8000/. Is FastAPI running?"))
        } catch (e: java.net.SocketTimeoutException) {
            Log.e("CodeSherlockAPI", "Request failed: Connection timed out", e)
            Result.failure(Exception("Request timed out. Please check backend server."))
        } catch (e: retrofit2.HttpException) {
            Log.e("CodeSherlockAPI", "Request failed with HTTP error ${e.code()}", e)
            Result.failure(Exception("Server returned HTTP error ${e.code()}."))
        } catch (e: Exception) {
            Log.e("CodeSherlockAPI", "Request failed with exception: ${e.message}", e)
            Result.failure(Exception(e.message ?: "An unexpected error occurred."))
        }
    }

    private fun getInitialMockInvestigations(): List<Investigation> {
        return listOf(
            Investigation(
                id = "421",
                issueNumber = "#421",
                title = "Payment Crash",
                description = "Probable payment response regression",
                status = InvestigationStatus.ROOT_CAUSE_FOUND,
                timeAgo = "2 min ago",
                repository = "company/android-app",
                instruction = "Find the probable root cause of payment crash on checkout",
                depth = InvestigationDepth.STANDARD,
                result = getMockResultForIssue("#421")
            ),
            Investigation(
                id = "398",
                issueNumber = "#398",
                title = "Login Failure",
                description = "Authentication state issue",
                status = InvestigationStatus.JIRA_CREATED,
                timeAgo = "1 hour ago",
                repository = "company/android-app",
                instruction = "Investigate token refresh failure during authentication",
                depth = InvestigationDepth.STANDARD,
                result = getMockResultForIssue("#398")
            ),
            Investigation(
                id = "374",
                issueNumber = "#374",
                title = "Notification Crash",
                description = "Null response handling",
                status = InvestigationStatus.INVESTIGATED,
                timeAgo = "3 hours ago",
                repository = "company/android-app",
                instruction = "Analyze crash in PushNotificationService upon receiving empty payload",
                depth = InvestigationDepth.QUICK,
                result = getMockResultForIssue("#374")
            )
        )
    }

    fun getInvestigationByNumber(issueNumber: String): Investigation {
        val cleanNumber = formatIssueNumber(issueNumber)
        return _investigations.value.find { it.issueNumber == cleanNumber }
            ?: createDefaultInvestigation(cleanNumber)
    }

    fun addInvestigation(
        issueNumber: String,
        repository: String,
        instruction: String,
        depth: InvestigationDepth
    ): Investigation {
        val cleanNumber = formatIssueNumber(issueNumber)
        val newInv = Investigation(
            id = cleanNumber.replace("#", ""),
            issueNumber = cleanNumber,
            title = "Issue $cleanNumber Investigation",
            description = instruction.ifBlank { "Probable root cause identified by CodeSherlock AI" },
            status = InvestigationStatus.ROOT_CAUSE_FOUND,
            timeAgo = "Just now",
            repository = repository.ifBlank { "company/android-app" },
            instruction = instruction,
            depth = depth,
            result = getMockResultForIssue(cleanNumber)
        )

        _investigations.value = listOf(newInv) + _investigations.value.filterNot { it.issueNumber == cleanNumber }
        _metrics.value = _metrics.value.copy(
            totalInvestigations = _metrics.value.totalInvestigations + 1,
            rootCausesFound = _metrics.value.rootCausesFound + 1
        )
        return newInv
    }

    fun getMockAgentSteps(): List<AgentStep> {
        return listOf(
            AgentStep(
                id = 1,
                title = "Understanding Request",
                description = "Parsing user prompt & issue parameters",
                logs = listOf("Parsed input prompt for issue parameters", "Identified target area: Application core workflow")
            ),
            AgentStep(
                id = 2,
                title = "Fetching GitHub Issue",
                description = "Retrieving issue context and stacktrace",
                logs = listOf("Fetched issue details from GitHub API", "Extracted NullPointerException stacktrace from crash logs")
            ),
            AgentStep(
                id = 3,
                title = "Finding Related PRs",
                description = "Searching for recently merged PRs in affected modules",
                logs = listOf("Found PR #142 related to module refactoring", "PR #142 merged 2 days ago by @dev_team")
            ),
            AgentStep(
                id = 4,
                title = "Analyzing Recent Commits",
                description = "Diffing commit b72c4 against main branch",
                logs = listOf("Analyzing diff in target source files", "Found recent response parsing change in deserialize()")
            ),
            AgentStep(
                id = 5,
                title = "Retrieving RAG Evidence",
                description = "Querying vector database for code context",
                logs = listOf("Retrieved Repository.kt", "Retrieved Response.kt", "Vector similarity score: 0.92")
            ),
            AgentStep(
                id = 6,
                title = "Analyzing Root Cause",
                description = "Reasoning over retrieved code & commit diffs",
                logs = listOf("Identified nullable field mismatch in response model", "ViewModel expects non-null payload")
            ),
            AgentStep(
                id = 7,
                title = "Checking Jira",
                description = "Checking for existing bug reports or active epics",
                logs = listOf("Searched Jira project for duplicate issues", "No active duplicate found")
            ),
            AgentStep(
                id = 8,
                title = "Creating Jira Task",
                description = "Generating detailed bug report with root cause",
                logs = listOf("Created ticket PAY-155: 'Fix NullPointerException in ViewModel'", "Assigned priority: High")
            ),
            AgentStep(
                id = 9,
                title = "Notifying Slack",
                description = "Sending summary alert to development channel",
                logs = listOf("Posted investigation breakdown to #dev-alerts", "Tagged @oncall-eng")
            ),
            AgentStep(
                id = 10,
                title = "Verifying Actions",
                description = "Ensuring all agent side-effects succeeded",
                logs = listOf("All 9 upstream actions validated", "Investigation artifact finalized")
            )
        )
    }

    fun getMockResultForIssue(issueNumber: String): InvestigationResult {
        val cleanNumber = formatIssueNumber(issueNumber)
        return when (cleanNumber) {
            "#398" -> InvestigationResult(
                issueNumber = "#398",
                probableRootCause = "Authentication token refresh state invalidation",
                confidenceScore = 92,
                confidenceNote = "Confidence represents the agent's assessment based on available repository evidence and should be reviewed by a developer.",
                evidenceSummary = listOf(
                    EvidenceSummaryItem("GitHub Issue", "#398 — Login Failure", "github"),
                    EvidenceSummaryItem("Related PR", "#128 — Auth token refactoring", "pr"),
                    EvidenceSummaryItem("Commit", "a41f9 — Updated AuthRepository refresh logic", "commit"),
                    EvidenceSummaryItem("Source File", "AuthRepository.kt", "file"),
                    EvidenceSummaryItem("Source File", "SessionManager.kt", "file")
                ),
                jiraResult = JiraResult(
                    ticketId = "AUTH-89",
                    summary = "Fix token refresh state race condition in AuthRepository",
                    status = "Created"
                ),
                slackResult = SlackResult(
                    channel = "#dev-alerts",
                    message = "Investigation complete for #398. Jira task AUTH-89 generated.",
                    notified = true
                ),
                evidenceDetails = EvidenceItem(
                    githubIssue = "#398 — Login Failure",
                    relatedPr = "#128 — Auth token refactoring",
                    relevantCommit = "a41f9 — Updated AuthRepository refresh logic",
                    retrievedFiles = listOf("AuthRepository.kt", "SessionManager.kt", "LoginViewModel.kt"),
                    aiReasoning = "The authentication failure occurs when token expiration coincides with an active session refresh request, leading to an invalid state wipe.",
                    timeline = listOf(
                        TimelineStep("Issue reported", "Users logged out unexpectedly during session refresh"),
                        TimelineStep("Auth code changed", "Refactored token rotation logic in commit a41f9"),
                        TimelineStep("Related PR merged", "PR #128 merged into main branch"),
                        TimelineStep("Bug triggered", "Concurrent refresh calls invalidated active bearer token"),
                        TimelineStep("Relevant code retrieved", "RAG engine retrieved AuthRepository & SessionManager"),
                        TimelineStep("Probable regression identified", "Missing mutex guard in AuthRepository.kt refresh()")
                    )
                )
            )
            "#374" -> InvestigationResult(
                issueNumber = "#374",
                probableRootCause = "Null payload response handling in PushNotificationService",
                confidenceScore = 84,
                confidenceNote = "Confidence represents the agent's assessment based on available repository evidence and should be reviewed by a developer.",
                evidenceSummary = listOf(
                    EvidenceSummaryItem("GitHub Issue", "#374 — Notification Crash", "github"),
                    EvidenceSummaryItem("Related PR", "#112 — Push service update", "pr"),
                    EvidenceSummaryItem("Commit", "f98c2 — Updated notification payload parser", "commit"),
                    EvidenceSummaryItem("Source File", "PushNotificationService.kt", "file"),
                    EvidenceSummaryItem("Source File", "NotificationPayload.kt", "file")
                ),
                jiraResult = JiraResult(
                    ticketId = "NOTIF-42",
                    summary = "Add null-check handling for silent push notification payloads",
                    status = "Created"
                ),
                slackResult = SlackResult(
                    channel = "#dev-alerts",
                    message = "Investigation complete for #374. Jira task NOTIF-42 generated.",
                    notified = true
                ),
                evidenceDetails = EvidenceItem(
                    githubIssue = "#374 — Notification Crash",
                    relatedPr = "#112 — Push service update",
                    relevantCommit = "f98c2 — Updated notification payload parser",
                    retrievedFiles = listOf("PushNotificationService.kt", "NotificationPayload.kt", "NotificationHandler.kt"),
                    aiReasoning = "Silent push notifications missing the 'data' payload key trigger an unchecked casting exception in PushNotificationService.",
                    timeline = listOf(
                        TimelineStep("Issue reported", "Crash reported when silent push is received in background"),
                        TimelineStep("Push code changed", "Updated JSON payload deserializer in commit f98c2"),
                        TimelineStep("Related PR merged", "PR #112 merged into main branch"),
                        TimelineStep("Crash reported", "NullPointerException triggered on empty data object"),
                        TimelineStep("Relevant code retrieved", "RAG engine retrieved PushNotificationService"),
                        TimelineStep("Probable regression identified", "Missing safe-cast on NotificationPayload.fromMap()")
                    )
                )
            )
            else -> InvestigationResult(
                issueNumber = cleanNumber,
                probableRootCause = "Payment response parsing regression",
                confidenceScore = 87,
                confidenceNote = "Confidence represents the agent's assessment based on available repository evidence and should be reviewed by a developer.",
                evidenceSummary = listOf(
                    EvidenceSummaryItem("GitHub Issue", "$cleanNumber — Payment crash", "github"),
                    EvidenceSummaryItem("Related PR", "#142 — Payment refactoring", "pr"),
                    EvidenceSummaryItem("Commit", "b72c4 — Updated payment response handling", "commit"),
                    EvidenceSummaryItem("Source File", "PaymentRepository.kt", "file"),
                    EvidenceSummaryItem("Source File", "PaymentResponse.kt", "file")
                ),
                jiraResult = JiraResult(
                    ticketId = "PAY-155",
                    summary = "Fix NullPointerException in PaymentViewModel",
                    status = "Created"
                ),
                slackResult = SlackResult(
                    channel = "#dev-alerts",
                    message = "Investigation complete for $cleanNumber. Jira task PAY-155 generated.",
                    notified = true
                ),
                evidenceDetails = EvidenceItem(
                    githubIssue = "$cleanNumber — Payment crash",
                    relatedPr = "#142 — Payment refactoring",
                    relevantCommit = "b72c4 — Updated payment response handling",
                    retrievedFiles = listOf("PaymentRepository.kt", "PaymentResponse.kt", "PaymentViewModel.kt"),
                    aiReasoning = "The reported crash appeared after changes to payment response handling. Repository evidence indicates that a response field may now reach the ViewModel as null.",
                    timeline = listOf(
                        TimelineStep("Issue reported", "Crash reported in production telemetry for payment flow"),
                        TimelineStep("Payment code changed", "Refactored payment parsing logic in commit b72c4"),
                        TimelineStep("Related PR merged", "PR #142 merged into main branch"),
                        TimelineStep("Crash reported", "NullPointerException triggered when handling empty payload"),
                        TimelineStep("Relevant code retrieved", "RAG engine retrieved PaymentRepository & PaymentResponse"),
                        TimelineStep("Probable regression identified", "Unchecked null cast identified in PaymentViewModel.kt line 42")
                    )
                )
            )
        }
    }

    private fun createDefaultInvestigation(issueNumber: String): Investigation {
        val cleanNumber = formatIssueNumber(issueNumber)
        return Investigation(
            id = cleanNumber.replace("#", ""),
            issueNumber = cleanNumber,
            title = "Issue $cleanNumber Investigation",
            description = "Probable root cause identified by CodeSherlock AI",
            status = InvestigationStatus.ROOT_CAUSE_FOUND,
            timeAgo = "Just now",
            repository = "company/android-app",
            instruction = "Investigate bug and find probable root cause",
            depth = InvestigationDepth.STANDARD,
            result = getMockResultForIssue(cleanNumber)
        )
    }

    private fun formatIssueNumber(input: String): String {
        val trimmed = input.trim()
        return if (trimmed.startsWith("#")) trimmed else "#$trimmed"
    }
}
