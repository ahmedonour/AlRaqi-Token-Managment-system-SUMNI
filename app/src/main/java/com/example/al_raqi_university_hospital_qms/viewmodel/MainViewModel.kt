package com.example.al_raqi_university_hospital_qms.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.al_raqi_university_hospital_qms.data.Section
import com.example.al_raqi_university_hospital_qms.data.SectionType
import com.example.al_raqi_university_hospital_qms.data.Token
import com.example.al_raqi_university_hospital_qms.data.HistoryItem
import com.example.al_raqi_university_hospital_qms.data.repository.SectionRepository
import com.example.al_raqi_university_hospital_qms.utils.ExcelUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = SectionRepository(application)

    private val _filter = MutableStateFlow(SectionType.All)
    val filter: StateFlow<SectionType> = _filter

    val sections: StateFlow<List<Section>> = repository.sections
    val history: StateFlow<List<HistoryItem>> = repository.history

    val filteredSections: StateFlow<List<Section>> = combine(
        repository.sections,
        _filter
    ) { allSections, currentFilter ->
        when (currentFilter) {
            SectionType.All -> allSections
            SectionType.Clinic -> allSections.filter { it.type == "Clinic" }
            SectionType.Laboratory -> allSections.filter { it.type == "Laboratory" }
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val tokenCounter: StateFlow<Int> = repository.tokenCounter

    fun setFilter(newFilter: SectionType) {
        _filter.value = newFilter
    }

    fun addSection(section: Section) {
        viewModelScope.launch {
            repository.addSection(section)
        }
    }

    fun removeSection(id: Int) {
        viewModelScope.launch {
            repository.removeSection(id)
        }
    }

    fun updateSection(id: Int, changes: (Section) -> Section) {
        viewModelScope.launch {
            repository.updateSection(id, changes)
        }
    }

    fun addToQueue(sectionId: Int, token: Token) {
        viewModelScope.launch {
            repository.addToQueue(sectionId, token)
        }
    }

    fun removeFromQueue(sectionId: Int, tokenNumber: Int) {
        viewModelScope.launch {
            repository.removeFromQueue(sectionId, tokenNumber)
        }
    }

    fun resetSections() {
        viewModelScope.launch {
            repository.resetSections()
        }
    }

    fun incrementTokenCounter(): Int {
        var newTokenCount = 0
        viewModelScope.launch {
            newTokenCount = repository.incrementTokenCounter()
        }
        return newTokenCount
    }

    fun resetTokenCounter() {
        viewModelScope.launch {
            repository.resetTokenCounter()
        }
    }

    fun importSectionsFromExcel(inputStream: java.io.InputStream) {
        viewModelScope.launch {
            val newSections = ExcelUtils.importSections(inputStream)
            repository.importSections(newSections)
        }
    }

    fun generateExcelTemplate(outputStream: java.io.OutputStream) {
        ExcelUtils.generateTemplate(outputStream)
    }

    fun exportHistoryToExcel(outputStream: java.io.OutputStream, hours: Int) {
        val now = System.currentTimeMillis()
        val cutoff = now - (hours * 3600 * 1000L)
        val filteredHistory = history.value.filter { it.timestamp >= cutoff }
        ExcelUtils.exportHistory(filteredHistory, outputStream, "${hours}h")
    }
}