package tr.com.gndg.self.ui.transactions

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import org.koin.androidx.compose.koinViewModel
import tr.com.gndg.self.R
import tr.com.gndg.self.SelfTopAppBar
import tr.com.gndg.self.core.util.TransactionType
import tr.com.gndg.self.core.util.returnTypeString
import tr.com.gndg.self.domain.join.TransactionJoinData
import tr.com.gndg.self.domain.model.WarehouseUiState
import tr.com.gndg.self.ui.navigation.NavigationDestination
import tr.com.gndg.self.ui.scopes.SourceAndTargetNameText
import tr.com.gndg.self.ui.scopes.WarehouseNameText
import tr.com.gndg.self.ui.search.SearchRow
import tr.com.gndg.self.ui.texts.TransactionStockPieceText
import tr.com.gndg.self.ui.transactions.listItems.AllWarehouseTransactionItem
import tr.com.gndg.self.ui.transactions.presentation.TransactionsViewModel

object TransactionsDestination : NavigationDestination {
    override val route = "transactions"
    override val titleRes = R.string.transactionsScreen_title
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun TransactionScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
    warehouseSelectScreen : () -> Unit,
    selectTransaction: (Long) -> Unit,
    viewModel: TransactionsViewModel = koinViewModel()
) {
    LaunchedEffect(key1 = viewModel) {
        this.launch(Dispatchers.IO) {
            viewModel.getTransactions()
        }
    }
    val coroutineScope = rememberCoroutineScope()
    val transactionsList : State<List<TransactionJoinData>> = viewModel.transactionJoinDataList.collectAsState()
    val warehouseState by viewModel.warehouseUiState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    var searchVisible by remember { mutableStateOf(false)  }
    val pagerState = rememberPagerState(pageCount = {
        4
    })

    val filteredTransactionList = if (warehouseState.warehouse.id == 0L) {
        transactionsList.value.sortedByDescending { it.transaction.date }
    } else {

        transactionsList.value.filter {
            when(it.transaction.transactionType) {
                TransactionType.Arrival -> {
                    it.transaction.targetID == warehouseState.warehouse.id
                }
                TransactionType.Outgoing -> {
                    it.transaction.sourceID == warehouseState.warehouse.id
                }
                TransactionType.Transfer -> {
                    it.transaction.sourceID == warehouseState.warehouse.id
                            ||
                            it.transaction.targetID == warehouseState.warehouse.id
                }
                else -> {
                    false
                }
            }
        }.let {list->
            list.sortedByDescending { it.transaction.date }
        }
    }


    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            Column {
                SelfTopAppBar(
                    title = stringResource(TransactionsDestination.titleRes),
                    canNavigateBack = canNavigateBack,
                    scrollBehavior = scrollBehavior,
                    navigateUp = onNavigateUp,
                    navigateBack = navigateBack,
                    action = {
                        IconButton(onClick = warehouseSelectScreen) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_warehouse_24),
                                contentDescription = "Localized description",
                            )
                        }

                        IconButton(onClick = { searchVisible = !searchVisible }) {
                            Icon(Icons.Filled.Search, contentDescription = "Search")
                        }
                    }
                )

                AnimatedVisibility(
                    visible = searchVisible,
                ) {

                    SearchRow(
                        onSearch =  viewModel::searchTransaction,
                        searchTextState = viewModel.searchText,
                        startBarcodeScanner = {},
                        showBarcodeScannerIcon = false,
                        hint = stringResource(id = R.string.searchTransaction)
                    )

                }
            }


        },
        bottomBar = {
            BottomAppBar(
                actions = {
                    val text = when(pagerState.currentPage) {
                        0-> {
                            stringResource(id = R.string.everything)
                        }
                        1-> {
                            stringResource(id = R.string.arrivals)
                        }
                        2-> {
                            stringResource(id = R.string.outgoings)
                        }
                        3-> {
                            stringResource(id = R.string.transfers)
                        } else -> {
                            ""
                        }
                    }
                    Column {

                        Row {

                            Text(text = text)
                        }
                        Row {
                            val iconSize = 24.dp
                            val iconSelectedSize = 48.dp

                            IconButton(onClick = {
                                coroutineScope.launch {
                                    // Call scroll to on pagerState
                                    pagerState.scrollToPage(0)
                                }
                            }) {
                                Icon(
                                    modifier = Modifier
                                        .size(if (pagerState.currentPage ==0) {iconSelectedSize} else {iconSize}),
                                    painter = painterResource(id = R.drawable.transaction_list_48),
                                    contentDescription = "All transactions",
                                    tint = colorResource(id = R.color.iconColor)
                                )

                            }
                            IconButton(onClick = {
                                coroutineScope.launch {
                                    // Call scroll to on pagerState
                                    pagerState.scrollToPage(1)
                                }
                            }) {
                                Icon(
                                    modifier = Modifier
                                        .size(if (pagerState.currentPage ==1) {iconSelectedSize} else {iconSize}),
                                    painter = painterResource(id = R.drawable.unpacking_48),
                                    contentDescription = "Transaction Arrivals",
                                    tint = colorResource(id = R.color.iconColor)
                                )
                            }
                            IconButton(onClick = {
                                coroutineScope.launch {
                                    // Call scroll to on pagerState
                                    pagerState.scrollToPage(2)
                                }
                            }) {
                                Icon(
                                    modifier = Modifier
                                        .size(if (pagerState.currentPage ==2) {iconSelectedSize} else {iconSize}),
                                    painter = painterResource(id = R.drawable.packing_48),
                                    contentDescription = "Transaction Outgoings",
                                    tint = colorResource(id = R.color.iconColor)
                                )
                            }
                            IconButton(onClick = {
                                coroutineScope.launch {
                                    // Call scroll to on pagerState
                                    pagerState.scrollToPage(3)
                                }
                            }) {
                                Icon(
                                    modifier = Modifier
                                        .size(if (pagerState.currentPage ==3) {iconSelectedSize} else {iconSize}),
                                    painter = painterResource(id = R.drawable.move_stock_48),
                                    contentDescription = "Transaction Transfers",
                                    tint = colorResource(id = R.color.iconColor)
                                )
                            }
                        }
                    }


                },
/*                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {  },
                        containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                    ) {
                        Icon(Icons.Filled.Add, "Localized description")
                    }
                }*/
            )
        },
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


        HorizontalPager(
            modifier = Modifier
                .padding(top = dimensionResource(id = R.dimen.screenTopPadding))
                .fillMaxSize(),
            contentPadding = innerPadding,
            state = pagerState) { page ->

            when(page) {
                // TransactionType AND pageNumber is ==
                0-> {
                    TransactionsBody(/*modifier = Modifier
                        .padding(innerPadding)
                        .padding(top = dimensionResource(id = R.dimen.screenTopPadding))
                        .fillMaxSize(),*/
                        transactionJoinDataList = filteredTransactionList,
                        warehouseUiState = warehouseState,
                        selectTransaction = { selectTransaction(it.transaction.transactionID) },
                        pageFilter = 0

                    )
                }
                TransactionType.Arrival -> {
                    TransactionsBody(/*modifier = Modifier
                        .padding(innerPadding)
                        .padding(top = dimensionResource(id = R.dimen.screenTopPadding))
                        .fillMaxSize(),*/
                        transactionJoinDataList = filteredTransactionList,
                        warehouseUiState = warehouseState,
                        selectTransaction = { selectTransaction(it.transaction.transactionID) },
                        pageFilter = TransactionType.Arrival

                    )

                }
                TransactionType.Outgoing -> {
                    TransactionsBody(/*modifier = Modifier
                        .padding(innerPadding)
                        .padding(top = dimensionResource(id = R.dimen.screenTopPadding))
                        .fillMaxSize(),*/
                        transactionJoinDataList = filteredTransactionList,
                        warehouseUiState = warehouseState,
                        selectTransaction = { selectTransaction(it.transaction.transactionID) },
                        pageFilter = TransactionType.Outgoing

                    )
                }
                TransactionType.Transfer -> {
                    TransactionsBody(/*modifier = Modifier
                        .padding(innerPadding)
                        .padding(top = dimensionResource(id = R.dimen.screenTopPadding))
                        .fillMaxSize(),*/
                        transactionJoinDataList = filteredTransactionList,
                        warehouseUiState = warehouseState,
                        selectTransaction = { selectTransaction(it.transaction.transactionID) },
                        pageFilter = TransactionType.Transfer

                    )

                }
            }
        }


    }
}

