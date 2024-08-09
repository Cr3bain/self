package tr.com.gndg.self.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.getViewModel
import tr.com.gndg.self.R
import tr.com.gndg.self.core.preferences.sharedPreferencesBoolean
import tr.com.gndg.self.core.util.Constants
import tr.com.gndg.self.ui.home.domain.homeMenuIconList
import tr.com.gndg.self.ui.home.domain.homeMenuList
import tr.com.gndg.self.ui.home.domain.homeMenuTripleList
import tr.com.gndg.self.ui.home.domain.model.HomeMenuItem
import tr.com.gndg.self.ui.home.presentation.HomeViewModel
import tr.com.gndg.self.ui.navigation.NavigationDestination
import tr.com.gndg.self.ui.scopes.WarehouseNameText

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}

@Composable
fun AppMainTitle(premium: Boolean){
    if (!premium) {
        Text(stringResource(HomeDestination.titleRes))
    } else {
        Text(stringResource(HomeDestination.titleRes) + " (Premium)")
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToRoute: (String) -> Unit,
    modifier: Modifier = Modifier,
    warehouseSelectScreen : () -> Unit,
    homeViewModel: HomeViewModel = getViewModel()
) {
    LaunchedEffect(key1 = homeViewModel) {
        homeViewModel.resetTransaction()
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val context = LocalContext.current
    val premiumUser by context.sharedPreferencesBoolean(Constants.PREMIUM)

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                scrollBehavior= scrollBehavior,
                title = { AppMainTitle(premiumUser) },
                actions = {
                    IconButton(onClick = warehouseSelectScreen) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_warehouse_24),
                            contentDescription = "Localized description",
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) {innerPadding->

        WarehouseNameText(
            Modifier
                .padding(innerPadding)
                .fillMaxWidth()
                .height(20.dp),
            null,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )

        HomeBody(
            menuItemList = homeMenuList(context),
            tripleList = homeMenuTripleList(context),
            homeMenuIconList =homeMenuIconList(context),
            onItemClick = navigateToRoute,
            modifier = Modifier
                .padding(innerPadding)
                .padding(top = dimensionResource(id = R.dimen.screenTopPadding))
                .fillMaxSize()
        )
    }

}

@Composable
private fun HomeBody(
    menuItemList: List<HomeMenuItem>,
    tripleList: List<HomeMenuItem>,
    homeMenuIconList: List<HomeMenuItem>,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier,

    ) {

    val verticalScrollableState = rememberScrollState()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.verticalScroll(verticalScrollableState)
    ) {

        if (menuItemList.isEmpty()) {
            Text(
                text = stringResource(R.string.no_item_description),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
        } else {
            MenuItemList(
                menuItemList = menuItemList,
                tripleList = tripleList,
                homeMenuIconList = homeMenuIconList,
                onItemClick = { onItemClick(it.route) },
                //modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_small))
            )
        }
    }
}


@Composable
private fun MenuItemList(
    menuItemList: List<HomeMenuItem>,
    tripleList: List<HomeMenuItem>,
    homeMenuIconList: List<HomeMenuItem>,
    onItemClick: (HomeMenuItem) -> Unit,
) {

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.height(360.dp)
    ) {
        items(menuItemList) {menuItem->
            MenuItem(item = menuItem,
                modifier = Modifier
                    //.aspectRatio(1.5F)
                    .padding(8.dp)
                    .width(135.dp)
                    .height(100.dp)
                    .clickable { onItemClick(menuItem) })

        }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.height(120.dp)
    ) {
        items(tripleList) {menuItem->
            MenuItem(item = menuItem,
                modifier = Modifier
                    //.aspectRatio(1.5F)
                    .padding(8.dp)
                    .width(135.dp)
                    .height(100.dp)
                    .clickable { onItemClick(menuItem) })

        }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        modifier = Modifier.height(80.dp)
    ) {
        items(homeMenuIconList) {menuItem->
            MenuItemIcon(item = menuItem,
                modifier = Modifier
                    //.aspectRatio(1.5F)
                    .padding(8.dp)
                    .width(60.dp)
                    .height(60.dp)
                    .clickable { onItemClick(menuItem) })

        }
    }
}

@Composable
private fun MenuItem(
    item: HomeMenuItem,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        modifier= modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        //border = BorderStroke(1.dp, Color.Black),

    ) {
        Column(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Icon(
                modifier = Modifier.size(48.dp),
                painter = painterResource(id = item.icon?:R.drawable.baseline_image_54),
                contentDescription = null,
                tint = colorResource(id = R.color.iconColor))

            if (item.key != 1) {
                Text(
                    text = item.name.uppercase(),
                    style = MaterialTheme.typography.titleSmall,
                )
            } else {
                item.composable()
            }



        }
    }

}

@Composable
private fun MenuItemIcon(
    item: HomeMenuItem,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        modifier= modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        //border = BorderStroke(1.dp, Color.Black),

    ) {

        Column(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = item.icon?:R.drawable.baseline_image_54),
                contentDescription = item.iconContentDescription,
                tint = colorResource(id = R.color.iconColor),
                )
        }



    }

}