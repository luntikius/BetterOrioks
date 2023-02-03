package com.example.betterorioks

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.betterorioks.ui.AcademicPerformance
import com.example.betterorioks.ui.AcademicPerformanceMore
import com.example.betterorioks.ui.BetterOrioksViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

val TAG:String = "BetterOrioksScreen"
enum class BetterOrioksScreens {
    Scheldue,
    AcademicPerformance,
    AcademicPerformanceMore
}
@Composable
fun BetterOrioksApp(viewModel: BetterOrioksViewModel = viewModel()){
    //declarations

    val uiState by viewModel.uiState.collectAsState()
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    Log.d("BetterOrioksApp","${uiState.currentSubject}")

    NavHost(navController = navController, startDestination = BetterOrioksScreens.AcademicPerformance.name){
        composable(route = BetterOrioksScreens.Scheldue.name){

        }
        composable(route = BetterOrioksScreens.AcademicPerformance.name) {
            AcademicPerformance(
                getAcademicPerformance = {viewModel.getAcademicPerformance()},
                setCurrentSubject = {viewModel.setCurrentSubject(it)},
                onComponentClicked = {
                    navController.navigate(BetterOrioksScreens.AcademicPerformanceMore.name)
                    Log.d(TAG,"onClick Worked! ${uiState.currentSubject}")
                }
            )
        }
        composable(route = BetterOrioksScreens.AcademicPerformanceMore.name){
            AcademicPerformanceMore(
                getAcademicPerformance = {viewModel.getAcademicPerformance()},
                uiState = uiState,
                onButtonClick = {
                    navController.popBackStack(route = BetterOrioksScreens.AcademicPerformance.name, inclusive = false)
                }
            )
        }
    }
}