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
import com.example.al_raqi_university_hospital_qms.ui.theme.Primary
import com.example.al_raqi_university_hospital_qms.viewmodel.MainViewModel

@Composable
fun ClinicTVDisplay(viewModel: MainViewModel = viewModel()) {
    val sections by viewModel.sections.collectAsState()
    val clinics = sections.filter { it.type == "Clinic" }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(24.dp)
    ) {
        Text(
            "CLINIC QUEUE STATUS",
            style = MaterialTheme.typography.displayLarge,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            items(clinics) { clinic ->
                TVSectionCard(section = clinic)
            }
        }
    }
}

@Composable
fun TVSectionCard(section: Section) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Primary),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                section.name,
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            val currentToken = section.queue.firstOrNull()?.number?.toString() ?: "---"
            Text(
                "NOW SERVING",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White.copy(alpha = 0.7f)
            )
            Text(
                currentToken,
                style = MaterialTheme.typography.displayLarge.copy(fontSize = 120.sp),
                color = Color.Yellow,
                fontWeight = FontWeight.Black
            )
            if (section.doctors.isNotEmpty()) {
                Text(
                    "Doctor: ${section.doctors.joinToString(", ")}",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White
                )
            }
        }
    }
}