@Composable
fun TransactionsBody(
    //modifier : Modifier,
    transactionJoinDataList: List<TransactionJoinData>,
    warehouseUiState: WarehouseUiState,
    selectTransaction: (TransactionJoinData) -> Unit,
    pageFilter : Int
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = if (transactionJoinDataList.isEmpty()) { Arrangement.Center } else { Arrangement.Top },
    ) {

        if (transactionJoinDataList.isEmpty()) {
            Text(
                text = stringResource(R.string.noTransactions),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
        } else {
            val transactionTypeFilter = if (pageFilter !=0) {
                transactionJoinDataList.filter {
                    it.transaction.transactionType == pageFilter}
            } else {
                transactionJoinDataList
            }

            TransactionsLazyColumn(
                transactionJoinDataList = transactionTypeFilter,
                warehouseID = warehouseUiState.warehouse.id,
                modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_extra_small)),
                selectTransaction = selectTransaction,
            )

        }
    }
}

@Composable
fun TransactionsLazyColumn(
    transactionJoinDataList: List<TransactionJoinData>,
    warehouseID: Long,
    modifier: Modifier,
    selectTransaction: (TransactionJoinData) -> Unit
) {

    LazyColumn(
        modifier = modifier.fillMaxWidth()
    ) {

        items(transactionJoinDataList) {

            if (warehouseID == 0L) {
                AllWarehouseTransactionItem(
                    transactionJoinData = it,
                    modifier = Modifier.clickable {
                        selectTransaction(it)
                    })

            } else {
                TransactionItem(
                    transactionJoinData = it,
                    modifier = Modifier.clickable {
                        selectTransaction(it)
                    })
            }


        }
    }

}

