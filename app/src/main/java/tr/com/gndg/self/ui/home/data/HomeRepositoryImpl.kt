package tr.com.gndg.self.ui.home.data

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import tr.com.gndg.self.ui.home.domain.HomeRepository
import tr.com.gndg.self.ui.home.domain.model.HomeUIState
import tr.com.gndg.self.ui.home.domain.model.HomeUIStateDetail
import tr.com.gndg.self.ui.home.domain.model.toHomeUIState

class HomeRepositoryImpl : HomeRepository {


    private var _homeUiState = mutableStateOf(HomeUIState())

    override val homeState: MutableState<HomeUIState> = _homeUiState


    override fun getHomeUIState() {
        _homeUiState.value =
            HomeUIStateDetail(
                productSize = 0,
                listOf()
            ).toHomeUIState()
    }

    override fun updateUiState(homeUIStateDetail: HomeUIStateDetail) {
        _homeUiState.value =
            HomeUIState(homeUIStateDetail = homeUIStateDetail, isEntryValid = validateInput(homeUIStateDetail))
    }


    private fun validateInput(uiState: HomeUIStateDetail = _homeUiState.value.homeUIStateDetail): Boolean {
        return with(uiState) {
            true
        }
    }



}