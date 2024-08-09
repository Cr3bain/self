package tr.com.gndg.self.ui.warehouses

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import tr.com.gndg.self.R
import tr.com.gndg.self.SelfTopAppBar
import tr.com.gndg.self.domain.model.WarehouseDetail
import tr.com.gndg.self.domain.model.toWarehouseDetail
import tr.com.gndg.self.ui.navigation.NavigationDestination
import tr.com.gndg.self.ui.warehouses.presentation.WarehouseFormViewModel

object WarehouseFormDestination : NavigationDestination {
    override val route = "new_warehouses"
    override val titleRes = R.string.warehouseFormScreen_title
    const val warehouseUUIDArgs = "warehouseUUID"
    val routeWithArgs = "${route}/{$warehouseUUIDArgs}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WarehouseFormScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack : Boolean = true,
    viewModel: WarehouseFormViewModel
){

    LaunchedEffect(key1 = viewModel.repoWarehouseUiState) {
        viewModel.getWarehouse()
    }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            SelfTopAppBar(
                title = stringResource(WarehouseFormDestination.titleRes),
                canNavigateBack = canNavigateBack,
                scrollBehavior = scrollBehavior,
                navigateUp = onNavigateUp,
                navigateBack = {
                               if (viewModel.somethingChanged) {
                                   navigateBack()
                               } else {
                                   navigateBack()
                               }
                },
                action = {
                    IconButton(onClick = {
                        viewModel.saveWarehouse()

                        navigateBack()

                    }) {
                        Icon(imageVector = Icons.Filled.Done, contentDescription = "save")
                        
                    }
                }
            )
        },
    ) {innerPadding->
        WarehouseFormBody(modifier = Modifier
            .padding(innerPadding)
            //.padding(top = 18.dp)
            .fillMaxSize(),
            warehouseDetail = viewModel.warehouseUiState.warehouse.toWarehouseDetail(),
            onValueChange = {
                viewModel.setWarehouseUiState(it)
            }
        )

    }

}

@Composable
fun WarehouseFormBody(
    modifier: Modifier,
    warehouseDetail: WarehouseDetail,
    onValueChange : (WarehouseDetail) -> Unit
){

    Column(modifier= modifier.padding(dimensionResource(id = R.dimen.padding_small))) {

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = warehouseDetail.name,
            onValueChange = { onValueChange(warehouseDetail.copy(name = it) ) },
            label = { Text(stringResource(id = R.string.nameText)) },
            singleLine = true,
            isError = false
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = warehouseDetail.description?:"",
            onValueChange = { onValueChange( warehouseDetail.copy(description = it) ) },
            label = { Text(stringResource(id = R.string.description)) },
            singleLine = true,
            isError = false
        )


    }
}