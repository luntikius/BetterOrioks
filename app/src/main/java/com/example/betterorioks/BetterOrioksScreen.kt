package com.example.betterorioks

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.betterorioks.ui.*
import com.example.betterorioks.ui.components.BottomNavigationBar
import com.example.betterorioks.model.BetterOrioksScreens
import com.example.betterorioks.ui.components.LoadingScreen
import com.example.betterorioks.ui.screens.*
import com.example.betterorioks.ui.states.AuthState
import com.example.betterorioks.ui.states.DebtsUiState
import com.example.betterorioks.ui.states.UserInfoUiState
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

//Screens


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun BetterOrioksApp(){
    //declarations
    val viewModel: BetterOrioksViewModel = viewModel(factory = BetterOrioksViewModel.Factory)
    val uiState by viewModel.uiState.collectAsState()
    val navController = rememberAnimatedNavController()
    if (uiState.authState == AuthState.NotLoggedIn)viewModel.retrieveToken()
    if (uiState.authState == AuthState.LoggedIn) {
        Scaffold(
            bottomBar = {
                if (uiState.authState == AuthState.LoggedIn) BottomNavigationBar(
                    navController = navController
                )
            }
        ) { padding ->
            AnimatedNavHost(
                navController = navController,
                startDestination = BetterOrioksScreens.AcademicPerformance.name,
                modifier = Modifier.padding(padding)
            ) {
                composable(route = BetterOrioksScreens.Schedule.name,
                    popEnterTransition = { fadeIn() },
                    popExitTransition = { fadeOut() }
                ) {
                    ScheduleScreen()
                }
                composable(
                    route = BetterOrioksScreens.AcademicPerformance.name,
                    exitTransition = { fadeOut() },
                    enterTransition = { fadeIn() },
                    popEnterTransition = { fadeIn(animationSpec = tween(800)) },
                    popExitTransition = { fadeOut() },
                ) {
                    if (uiState.loadingState) viewModel.getAcademicPerformance()
                    AcademicPerformanceScreen(
                        uiState = uiState,
                        navController = navController,
                        viewModel = viewModel
                    )
                }
                composable(
                    route = BetterOrioksScreens.AcademicPerformanceMore.name,
                    enterTransition = {
                        slideIntoContainer(
                            AnimatedContentScope.SlideDirection.Left,
                            animationSpec = tween(700)
                        )
                    },
                    exitTransition = {
                        slideOutOfContainer(
                            AnimatedContentScope.SlideDirection.Right,
                            animationSpec = tween(700)
                        )
                    }
                ) {
                    AcademicPerformanceMoreScreen(
                        uiState = uiState,
                        onButtonClick = {
                            navController.popBackStack(
                                route = BetterOrioksScreens.AcademicPerformance.name,
                                inclusive = false
                            )
                        },
                    )

                }
                composable(
                    route = BetterOrioksScreens.Profile.name,
                    popEnterTransition = { fadeIn() },
                    popExitTransition = { fadeOut() }
                ) {
                    if(uiState.userInfoUiState == UserInfoUiState.NotStarted)viewModel.getUserInfo()
                    ProfileScreen(
                        onExitClick = { viewModel.exit(navController)},
                        uiState = uiState,
                        onDebtClick = {navController.navigate(BetterOrioksScreens.Debts.name)},
                        viewModel = viewModel
                    )
                }
                composable(
                    route = BetterOrioksScreens.Debts.name,
                    enterTransition = {
                        slideIntoContainer(
                            AnimatedContentScope.SlideDirection.Left,
                            animationSpec = tween(700)
                        )
                    },
                    exitTransition = {
                        slideOutOfContainer(
                            AnimatedContentScope.SlideDirection.Right,
                            animationSpec = tween(700)
                        )
                    }
                ){
                    if(uiState.academicDebtsUiState == DebtsUiState.NotStarted ){viewModel.getAcademicDebts()}
                    AcademicDebtScreen(
                        onClick = {navController.popBackStack(
                        route = BetterOrioksScreens.Profile.name,
                        inclusive = false,)},
                        uiState = uiState,
                        viewModel = viewModel
                    )
                }
            }
        }
    }else if(uiState.authState == AuthState.Loading){
        LoadingScreen(modifier = Modifier.fillMaxSize())
    }else{
        AuthorizationScreen(onLogin = {viewModel.getToken(it)}, uiState = uiState)
    }
}
