package com.example.codesherlockai.viewmodel

import androidx.lifecycle.ViewModel
import com.example.codesherlockai.data.repository.InvestigationRepository
import com.example.codesherlockai.domain.model.InvestigationResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class EvidenceViewModel(
    private val repository: InvestigationRepository = InvestigationRepository()
) : ViewModel() {

    private val _result = MutableStateFlow<InvestigationResult?>(repository.getMockResultForIssue("#421"))
    val result: StateFlow<InvestigationResult?> = _result.asStateFlow()

    fun loadResult(issueNumber: String = "#421") {
        val cleanNumber = if (issueNumber.startsWith("#")) issueNumber else "#$issueNumber"
        _result.value = repository.getMockResultForIssue(cleanNumber)
    }
}
