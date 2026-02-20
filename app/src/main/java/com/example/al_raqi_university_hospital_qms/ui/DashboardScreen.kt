package com.example.al_raqi_university_hospital_qms.ui

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.al_raqi_university_hospital_qms.R
import com.example.al_raqi_university_hospital_qms.data.Section
import com.example.al_raqi_university_hospital_qms.ui.theme.Primary
import com.example.al_raqi_university_hospital_qms.ui.theme.Secondary
import com.example.al_raqi_university_hospital_qms.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onNavigateBack: () -> Unit,
    onNavigateToClinicTV: () -> Unit,
    onNavigateToReceptionTV: () -> Unit,
    viewModel: MainViewModel = viewModel()
) {
    val sections by viewModel.sections.collectAsState()
    val tokenCounter by viewModel.tokenCounter.collectAsState()
    val context = LocalContext.current

    var showAddDialog by remember { mutableStateOf(false) }
    var sectionToEdit by remember { mutableStateOf<Section?>(null) }

    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(Primary, Secondary)
    )

    val importLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            context.contentResolver.openInputStream(it)?.use { input ->
                viewModel.importSectionsFromExcel(input)
            }
        }
    }

    val templateLauncher = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) { uri: Uri? ->
        uri?.let {
            context.contentResolver.openOutputStream(it)?.use { output ->
                viewModel.generateExcelTemplate(output)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Admin Dashboard") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { importLauncher.launch("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") }) {
                        Icon(Icons.Default.UploadFile, contentDescription = "Import Excel")
                    }
                    IconButton(onClick = { templateLauncher.launch("sections_template.xlsx") }) {
                        Icon(Icons.Default.Download, contentDescription = "Download Template")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Section")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(backgroundBrush)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Stats Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                StatCard(
                    title = "Total Tokens",
                    value = tokenCounter.toString(),
                    icon = Icons.Default.ConfirmationNumber,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Sections",
                    value = sections.size.toString(),
                    icon = Icons.Default.Category,
                    modifier = Modifier.weight(1f)
                )
            }

            Text(
                "Section Management",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(top = 8.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(sections) { section ->
                    SectionAdminCard(
                        section = section,
                        onEdit = { sectionToEdit = section },
                        onDelete = { viewModel.removeSection(section.id) },
                        onNext = {
                            if (section.queue.isNotEmpty()) {
                                viewModel.removeFromQueue(section.id, section.queue.first().number)
                            }
                        }
                    )
                }
            }

            // TV Displays Section
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("TV Displays", style = MaterialTheme.typography.titleMedium)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(onClick = onNavigateToClinicTV) {
                            Icon(Icons.Default.Tv, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("Clinic TV")
                        }
                        Button(onClick = onNavigateToReceptionTV) {
                            Icon(Icons.Default.Tv, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("Reception TV")
                        }
                    }
                }
            }

            // Reports Section
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Download Reports", style = MaterialTheme.typography.titleMedium)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        ReportButton("24h", 24, viewModel, context)
                        ReportButton("7d", 24 * 7, viewModel, context)
                        ReportButton("1m", 24 * 30, viewModel, context)
                        ReportButton("3m", 24 * 90, viewModel, context)
                    }
                }
            }

            Button(
                onClick = { viewModel.resetTokenCounter() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Reset Token Counter")
            }
        }
    }

    if (showAddDialog) {
        SectionDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { name, type, price, doctors ->
                viewModel.addSection(Section(0, name, type, price, emptyList(), doctors))
                showAddDialog = false
            }
        )
    }

    sectionToEdit?.let { section ->
        SectionDialog(
            section = section,
            onDismiss = { sectionToEdit = null },
            onConfirm = { name, type, price, doctors ->
                viewModel.updateSection(section.id) {
                    it.copy(name = name, type = type, price = price, doctors = doctors)
                }
                sectionToEdit = null
            }
        )
    }
}

@Composable
fun ReportButton(label: String, hours: Int, viewModel: MainViewModel, context: Context) {
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) { uri: Uri? ->
        uri?.let {
            context.contentResolver.openOutputStream(it)?.use { output ->
                viewModel.exportHistoryToExcel(output, hours)
            }
        }
    }
    TextButton(onClick = { launcher.launch("report_$label.xlsx") }) {
        Text(label)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SectionDialog(
    section: Section? = null,
    onDismiss: () -> Unit,
    onConfirm: (String, String, Int, List<String>) -> Unit
) {
    var name by remember { mutableStateOf(section?.name ?: "") }
    var type by remember { mutableStateOf(section?.type ?: "Clinic") }
    var price by remember { mutableStateOf(section?.price?.toString() ?: "") }
    var doctors by remember { mutableStateOf(section?.doctors?.joinToString(", ") ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (section == null) "Add Section" else "Edit Section") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                TextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = type == "Clinic", onClick = { type = "Clinic" })
                    Text("Clinic")
                    Spacer(Modifier.width(8.dp))
                    RadioButton(selected = type == "Laboratory", onClick = { type = "Laboratory" })
                    Text("Laboratory")
                }
                TextField(value = price, onValueChange = { price = it }, label = { Text("Price") })
                TextField(value = doctors, onValueChange = { doctors = it }, label = { Text("Doctors (comma separated)") })
            }
        },
        confirmButton = {
            Button(onClick = { 
                onConfirm(name, type, price.toIntOrNull() ?: 0, doctors.split(",").map { it.trim() }.filter { it.isNotEmpty() })
            }) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun SectionAdminCard(
    section: Section,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onNext: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(section.name, style = MaterialTheme.typography.titleMedium)
                Text("Type: ${section.type} | Price: ${section.price} SDG", style = MaterialTheme.typography.bodySmall)
                Text("In Queue: ${section.queue.size}", style = MaterialTheme.typography.bodySmall)
                if (section.doctors.isNotEmpty()) {
                    Text("Doctors: ${section.doctors.joinToString(", ")}", style = MaterialTheme.typography.bodySmall)
                }
            }
            Row {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = MaterialTheme.colorScheme.primary)
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                }
                IconButton(onClick = onNext) {
                    Icon(Icons.Default.SkipNext, contentDescription = "Next Patient", tint = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}

@Composable
fun StatCard(title: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Text(title, style = MaterialTheme.typography.bodySmall)
            Text(value, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        }
    }
}
