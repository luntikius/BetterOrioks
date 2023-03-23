package com.studentapp.betterorioks

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.studentapp.betterorioks.ui.screens.AcademicPerformanceScreen
import com.studentapp.betterorioks.ui.components.BottomNavigationBar
import com.studentapp.betterorioks.model.BetterOrioksScreens
import com.studentapp.betterorioks.ui.components.LoadingScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.studentapp.betterorioks.ui.BetterOrioksViewModel
import com.studentapp.betterorioks.ui.screens.*
import com.studentapp.betterorioks.ui.states.*
import com.studentapp.betterorioks.ui.theme.BetterOrioksTheme

//Screens


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun BetterOrioksApp(){
    //declarations
    val viewModel: BetterOrioksViewModel = viewModel(factory = BetterOrioksViewModel.Factory)
    val uiState by viewModel.uiState.collectAsState()
    val navController = rememberAnimatedNavController()
    val context = LocalContext.current

    if (uiState.authState == AuthState.NotLoggedIn)
        viewModel.retrieveToken(context = context, navController = navController)

    BetterOrioksTheme(
        darkTheme = when(uiState.theme){
            0 -> isSystemInDarkTheme()
            1 -> false
            else -> true
        }
    ) {
        when (uiState.authState) {
            AuthState.LoggedIn -> {
                Scaffold(
                    bottomBar = {
                        if (uiState.authState == AuthState.LoggedIn) BottomNavigationBar(
                            navController = navController
                        )
                    }
                ) { padding ->
                    AnimatedNavHost(
                        navController = navController,
                        startDestination = BetterOrioksScreens.Schedule.name,
                        modifier = Modifier.padding(padding)
                    ) {
                        composable(route = BetterOrioksScreens.Schedule.name,
                            popEnterTransition = { fadeIn() },
                            popExitTransition = { fadeOut() }
                        ) {
                            if (uiState.fullScheduleUiState is FullScheduleUiState.NotStarted) viewModel.getFullSchedule(
                                context = LocalContext.current
                            )
                            ScheduleScreen(uiState = uiState, viewModel = viewModel)
                        }
                        composable(
                            route = BetterOrioksScreens.AcademicPerformance.name,
                            exitTransition = { fadeOut() },
                            enterTransition = { fadeIn() },
                            popEnterTransition = { fadeIn(animationSpec = tween(800)) },
                            popExitTransition = { fadeOut() }
                        ) {
                            if (uiState.subjectsFromSiteUiState is SubjectsFromSiteUiState.NotStarted) viewModel.getAcademicPerformanceFromSite()
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
                                onControlEventClick = {
                                    viewModel.setCurrentControlEvent(it)
                                },
                                onResourceClick = {
                                    navController.navigate(BetterOrioksScreens.Resources.name)
                                }
                            )

                        }
                        composable(
                            route = BetterOrioksScreens.Profile.name,
                            popEnterTransition = { fadeIn() },
                            popExitTransition = { fadeOut() }
                        ) {
                            if (uiState.userInfoUiState == UserInfoUiState.NotStarted) viewModel.getUserInfo()
                            if (uiState.newsUiState == NewsUiState.NotStarted) viewModel.getNews()
                            ProfileScreen(
                                onExitClick = {
                                    viewModel.exit(
                                        context = context,
                                        navController = navController
                                    )
                                },
                                uiState = uiState,
                                viewModel = viewModel,
                                onSettingsClick = {navController.navigate(BetterOrioksScreens.Settings.name)}
                            )
                        }
                        composable(
                            route = BetterOrioksScreens.Resources.name,
                            enterTransition = {
                                fadeIn()
                            },
                            exitTransition = {
                                fadeOut()
                            }
                        ) {
                            if (uiState.resourcesUiState is ResourcesUiState.NotStarted) viewModel.getResources()
                            ResourcesScreen(
                                resourcesUiState = uiState.resourcesUiState,
                                onBackButtonClick = {
                                    navController.popBackStack(
                                        route = BetterOrioksScreens.AcademicPerformanceMore.name,
                                        inclusive = false,
                                    )
                                },
                                onRefresh = { viewModel.getResources() },
                                subjectName = uiState.currentSubject.name
                            )
                        }
                        composable(
                            route = BetterOrioksScreens.Settings.name,
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
                            SettingsScreen(uiState = uiState,
                                viewModel = viewModel,
                                onBackButtonPress = {
                                    navController.popBackStack(
                                        route = BetterOrioksScreens.Profile.name,
                                        inclusive = false
                                    )
                                }
                            )
                        }
                    }
                }
            }
            AuthState.Loading  -> {
                LoadingScreen(modifier = Modifier.fillMaxSize())
            }
            else               -> {
                AuthorizationScreen(onLogin = { s1, s2 ->
                    viewModel.getAuthInfo(
                        login = s1,
                        password = s2
                    )
                }, uiState = uiState)
            }
        }
    }
}
