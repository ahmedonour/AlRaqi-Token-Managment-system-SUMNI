package com.example.al_raqi_university_hospital_qms

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.al_raqi_university_hospital_qms.ui.DashboardScreen
import com.example.al_raqi_university_hospital_qms.ui.MainScreen
import com.example.al_raqi_university_hospital_qms.ui.PaymentScreen
import com.example.al_raqi_university_hospital_qms.ui.TokenScreen
import com.example.al_raqi_university_hospital_qms.ui.ClinicTVDisplay
import com.example.al_raqi_university_hospital_qms.ui.ReceptionTVDisplay
import com.example.al_raqi_university_hospital_qms.ui.components.HospitalHeader
import com.example.al_raqi_university_hospital_qms.ui.theme.ALRaqiUniversityHospitalQMSTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ALRaqiUniversityHospitalQMSTheme {
                val navController = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = { HospitalHeader() },
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.onBackground
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "main_screen",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("main_screen") {
                            MainScreen(
                                onNavigateToDashboard = { navController.navigate("dashboard_screen") },
                                onNavigateToPayment = { sectionId -> navController.navigate("payment_screen/$sectionId") }
                            )
                        }
                        composable("dashboard_screen") {
                            DashboardScreen(
                                onNavigateBack = { navController.popBackStack() },
                                onNavigateToClinicTV = { navController.navigate("clinic_tv") },
                                onNavigateToReceptionTV = { navController.navigate("reception_tv") }
                            )
                        }
                        composable("clinic_tv") {
                            ClinicTVDisplay()
                        }
                        composable("reception_tv") {
                            ReceptionTVDisplay()
                        }
                        composable(
                            route = "payment_screen/{sectionId}",
                            arguments = listOf(navArgument("sectionId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val sectionId = backStackEntry.arguments?.getString("sectionId")
                            PaymentScreen(
                                sectionId = sectionId,
                                onNavigateBack = { navController.popBackStack() },
                                onTokenGenerated = { number, name ->
                                    navController.navigate("token_screen/$number/$name") {
                                        popUpTo("main_screen") { inclusive = false }
                                    }
                                }
                            )
                        }
                        composable(
                            route = "token_screen/{tokenNumber}/{sectionName}",
                            arguments = listOf(
                                navArgument("tokenNumber") { type = NavType.IntType },
                                navArgument("sectionName") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            val tokenNumber = backStackEntry.arguments?.getInt("tokenNumber") ?: 0
                            val sectionName = backStackEntry.arguments?.getString("sectionName") ?: ""
                            TokenScreen(
                                tokenNumber = tokenNumber,
                                sectionName = sectionName,
                                onFinish = {
                                    navController.navigate("main_screen") {
                                        popUpTo("main_screen") { inclusive = true }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
