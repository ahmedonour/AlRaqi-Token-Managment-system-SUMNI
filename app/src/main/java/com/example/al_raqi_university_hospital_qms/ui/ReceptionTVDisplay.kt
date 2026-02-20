package com.example.al_raqi_university_hospital_qms.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.al_raqi_university_hospital_qms.data.Section
import com.example.al_raqi_university_hospital_qms.ui.theme.Secondary
import com.example.al_raqi_university_hospital_qms.viewmodel.MainViewModel

@Composable
fun ReceptionTVDisplay(viewModel: MainViewModel = viewModel()) {
    val sections by viewModel.sections.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "HOSPITAL QUEUE OVERVIEW",
                style = MaterialTheme.typography.displayMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            // Digital Clock could go here
        }

        Spacer(modifier = Modifier.height(24.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(sections) { section ->
                ReceptionTVCard(section = section)
            }
        }
    }
}

@Composable
fun ReceptionTVCard(section: Section) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (section.type == "Clinic") Color(0xFF1E3A8A) else Color(0xFF14532D)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                section.name,
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
            Divider(modifier = Modifier.padding(vertical = 8.dp), color = Color.White.copy(alpha = 0.2f))
            val currentToken = section.queue.firstOrNull()?.number?.toString() ?: "---"
            Text(
                currentToken,
                style = MaterialTheme.typography.displayMedium,
                color = Color.Yellow,
                fontWeight = FontWeight.Bold
            )
            Text(
                "In Queue: ${section.queue.size}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}
