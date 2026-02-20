package com.example.al_raqi_university_hospital_qms.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.al_raqi_university_hospital_qms.R
import com.example.al_raqi_university_hospital_qms.data.SectionType
import com.example.al_raqi_university_hospital_qms.ui.theme.ALRaqiUniversityHospitalQMSTheme
import com.example.al_raqi_university_hospital_qms.ui.theme.BgLight
import com.example.al_raqi_university_hospital_qms.ui.theme.TextDark
import com.example.al_raqi_university_hospital_qms.ui.theme.TextLight

@Composable
fun FilterTabs(
    selectedFilter: SectionType,
    onFilterSelected: (SectionType) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 16.dp) // Adjusted padding
            .background(Color.White, RoundedCornerShape(12.dp))
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(12.dp)) // Apply shadow, using 4dp as approximation for shadow
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TabButton(
            text = stringResource(id = R.string.filters_all_services),
            isSelected = selectedFilter == SectionType.All,
            onClick = { onFilterSelected(SectionType.All) },
            modifier = Modifier.weight(1f)
        )
        TabButton(
            text = stringResource(id = R.string.filters_clinics),
            isSelected = selectedFilter == SectionType.Clinic,
            onClick = { onFilterSelected(SectionType.Clinic) },
            modifier = Modifier.weight(1f)
        )
        TabButton(
            text = stringResource(id = R.string.filters_laboratories),
            isSelected = selectedFilter == SectionType.Laboratory,
            onClick = { onFilterSelected(SectionType.Laboratory) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun TabButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isSelected) MaterialTheme.colorScheme.primary else BgLight // Use custom BgLight
    val textColor = if (isSelected) Color.White else TextLight // Use custom TextLight

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FilterTabsPreview() {
    ALRaqiUniversityHospitalQMSTheme {
        FilterTabs(selectedFilter = SectionType.All, onFilterSelected = {})
    }
}