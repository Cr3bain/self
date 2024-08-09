package tr.com.gndg.self.ui.home.domain.model

import androidx.compose.runtime.Composable

data class HomeMenuItem(
    val name: String,
    val route : String,
    val key :Int?,
    val icon: Int?,
    val iconContentDescription: String?,
    val composable: @Composable () -> Unit? = {}
)
