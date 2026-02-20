package com.example.al_raqi_university_hospital_qms.ui.components

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Print
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun ThermalPrinterExample(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val sunmiPrinterHelper = remember { SunmiPrinterHelper() }
    
    var isPrinterConnected by remember { mutableStateOf(false) }
    var printStatus by remember { mutableStateOf("Disconnected") }

    DisposableEffect(Unit) {
        sunmiPrinterHelper.initPrinter(context) { connected ->
            isPrinterConnected = connected
            printStatus = if (connected) "Sunmi Inner Printer Ready" else "Disconnected"
        }
        onDispose {
            sunmiPrinterHelper.deinitPrinter(context)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Printer Selection", style = MaterialTheme.typography.headlineMedium)
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = if (isPrinterConnected) 
                    MaterialTheme.colorScheme.primaryContainer 
                else 
                    MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Print, contentDescription = null)
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text("Sunmi Inner Printer", style = MaterialTheme.typography.titleMedium)
                        Text(printStatus, style = MaterialTheme.typography.bodySmall)
                    }
                }
                
                if (isPrinterConnected) {
                    Button(onClick = {
                        coroutineScope.launch {
                            sunmiPrinterHelper.printToken(
                                "AL-RAQI HOSPITAL\n" +
                                "--------------------------------\n" +
                                "Test Print Success!\n" +
                                "Sunmi Inner Printer Detected\n" +
                                "--------------------------------\n"
                            )
                        }
                    }) {
                        Text("Test Print")
                    }
                }
            }
        }

        if (!isPrinterConnected) {
            Text(
                "No printers detected. If you are using a Sunmi device, ensure the printer service is running.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error
            )
            Button(
                onClick = { sunmiPrinterHelper.initPrinter(context) { isPrinterConnected = it } },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Retry Connection")
            }
        }
    }
}
