package tr.com.gndg.self.ui.home.presentation

import tr.com.gndg.self.ui.home.domain.HomeRepository
import tr.com.gndg.self.ui.home.domain.model.HomeUIStateDetail

class HomeUIStateHolder(private val repository: HomeRepository) {

    init {
        repository.getHomeUIState()
    }

    var homeState = repository.homeState

    fun updateUiState(homeUIStateDetail: HomeUIStateDetail) = repository.updateUiState(homeUIStateDetail)

}