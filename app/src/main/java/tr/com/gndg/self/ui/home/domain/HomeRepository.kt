package tr.com.gndg.self.ui.home.domain

import androidx.compose.runtime.MutableState
import tr.com.gndg.self.ui.home.domain.model.HomeUIState
import tr.com.gndg.self.ui.home.domain.model.HomeUIStateDetail

interface HomeRepository {

    val homeState : MutableState<HomeUIState>

    fun getHomeUIState()

    fun updateUiState(homeUIStateDetail: HomeUIStateDetail)

}