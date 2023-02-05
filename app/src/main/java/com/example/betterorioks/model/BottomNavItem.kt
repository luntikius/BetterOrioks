package com.example.betterorioks.model

import com.example.betterorioks.R

sealed class BottomNavItem(var title:String, var icon:Int, var screen_route:String) {
    object Profile: BottomNavItem("профиль", R.drawable.profile,"Profile")
    object Schedule: BottomNavItem("расписание",R.drawable.scheldule,"Schedule")
    object AcademicPerformance: BottomNavItem("успеваемость",R.drawable.academic_performance,"AcademicPerformance")
}