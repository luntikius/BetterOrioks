package com.example.betterorioks

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.betterorioks.ui.*
import com.example.betterorioks.ui.states.SubjectsUiState
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

//Screens
enum class BetterOrioksScreens {
    Scheldue,
    AcademicPerformance,
    AcademicPerformanceMore
}
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun BetterOrioksApp(){
    //declarations
    val viewModel: BetterOrioksViewModel = viewModel(factory = BetterOrioksViewModel.Factory)
    val uiState by viewModel.uiState.collectAsState()
    val navController = rememberAnimatedNavController()
    //val backStackEntry by navController.currentBackStackEntryAsState()

    AnimatedNavHost(navController = navController, startDestination = BetterOrioksScreens.AcademicPerformance.name){
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
        composable(
            route = BetterOrioksScreens.AcademicPerformanceMore.name,
            enterTransition = {slideIntoContainer(AnimatedContentScope.SlideDirection.Left, animationSpec = tween(700))},
            exitTransition = {slideOutOfContainer(AnimatedContentScope.SlideDirection.Right, animationSpec = tween(700))}
        ){
            AcademicPerformanceMore(
                subjects = listOf(),
                uiState = uiState,
                onButtonClick = {
                    navController.popBackStack(
                        route = BetterOrioksScreens.AcademicPerformance.name,
                        inclusive = false
                    )
                }
            )

        }
    }
}