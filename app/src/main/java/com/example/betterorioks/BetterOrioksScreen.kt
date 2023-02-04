package com.example.betterorioks

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import com.example.betterorioks.ui.*
import com.example.betterorioks.ui.components.ErrorScreen
import com.example.betterorioks.ui.components.LoadingScreen
import com.example.betterorioks.ui.states.SubjectsUiState

//Screens
enum class BetterOrioksScreens {
    Scheldue,
    AcademicPerformance,
    AcademicPerformanceMore
}
@Composable
fun BetterOrioksApp(){
    //declarations
    val viewModel: BetterOrioksViewModel = viewModel(factory = BetterOrioksViewModel.Factory)
    val uiState by viewModel.uiState.collectAsState()
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()

    NavHost(navController = navController, startDestination = BetterOrioksScreens.AcademicPerformance.name){
        composable(route = BetterOrioksScreens.Scheldue.name){

        }
        composable(route = BetterOrioksScreens.AcademicPerformance.name) {
            if(uiState.subjectsUiState !is SubjectsUiState.Success)viewModel.getAcademicPerformance()
            AcademicPerformanceScreen(
                uiState = uiState,
                navController = navController,
                viewModel = viewModel
            )

        }
        composable(route = BetterOrioksScreens.AcademicPerformanceMore.name){
            AcademicPerformanceMore(
                subjects = listOf(),
                uiState = uiState,
                onButtonClick = {
                    navController.popBackStack(route = BetterOrioksScreens.AcademicPerformance.name, inclusive = false)
                }
            )
        }
    }
}