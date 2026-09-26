package com.example.codesherlockai.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.codesherlockai.data.remote.InvestigationResponse
import com.example.codesherlockai.data.repository.InvestigationRepository
import com.example.codesherlockai.domain.model.InvestigationDepth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface InvestigationUiState {
    object Idle : InvestigationUiState
    object Loading : InvestigationUiState
    data class Success(val response: InvestigationResponse) : InvestigationUiState
    data class Error(val message: String) : InvestigationUiState
}

class InvestigationViewModel(
    private val repository: InvestigationRepository = InvestigationRepository()
) : ViewModel() {

    private val _githubIssue = MutableStateFlow("#421")
    val githubIssue: StateFlow<String> = _githubIssue.asStateFlow()

    private val _repository = MutableStateFlow("company/android-app")
    val repositoryName: StateFlow<String> = _repository.asStateFlow()

    private val _instruction = MutableStateFlow("Find the probable root cause of this issue.")
    val instruction: StateFlow<String> = _instruction.asStateFlow()

    private val _selectedDepth = MutableStateFlow(InvestigationDepth.STANDARD)
    val selectedDepth: StateFlow<InvestigationDepth> = _selectedDepth.asStateFlow()

    private val _issueError = MutableStateFlow<String?>(null)
    val issueError: StateFlow<String?> = _issueError.asStateFlow()

    private val _repoError = MutableStateFlow<String?>(null)
    val repoError: StateFlow<String?> = _repoError.asStateFlow()

    private val _uiState = MutableStateFlow<InvestigationUiState>(InvestigationUiState.Idle)
    val uiState: StateFlow<InvestigationUiState> = _uiState.asStateFlow()

    fun onIssueChanged(value: String) {
        _githubIssue.value = value
        if (value.isNotBlank()) _issueError.value = null
        if (_uiState.value is InvestigationUiState.Error) {
            _uiState.value = InvestigationUiState.Idle
        }
    }

    fun onRepoChanged(value: String) {
        _repository.value = value
        if (value.isNotBlank()) _repoError.value = null
        if (_uiState.value is InvestigationUiState.Error) {
            _uiState.value = InvestigationUiState.Idle
        }
    }

    fun onInstructionChanged(value: String) {
        _instruction.value = value
        if (_uiState.value is InvestigationUiState.Error) {
            _uiState.value = InvestigationUiState.Idle
        }
    }

    fun onDepthSelected(depth: InvestigationDepth) {
        _selectedDepth.value = depth
    }

    fun clearError() {
        _uiState.value = InvestigationUiState.Idle
    }

    fun submitInvestigation(onSuccess: (String) -> Unit) {
        var isValid = true
        if (_githubIssue.value.isBlank()) {
            _issueError.value = "GitHub Issue is required"
            isValid = false
        }
        if (_repository.value.isBlank()) {
            _repoError.value = "Repository is required"
            isValid = false
        }
        if (!isValid) return

        val issueNumberInt = _githubIssue.value.replace("#", "").trim().toIntOrNull() ?: 421
        val repoStr = _repository.value.trim()
        val instructionStr = _instruction.value.trim()
        val depthStr = _selectedDepth.value.displayName.lowercase()

        _uiState.value = InvestigationUiState.Loading

        viewModelScope.launch {
            val result = repository.investigateBackend(
                issueNumber = issueNumberInt,
                repository = repoStr,
                instruction = instructionStr,
                depth = depthStr
            )

            result.onSuccess { response ->
                repository.addInvestigation(
                    issueNumber = _githubIssue.value,
                    repository = repoStr,
                    instruction = instructionStr,
                    depth = _selectedDepth.value
                )
                _uiState.value = InvestigationUiState.Success(response)
                onSuccess(_githubIssue.value)
            }.onFailure { error ->
                _uiState.value = InvestigationUiState.Error(
                    error.message ?: "Could not connect to FastAPI server."
                )
            }
        }
    }
}
