package com.studentapp.betterorioks.ui.states

import com.studentapp.betterorioks.model.News

interface NewsUiState {
    object NotStarted: NewsUiState

    object Loading: NewsUiState

    object Error: NewsUiState

    data class Success (val news: List<News>): NewsUiState
}
