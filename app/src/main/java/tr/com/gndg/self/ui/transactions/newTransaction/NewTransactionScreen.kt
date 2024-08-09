package tr.com.gndg.self.ui.transactions.newTransaction

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import org.koin.androidx.compose.getViewModel
import tr.com.gndg.self.R
import tr.com.gndg.self.SelfTopAppBar
import tr.com.gndg.self.core.drag.DeleteAction
import tr.com.gndg.self.core.drag.DragAnchors
import tr.com.gndg.self.core.drag.DraggableItem
import tr.com.gndg.self.core.preferences.sharedPreferencesBoolean
import tr.com.gndg.self.core.util.TransactionType
import tr.com.gndg.self.core.util.returnTypeString
import tr.com.gndg.self.core.util.stringToBigDecimal
import tr.com.gndg.self.domain.model.transactions.TransactionDataDetail
import tr.com.gndg.self.domain.model.transactions.TransactionState
import tr.com.gndg.self.ui.dialogs.DatePickerDialogScreen
import tr.com.gndg.self.ui.dialogs.SelfAlertDialogScreen
import tr.com.gndg.self.ui.navigation.NavigationDestination
import tr.com.gndg.self.ui.scopes.ProductImage
import tr.com.gndg.self.ui.scopes.ProductNameText
import tr.com.gndg.self.ui.scopes.SourceTargetTextField
import tr.com.gndg.self.ui.scopes.WarehouseNameText
import tr.com.gndg.self.ui.textFields.DatePickerRow
import tr.com.gndg.self.ui.transactions.newTransaction.presentation.NewTransactionViewModel
import java.math.BigDecimal
import kotlin.math.roundToInt