@Composable
fun TransactionItem(
    transactionJoinData: TransactionJoinData,
    modifier: Modifier
){
    val context = LocalContext.current


    val sourceTargetUUID = when(transactionJoinData.transaction.transactionType) {
        TransactionType.Arrival-> {
            transactionJoinData.transaction.sourceUUID //supplier
        }
        TransactionType.Outgoing-> {
            transactionJoinData.transaction.targetUUID // customer
        }
        TransactionType.Transfer-> {
            transactionJoinData.transaction.targetUUID // other warehouse
        } else -> {
            null
        }
    }

    Box(modifier = modifier
        .background(MaterialTheme.colorScheme.background)
        .padding(dimensionResource(id = R.dimen.padding_extra_small)))
    {
        Card {
            ListItem(
                colors = ListItemDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                ),
                headlineContent = {
                    Text(
                        text = returnTypeString(context, transactionJoinData.transaction.transactionType)
                    )


                },
                supportingContent = {
                    Column {
                        Text(text = DateTime(transactionJoinData.transaction.date).toString("dd.MM.yyyy"))


                        //Ürünün satın aldındığı supplier
                        if (sourceTargetUUID != null) {

                            SourceAndTargetNameText(
                                transactionType = transactionJoinData.transaction.transactionType,
                                sourceTargetUUID = sourceTargetUUID
                            )
                        }

                    }

                },
                trailingContent = {

                    TransactionStockPieceText(
                        modifier = Modifier,
                        textStyle = MaterialTheme.typography.bodyLarge,
                        transactionJoinData.dataList
                    )

                },
            )
        }
    }

}
