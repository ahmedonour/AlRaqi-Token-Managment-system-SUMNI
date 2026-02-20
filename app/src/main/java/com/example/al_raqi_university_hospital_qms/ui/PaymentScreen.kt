package com.example.al_raqi_university_hospital_qms.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.al_raqi_university_hospital_qms.R
import com.example.al_raqi_university_hospital_qms.data.Token
import com.example.al_raqi_university_hospital_qms.ui.components.SunmiPrinterHelper
import com.example.al_raqi_university_hospital_qms.ui.theme.Primary
import com.example.al_raqi_university_hospital_qms.ui.theme.Secondary
import com.example.al_raqi_university_hospital_qms.viewmodel.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PaymentScreen(
    sectionId: String?,
    onNavigateBack: () -> Unit,
    onTokenGenerated: (Int, String) -> Unit,
    viewModel: MainViewModel = viewModel()
) {
    val sections by viewModel.sections.collectAsState()
    val section = sections.find { it.id == sectionId?.toIntOrNull() }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val sunmiPrinterHelper = remember { SunmiPrinterHelper() }
    var isPrinterReady by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        sunmiPrinterHelper.initPrinter(context) { ready ->
            isPrinterReady = ready
        }
        onDispose {
            // Delay deinit slightly to ensure last print job is sent
            sunmiPrinterHelper.deinitPrinter(context)
        }
    }

    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(Primary, Secondary)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBrush),
        contentAlignment = Alignment.Center
    ) {
        if (section != null) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = section.name,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${section.price} ${stringResource(id = R.string.currency)}",
                        style = MaterialTheme.typography.displaySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    
                    if (!isPrinterReady) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                        Text("Connecting to printer...", style = MaterialTheme.typography.bodySmall)
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                val tokenNumber = viewModel.incrementTokenCounter()
                                viewModel.addToQueue(section.id, Token(tokenNumber))
                                
                                // Print receipt
                                sunmiPrinterHelper.printToken(
                                    "AL-RAQI UNIVERSITY HOSPITAL\n" +
                                    "--------------------------------\n" +
                                    "SERVICE: ${section.name}\n" +
                                    "TOKEN NUMBER: $tokenNumber\n" +
                                    "PRICE: ${section.price} SDG\n" +
                                    "--------------------------------\n" +
                                    "Please wait for your turn.\n" +
                                    "Thank you!"
                                )
                                
                                // Small delay to ensure printer starts before we navigate and dispose
                                delay(500)
                                onTokenGenerated(tokenNumber, section.name)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium,
                        enabled = isPrinterReady
                    ) {
                        Text("Pay & Print Token", fontSize = 18.sp, modifier = Modifier.padding(8.dp))
                    }
                    
                    OutlinedButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Cancel")
                    }
                }
            }
        } else {
            Text("Section not found", color = Color.White)
        }
    }
}