object NewTransactionScreenDestination : NavigationDestination {
    override val route = "new_transaction"
    override val titleRes = R.string.transactions
    const val transactionIDArg = "transactionID"
    val routeWithTransactionIDArgs = "$route/{$transactionIDArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewTransactionScreen(
    transactionID: Long?,
    navigateBack: () -> Unit,
    toInventoryScreen: (Int) -> Unit,
    toSupplierScreen: (Long) -> Unit,
    toCustomerScreen: (Long) -> Unit,
    viewModel: NewTransactionViewModel = getViewModel()

) {

    LaunchedEffect(key1 = transactionID) {
        viewModel.getTransaction()
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val transactionState = viewModel.repositoryTransactionState.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    when {
        viewModel.showToast -> {
            Toast.makeText(LocalContext.current, viewModel.toastMessage, Toast.LENGTH_SHORT).show()
            viewModel.showToast = !viewModel.showToast
        }
        viewModel.openDialogDeleteTransactionFail -> {
            SelfAlertDialogScreen(
                modifier = Modifier,
                onConfirmation = { viewModel.openDialogDeleteTransactionFail = false },
                dialogTitle = stringResource(id = R.string.cantDeleteTitle),
                dialogText = stringResource(id = R.string.cantDeleteText),
                icon = Icons.Filled.Info,
                dismissButton = null
            )

        }
        viewModel.openDialogDeleteTransactionConfirm -> {

            SelfAlertDialogScreen(
                modifier = Modifier,
                onConfirmation = {
                    viewModel.openDialogDeleteTransactionConfirm = false

                    scope.launch {
                        viewModel.deleteTransaction(transactionState.value)
                            .onSuccess {
                            viewModel.resetTransaction()
                            navigateBack()
                        }.onFailure {
                                this.launch(Dispatchers.Main) {
                                    viewModel.toastMessage = it.message.toString()
                                    viewModel.showToast = true
                                }

                            }
                    }

                },
                dialogTitle = stringResource(id = R.string.areYouSure),
                dialogText = stringResource(id = R.string.canDeleteText),
                icon = Icons.Filled.Warning,
                dismissButton = {
                    TextButton(
                        onClick = {
                            viewModel.openDialogDeleteTransactionConfirm = false
                        }
                    ) {
                        Text(stringResource(id = R.string.dismiss))
                    }
                }
            )

        }
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            SelfTopAppBar(
                title = returnTypeString(context, transactionState.value.transactionDetail.transactionType),
                canNavigateBack = true,
                scrollBehavior = scrollBehavior,
                navigateUp = {  },
                navigateBack = {
                    navigateBack()
                    viewModel.resetTransaction()
                               } ,
                action = {
                    AnimatedVisibility(visible = transactionState.value.transactionDetail.transactionID != 0L) {
                        IconButton(onClick = {
                            scope.launch {
                                viewModel.transactionDeleteRequest()
                                    .onSuccess {
                                    viewModel.openDialogDeleteTransactionConfirm = true
                                }
                                    .onFailure {
                                    viewModel.openDialogDeleteTransactionFail = true
                                    Log.e("transactionDeleteRequest1", it.message.toString())
                                }
                            }

                        }) {
                            Icon(imageVector = Icons.Filled.Delete,
                                contentDescription = "Delete")
                        }
                    }

                    IconButton(
                        onClick = {
                        if (transactionState.value.transactionDetail.transactionID != 0L) {
                            //update area
                            scope.launch {
                                viewModel.updateTransactionDetail()
                                    .onSuccess {
                                        this.launch(Dispatchers.Main) {
                                            viewModel.toastMessage = context.getString(R.string.updated)
                                            viewModel.showToast = true
                                        }

                                }   .onFailure {
                                        this.launch(Dispatchers.Main) {
                                            viewModel.toastMessage = it.message.toString()
                                            viewModel.showToast = true
                                        }
                                    Log.e("updateTransactionDetail", it.message.toString())
                                }
                            }

                        } else {
                            //new
                            scope.launch {
                                viewModel.saveTransaction().onSuccess {
                                    this.launch(Dispatchers.Main) {
                                        viewModel.toastMessage = context.getString(R.string.saved)
                                        viewModel.showToast = true
                                    }
                                }.onFailure {
                                    this.launch(Dispatchers.Main) {
                                        viewModel.toastMessage = it.message.toString()
                                        viewModel.showToast = true
                                    }
                                }
                            }

                        }
                    }) {
                        Icon(imageVector = Icons.Filled.Done,
                            contentDescription = "Done")
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                actions = {

                    Column {

                        Text(modifier = Modifier.padding(start = 6.dp),
                            text = stringResource(id = R.string.products, transactionState.value.dataList.size)
                        )

                        var totalPrice = BigDecimal.ZERO
                        transactionState.value.dataList
                            .map { stringToBigDecimal(it.unitPrice)?.times(
                            BigDecimal(it.piece)
                        ) }.forEach {
                            it?.let {
                                totalPrice += it
                            }

                        }.let {
                            Text(modifier = Modifier.padding(start = 6.dp),
                                text = stringResource(id = R.string.totalPrice, totalPrice)
                            )
                        }

                        Row {

                        }
                    }

                },
                floatingActionButton = {
                    FloatingActionButton(onClick = {
                        toInventoryScreen(transactionState.value.transactionDetail.transactionType)
                    },
                        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()) {
                        Icon(Icons.Filled.Add, "Add Warehouse")

                    }
                }
            )
        },


    ) { innerPadding->

        WarehouseNameText(
            Modifier
                .padding(innerPadding)
                .fillMaxWidth()
                .height(20.dp),
            null,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )

        TransactionProductBody(
            modifier = Modifier.padding(innerPadding)
                .padding(top = dimensionResource(id = R.dimen.screenTopPadding)),
            transactionState= transactionState.value,
            viewModel = viewModel,
            sourceTargetSelect = {
                when(transactionState.value.transactionDetail.transactionType) {
                    TransactionType.Arrival -> {
                        toSupplierScreen(it)
                    }
                    TransactionType.Outgoing -> {
                        toCustomerScreen(it)
                    }
                }

            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionProductBody(
    modifier: Modifier,
    transactionState: TransactionState,
    viewModel: NewTransactionViewModel,
    sourceTargetSelect: (Long) -> Unit
) {
    val selectedDate = DateTime(transactionState.transactionDetail.date)
    val context = LocalContext.current

    var datePickerDialog by remember {
        mutableStateOf(false)
    }

    var tabVisiblePref by context.sharedPreferencesBoolean("transaction_tab")
    var tabVisible by remember {
        mutableStateOf(tabVisiblePref)
    }

    val datePickerState = rememberDatePickerState()

    Column(
        modifier = modifier
    ) {

        AnimatedVisibility(
            visible = tabVisible) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(id = R.dimen.padding_medium))
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {

                    OutlinedTextField(
                        label = { Text(text = "Document No")},
                        modifier = Modifier
                            .weight(1F),
                        value = transactionState.transactionDetail.documentNumber?:"",
                        onValueChange = {
                            val set = transactionState.transactionDetail.copy(
                                documentNumber = it
                            )
                            viewModel.setTransactionDetail(set)
                        } )

                    Spacer(modifier = Modifier.size(8.dp))

                    Box(modifier = Modifier
                        .weight(1F)){
                        DatePickerRow(selectedDate = selectedDate, info = false) {
                            datePickerDialog = true
                        }
                    }

                }

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {

                    SourceTargetTextField(
                        modifier = Modifier.weight(1F),
                        transactionState = transactionState,
                        sourceTargetSelect = sourceTargetSelect
                    )

                }

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = transactionState.transactionDetail.description?:"",
                    onValueChange = {
                        val set = transactionState.transactionDetail.copy(
                            description = it
                        )
                        viewModel.setTransactionDetail(set)
                    },
                    label =  {Text(stringResource(id = R.string.description)) },
                    singleLine = true,
                    isError = false
                )
            }

        }

        Box(modifier = Modifier
            .background(MaterialTheme.colorScheme.onBackground)
            .fillMaxWidth()
            .height(24.dp)
            .clickable {
                tabVisiblePref = !tabVisiblePref
                tabVisible = !tabVisible

            },
            contentAlignment = Alignment.Center) {
            Icon(
                imageVector = if (tabVisible) { Icons.Rounded.KeyboardArrowUp } else { Icons.Rounded.KeyboardArrowDown },
                contentDescription = "Tab up and down",
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.surface)
        }

        TransactionDataList(
            modifier = Modifier.fillMaxWidth(),
            transactionDataList = transactionState.dataList,
            deleteProduct = viewModel::deleteProductFromList
        )

    }

    when {
        datePickerDialog -> {
            DatePickerDialogScreen(datePickerState, datePickerClosed = {
                viewModel.setTransactionDetail(transactionState.transactionDetail.copy(date = datePickerState.selectedDateMillis?: DateTime.now().millis ))
                datePickerDialog = false
            })

        }
    }




}

@Composable
fun TransactionDataList(
    modifier: Modifier,
    transactionDataList : MutableList<TransactionDataDetail>,
    deleteProduct: (TransactionDataDetail) -> Unit
) {

    if (transactionDataList.isEmpty()) {

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.listEmpty),
                style = MaterialTheme.typography.titleLarge)
            Text(
                text = stringResource(id = R.string.clickToAdd),
                style = MaterialTheme.typography.titleLarge)
        }

    } else {
        LazyColumn(
            modifier = modifier
        ) {
            items(transactionDataList, key = { it.uuid }) {
                TransactionDataItem(
                    modifier = modifier,
                    transactionData = it,
                    deleteProduct = deleteProduct)
            }
        }
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TransactionDataItem(
    modifier: Modifier,
    transactionData: TransactionDataDetail,
    deleteProduct: (TransactionDataDetail) -> Unit
) {

    var askForDelete by remember {
        mutableStateOf(false)
    }
    when {
        askForDelete -> {
            SelfAlertDialogScreen(
                modifier = Modifier,
                onConfirmation = {
                    askForDelete = false
                    deleteProduct(transactionData) },
                dialogTitle = stringResource(id = R.string.wannaDeleteProductTitle),
                dialogText = stringResource(id = R.string.wannaDeleteProductText),
                icon = Icons.Default.Warning,
                dismissButton = {
                    TextButton(
                        onClick = {
                            askForDelete = false
                        }
                    ) {
                        Text(stringResource(id = R.string.dismiss))
                    }
                }
            )

        }
    }

    val density = LocalDensity.current

    val defaultActionSize = 72.dp
    //val actionSizePx = with(density) { defaultActionSize.toPx() }
    val endActionSizePx = with(density) { (defaultActionSize).toPx() }

    val state = remember {
        AnchoredDraggableState(
            initialValue = DragAnchors.Center,
            anchors = DraggableAnchors {
                DragAnchors.Start at 0f
                DragAnchors.Center at 0f
                DragAnchors.End at endActionSizePx
            },
            positionalThreshold = { distance: Float -> distance * 0.5f },
            velocityThreshold = { with(density) { 100.dp.toPx() } },
            animationSpec = tween(),
        )
    }
    DraggableItem(state = state,
        endAction = {
            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .align(Alignment.CenterEnd)
                    .offset {
                        IntOffset(
                            ((-state
                                .requireOffset()) + endActionSizePx)
                                .roundToInt(), 0
                        )
                    }
            )
            {
                DeleteAction(
                    Modifier
                        .width(defaultActionSize)
                        .fillMaxHeight(),
                    deleteUnit = {
                                 //deleteProduct(transactionData)
                                 askForDelete = true
                    },
                )
            }
        }, content = {

            Card(modifier = modifier) {
                ListItem(
                    colors = ListItemDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    ),
                    leadingContent = {
                        ProductImage(transactionData.productUUID)
                    },

                    headlineContent = {
                        ProductNameText(
                            productUUID = transactionData.productUUID,
                            productID = null,
                            modifier = Modifier
                        )
                    },
                    trailingContent = {
                        Text(text = transactionData.piece,
                            style = MaterialTheme.typography.bodyLarge)
                    },

                    )
            }

        })


}