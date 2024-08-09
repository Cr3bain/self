package tr.com.gndg.self.ui.customers

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel
import tr.com.gndg.self.R
import tr.com.gndg.self.SelfTopAppBar
import tr.com.gndg.self.core.preferences.sharedPreferencesBoolean
import tr.com.gndg.self.domain.model.Customer
import tr.com.gndg.self.ui.customers.presentation.CustomersViewModel
import tr.com.gndg.self.ui.navigation.NavigationDestination
import tr.com.gndg.self.ui.search.SearchRow

object CustomersDestination : NavigationDestination {
    override val route = "customers"
    override val titleRes = R.string.customersScreen_title
    const val transactionIDArg = "transactionID"
    val routeWithTransactionArgs = "$route/{$transactionIDArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomersScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
    transactionID: Long?,
    toCustomerFormScreen:(String?)-> Unit
) {
    val viewModel : CustomersViewModel = koinViewModel()
    val context = LocalContext.current
    LaunchedEffect(key1 = viewModel){
        viewModel.getCustomers()
    }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    var searchVisiblePref by context.sharedPreferencesBoolean("customer_search")
    var searchVisible by remember { mutableStateOf(searchVisiblePref)  }
    val customerList = viewModel.customersList.collectAsState()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Column {
                SelfTopAppBar(
                    title = stringResource(CustomersDestination.titleRes),
                    canNavigateBack = canNavigateBack,
                    scrollBehavior = scrollBehavior,
                    navigateUp = onNavigateUp,
                    navigateBack = navigateBack,
                    action = {

                        IconButton(onClick = {
                            searchVisiblePref = !searchVisiblePref
                            searchVisible = !searchVisible
                        }) {
                            Icon(Icons.Filled.Search, contentDescription = "Search",
                                )
                        }
                    }
                )

                AnimatedVisibility(
                    visible = searchVisible,
                ) {

                    SearchRow(
                        onSearch =  viewModel::searchCustomer,
                        searchTextState = viewModel.searchText,
                        startBarcodeScanner = {},
                        showBarcodeScannerIcon = false,
                        hint = stringResource(id = R.string.searchCustomer)
                    )

                }
            }


        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                toCustomerFormScreen(null)
            },
                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()) {
                Icon(Icons.Filled.Add, "Add Customer")

            }
        }
    ) { innerPadding->

        CustomersBody(
            modifier = Modifier
                .padding(innerPadding)
                .padding(top = dimensionResource(id = R.dimen.screenTopPadding))
                .fillMaxSize(),
            customers = customerList.value,
            selectCustomer = {
                if (transactionID == null) {
                    toCustomerFormScreen(it.uuid)
                } else {
                    viewModel.setSourceTarget(it).also {
                        navigateBack()
                    }
                }
            }
        )


    }

}

@Composable
fun CustomersBody(
    modifier : Modifier,
    customers : List<Customer>,
    selectCustomer : (Customer) -> Unit
) {

    LazyColumn(
        modifier = modifier
    ) {
        items(customers, key = { it.uuid }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(id = R.dimen.padding_small))
                    .clickable {
                        selectCustomer(it)
                    }
                    .height(50.dp)
                ,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(dimensionResource(id = R.dimen.padding_small)),

                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = it.name,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }


            }

        }

    }

}