package com.example.al_raqi_university_hospital_qms.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.al_raqi_university_hospital_qms.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HospitalHeader() {
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Note: The file 'icon.png' must be moved to 'app/src/main/res/drawable/'
                Image(
                    painter = painterResource(id = R.drawable.icon),
                    contentDescription = "Hospital Icon",
                    modifier = Modifier.size(40.dp)
                )
                Text(
                    text = stringResource(id = R.string.hospital_title),
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    )
}
