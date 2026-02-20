package com.example.al_raqi_university_hospital_qms.data.repository

import android.content.Context
import android.content.SharedPreferences
import android.util.Log // Added Log import
import com.example.al_raqi_university_hospital_qms.data.Section
import com.example.al_raqi_university_hospital_qms.data.Token
import com.example.al_raqi_university_hospital_qms.data.SectionType
import com.example.al_raqi_university_hospital_qms.data.HistoryItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.concurrent.atomic.AtomicInteger

class SectionRepository(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("hospital_qms_prefs", Context.MODE_PRIVATE)
    private val json = Json { ignoreUnknownKeys = true }

    // Default sections, mirroring the Svelte app
    private val defaultSections = listOf(
        Section(id = 1, name = "General Medicine", type = "Clinic", price = 100, queue = emptyList(), doctors = emptyList()),
        Section(id = 2, name = "Pediatrics", type = "Clinic", price = 120, queue = emptyList(), doctors = emptyList()),
        Section(id = 3, name = "Cardiology", type = "Clinic", price = 150, queue = emptyList(), doctors = emptyList()),
        Section(id = 4, name = "Orthopedics", type = "Clinic", price = 150, queue = emptyList(), doctors = emptyList()),
        Section(id = 5, name = "Dermatology", type = "Clinic", price = 130, queue = emptyList(), doctors = emptyList()),
        Section(id = 6, name = "Blood Test", type = "Laboratory", price = 80, queue = emptyList(), doctors = emptyList()),
        Section(id = 7, name = "X-Ray", type = "Laboratory", price = 200, queue = emptyList(), doctors = emptyList()),
        Section(id = 8, name = "Ultrasound", type = "Laboratory", price = 250, queue = emptyList(), doctors = emptyList()),
        Section(id = 9, name = "CT Scan", type = "Laboratory", price = 500, queue = emptyList(), doctors = emptyList()),
        Section(id = 10, name = "MRI", type = "Laboratory", price = 800, queue = emptyList(), doctors = emptyList())
    )

    private val _sections = MutableStateFlow<List<Section>>(emptyList())
    val sections = _sections.asStateFlow()

    private val _tokenCounter = MutableStateFlow(0)
    val tokenCounter = _tokenCounter.asStateFlow()

    private val _history = MutableStateFlow<List<HistoryItem>>(emptyList())
    val history = _history.asStateFlow()

    init {
        loadSections()
        loadTokenCounter()
        loadHistory()
    }

    private fun loadHistory() {
        val storedHistoryJson = sharedPreferences.getString("hospital_history", null)
        if (storedHistoryJson != null) {
            try {
                _history.value = json.decodeFromString<List<HistoryItem>>(storedHistoryJson)
            } catch (e: Exception) {
                Log.e("SectionRepository", "Error parsing history: ${e.message}")
            }
        }
    }

    private fun saveHistory() {
        try {
            val historyJson = json.encodeToString(_history.value)
            sharedPreferences.edit().putString("hospital_history", historyJson).apply()
        } catch (e: Exception) {
            Log.e("SectionRepository", "Error saving history: ${e.message}")
        }
    }

    private fun loadSections() {
        val storedSectionsJson = sharedPreferences.getString("hospital_sections", null)
        if (storedSectionsJson != null) {
            try {
                _sections.value = json.decodeFromString<List<Section>>(storedSectionsJson)
                Log.d("SectionRepository", "Sections loaded successfully.")
            } catch (e: Exception) {
                Log.e("SectionRepository", "Error parsing stored sections, resetting to default: ${e.message}")
                _sections.value = defaultSections
                saveSections()
            }
        } else {
            Log.d("SectionRepository", "No stored sections found, using default.")
            _sections.value = defaultSections
            saveSections()
        }
    }

    private fun saveSections() {
        try {
            val sectionsJson = json.encodeToString(_sections.value)
            sharedPreferences.edit().putString("hospital_sections", sectionsJson).apply()
            Log.d("SectionRepository", "Sections saved successfully.")
        } catch (e: Exception) {
            Log.e("SectionRepository", "Error saving sections: ${e.message}")
        }
    }

    private fun loadTokenCounter() {
        _tokenCounter.value = sharedPreferences.getInt("token_counter", 0)
        Log.d("SectionRepository", "Token counter loaded: ${_tokenCounter.value}")
    }

    private fun saveTokenCounter() {
        try {
            sharedPreferences.edit().putInt("token_counter", _tokenCounter.value).apply()
            Log.d("SectionRepository", "Token counter saved: ${_tokenCounter.value}")
        } catch (e: Exception) {
            Log.e("SectionRepository", "Error saving token counter: ${e.message}")
        }
    }

    fun addSection(section: Section) {
        val currentSections = _sections.value.toMutableList()
        val newId = (currentSections.maxOfOrNull { it.id } ?: 0) + 1
        val newSection = section.copy(id = newId, queue = emptyList())
        currentSections.add(newSection)
        _sections.value = currentSections
        saveSections()
    }

    fun removeSection(id: Int) {
        _sections.value = _sections.value.filter { it.id != id }
        saveSections()
    }

    fun updateSection(id: Int, changes: (Section) -> Section) {
        _sections.value = _sections.value.map {
            if (it.id == id) {
                changes(it)
            } else {
                it
            }
        }
        saveSections()
    }

    fun addToQueue(sectionId: Int, token: Token) {
        _sections.value = _sections.value.map { section ->
            if (section.id == sectionId) {
                // Track history when added to queue
                val historyItem = HistoryItem(
                    sectionName = section.name,
                    tokenNumber = token.number,
                    timestamp = token.timestamp,
                    price = section.price
                )
                _history.value = _history.value + historyItem
                saveHistory()
                
                section.copy(queue = section.queue + token)
            } else {
                section
            }
        }
        saveSections()
    }

    fun importSections(newSections: List<Section>) {
        _sections.value = newSections
        saveSections()
    }

    fun removeFromQueue(sectionId: Int, tokenNumber: Int) {
        _sections.value = _sections.value.map { section ->
            if (section.id == sectionId) {
                section.copy(queue = section.queue.filter { it.number != tokenNumber })
            } else {
                section
            }
        }
        saveSections()
    }

    fun resetSections() {
        _sections.value = defaultSections
        saveSections()
    }

    fun incrementTokenCounter(): Int {
        _tokenCounter.value += 1
        saveTokenCounter()
        return _tokenCounter.value
    }

    fun resetTokenCounter() {
        _tokenCounter.value = 0
        saveTokenCounter()
    }
}