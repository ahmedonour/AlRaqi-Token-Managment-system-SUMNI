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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.al_raqi_university_hospital_qms.R
import com.example.al_raqi_university_hospital_qms.data.SectionType
import com.example.al_raqi_university_hospital_qms.ui.components.FilterTabs
import com.example.al_raqi_university_hospital_qms.ui.components.SectionCard
import com.example.al_raqi_university_hospital_qms.ui.theme.Primary
import com.example.al_raqi_university_hospital_qms.ui.theme.Secondary
import com.example.al_raqi_university_hospital_qms.viewmodel.MainViewModel

@Composable
fun MainScreen(
    viewModel: MainViewModel = viewModel(),
    onNavigateToDashboard: () -> Unit,
    onNavigateToPayment: (Int) -> Unit
) {
    val filteredSections by viewModel.filteredSections.collectAsState()
    val selectedFilter by viewModel.filter.collectAsState()

    val backgroundBrush = Brush.linearGradient(
        colors = listOf(Primary, Secondary),
        start = androidx.compose.ui.geometry.Offset(0f, Float.POSITIVE_INFINITY),
        end = androidx.compose.ui.geometry.Offset(Float.POSITIVE_INFINITY, 0f)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBrush)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FilterTabs(
            selectedFilter = selectedFilter,
            onFilterSelected = { viewModel.setFilter(it) },
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (filteredSections.isEmpty()) {
            EmptyState()
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 180.dp),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(filteredSections, key = { it.id }) { section ->
                    SectionCard(section = section) {
                        onNavigateToPayment(section.id)
                    }
                }
            }
        }
        
        Button(
            onClick = onNavigateToDashboard,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(stringResource(id = R.string.dashboard_heading))
        }
    }
}

@Composable
fun EmptyState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.empty_state_no_sections),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}
