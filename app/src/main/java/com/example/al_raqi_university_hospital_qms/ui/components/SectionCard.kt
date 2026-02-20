package com.example.al_raqi_university_hospital_qms.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.example.al_raqi_university_hospital_qms.data.Section
import com.example.al_raqi_university_hospital_qms.data.SectionType
import com.example.al_raqi_university_hospital_qms.ui.theme.ALRaqiUniversityHospitalQMSTheme
import com.example.al_raqi_university_hospital_qms.ui.theme.BgLight
import com.example.al_raqi_university_hospital_qms.ui.theme.Border
import com.example.al_raqi_university_hospital_qms.ui.theme.Primary
import com.example.al_raqi_university_hospital_qms.ui.theme.TextDark
import com.example.al_raqi_university_hospital_qms.ui.theme.TextLight

@Composable
fun SectionCard(
    section: Section,
    modifier: Modifier = Modifier,
    onClick: (Section) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min) // Ensure card height adjusts to content
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(16.dp)) // Apply shadow
            .border(2.dp, Border, RoundedCornerShape(16.dp)) // Apply border
            .clickable { onClick(section) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White) // Use white background
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            val icon = if (section.type == "Clinic") "🩺" else "🔬"
            Text(
                text = icon,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = section.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextDark, // Use custom TextDark
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "${section.price} ${stringResource(id = R.string.currency)}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = Primary, // Use custom Primary
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(BgLight) // Use custom BgLight
                    .padding(vertical = 8.dp, horizontal = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = R.string.queue_in_queue, section.queue.size),
                    style = MaterialTheme.typography.bodySmall,
                    color = TextLight, // Use custom TextLight
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SectionCardPreview() {
    ALRaqiUniversityHospitalQMSTheme {
        SectionCard(
            section = Section(
                id = 1,
                name = "General Medicine",
                type = SectionType.Clinic.name,
                price = 100,
                queue = listOf(
                    com.example.al_raqi_university_hospital_qms.data.Token(1),
                    com.example.al_raqi_university_hospital_qms.data.Token(2)
                )
            ),
            onClick = {}
        )
    }
}
