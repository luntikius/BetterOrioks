package com.example.betterorioks

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import com.example.betterorioks.ui.AcademicPerformance
import com.example.betterorioks.ui.BetterOrioksViewModel

@Composable
fun BetterOrioksApp(viewModel: BetterOrioksViewModel = BetterOrioksViewModel()){
    AcademicPerformance(viewModel = viewModel)
}