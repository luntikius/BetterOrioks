package com.studentapp.betterorioks.ui.states

import com.studentapp.betterorioks.model.GroupElement

sealed interface GroupUiState{
    object Loading: GroupUiState
    object Error: GroupUiState
    data class Success (val groups : List<GroupElement>): GroupUiState
}