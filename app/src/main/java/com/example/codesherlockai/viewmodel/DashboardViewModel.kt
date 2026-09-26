package com.example.codesherlockai.viewmodel

import androidx.lifecycle.ViewModel
import com.example.codesherlockai.data.repository.InvestigationRepository
import com.example.codesherlockai.domain.model.DashboardMetrics
import com.example.codesherlockai.domain.model.Investigation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DashboardViewModel(
    private val repository: InvestigationRepository = InvestigationRepository()
) : ViewModel() {

    val investigations: StateFlow<List<Investigation>> = repository.investigations
    val metrics: StateFlow<DashboardMetrics> = repository.metrics

    private val _selectedTab = MutableStateFlow(0) // 0: Home, 1: Investigations, 2: Activity, 3: Settings
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    fun selectTab(index: Int) {
        _selectedTab.value = index
    }
}
