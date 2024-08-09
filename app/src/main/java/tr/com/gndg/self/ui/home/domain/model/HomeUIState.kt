package tr.com.gndg.self.ui.home.domain.model


data class HomeUIState (
    val homeUIStateDetail: HomeUIStateDetail = HomeUIStateDetail(),
    val isEntryValid: Boolean = false
)

data class HomeUIStateDetail (
    val productSize: Int = 0,
    val menuItems : List<HomeMenuItem> = listOf()
)

fun HomeUIStateDetail.toHomeUIState() = HomeUIState(
    homeUIStateDetail = HomeUIStateDetail(
        this.productSize,
        this.menuItems
    )
)