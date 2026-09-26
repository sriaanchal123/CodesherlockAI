package com.example.codesherlockai.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.codesherlockai.data.repository.InvestigationRepository
import com.example.codesherlockai.domain.model.AgentStep
import com.example.codesherlockai.domain.model.AgentStepStatus
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AgentExecutionViewModel(
    private val repository: InvestigationRepository = InvestigationRepository()
) : ViewModel() {

    private val _steps = MutableStateFlow<List<AgentStep>>(emptyList())
    val steps: StateFlow<List<AgentStep>> = _steps.asStateFlow()

    private val _activityLogs = MutableStateFlow<List<String>>(emptyList())
    val activityLogs: StateFlow<List<String>> = _activityLogs.asStateFlow()

    private val _isFinished = MutableStateFlow(false)
    val isFinished: StateFlow<Boolean> = _isFinished.asStateFlow()

    private val _issueNumber = MutableStateFlow("#421")
    val issueNumber: StateFlow<String> = _issueNumber.asStateFlow()

    private var executionJob: Job? = null

    fun startExecution(issueNumber: String = "#421") {
        val cleanNumber = if (issueNumber.startsWith("#")) issueNumber else "#$issueNumber"
        if (_issueNumber.value == cleanNumber && _steps.value.isNotEmpty() && !(_isFinished.value.not() && executionJob?.isActive == false)) {
            if (executionJob?.isActive == true) return
        }

        executionJob?.cancel()
        _issueNumber.value = cleanNumber
        val initialSteps = repository.getMockAgentSteps().map { it.copy(status = AgentStepStatus.PENDING) }
        _steps.value = initialSteps
        _activityLogs.value = listOf("Initializing CodeSherlock AI autonomous agent...")
        _isFinished.value = false

        executionJob = viewModelScope.launch {
            val currentSteps = initialSteps.toMutableList()

            for (i in currentSteps.indices) {
                // Set step to RUNNING
                currentSteps[i] = currentSteps[i].copy(status = AgentStepStatus.RUNNING)
                _steps.value = currentSteps.toList()

                // Add logs to activity panel
                val stepLogs = currentSteps[i].logs
                for (log in stepLogs) {
                    _activityLogs.value = listOf(log) + _activityLogs.value.take(15)
                    delay(300)
                }

                delay(400)

                // Set step to COMPLETED
                currentSteps[i] = currentSteps[i].copy(status = AgentStepStatus.COMPLETED)
                _steps.value = currentSteps.toList()
            }

            _isFinished.value = true
            _activityLogs.value = listOf("Investigation completed successfully!") + _activityLogs.value
        }
    }
}
