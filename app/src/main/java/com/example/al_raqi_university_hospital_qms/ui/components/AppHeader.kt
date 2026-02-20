import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.al_raqi_university_hospital_qms.R
import com.example.al_raqi_university_hospital_qms.ui.theme.ALRaqiUniversityHospitalQMSTheme
import com.example.al_raqi_university_hospital_qms.ui.theme.TextLight
import com.example.al_raqi_university_hospital_qms.ui.components.LanguageSwitcher

@Composable
fun AppHeader(
    onAdminClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White, MaterialTheme.shapes.medium) // Apply white background
            .shadow(elevation = 8.dp, shape = MaterialTheme.shapes.medium) // Apply shadow, using 8dp as approximation for shadow-lg
            .padding(16.dp), // Adjust padding
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Logo and Title
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = "🏥", style = MaterialTheme.typography.headlineLarge)
            Column {
                Text(
                    text = stringResource(id = R.string.hospital_title),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(id = R.string.hospital_token_management_system),
                    style = MaterialTheme.typography.bodySmall,
                    color = TextLight // Use custom TextLight color
                )
            }
        }

        // Header Actions
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Language Switcher
            LanguageSwitcher()

            // Admin Button
            Button(
                onClick = onAdminClick,
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                )
            ) {
                Text(text = "⚙️", modifier = Modifier.padding(end = 4.dp))
                Text(stringResource(id = R.string.dashboard_heading))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppHeaderPreview() {
    ALRaqiUniversityHospitalQMSTheme {
        AppHeader(onAdminClick = {})
    }
}