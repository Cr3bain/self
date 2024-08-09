package tr.com.gndg.self.ui.suppliers

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tr.com.gndg.self.R
import tr.com.gndg.self.SelfTopAppBar
import tr.com.gndg.self.core.util.isValidEmail
import tr.com.gndg.self.domain.model.SupplierDetail
import tr.com.gndg.self.domain.model.toSupplierDetail
import tr.com.gndg.self.ui.navigation.NavigationDestination
import tr.com.gndg.self.ui.suppliers.presentation.SuppliersViewModel

@Composable
@Preview
fun SupplierFormBodyPreview(){
    SupplierFormBody(modifier = Modifier.fillMaxSize(), supplier = SupplierDetail(), onValueChange = {})
}

object SupplierFormDestination : NavigationDestination {
    override val route = "new_supplier"
    override val titleRes = R.string.supplier
    const val supplierUUIDArgs = "supplierUUID"
    val routeWithArgs = "${route}/{$supplierUUIDArgs}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupplierFormScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack : Boolean = true,
    supplierUUID: String?,
    viewModel: SuppliersViewModel
) {

    LaunchedEffect(key1 = supplierUUID){
        if (supplierUUID != null) {
            this.launch(Dispatchers.IO) {
                viewModel.getSupplierByUUID(supplierUUID)
            }

        }
    }

    val supplierUiState = viewModel.supplierUiState
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            SelfTopAppBar(
                title = stringResource(SupplierFormDestination.titleRes),
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
                    AnimatedVisibility(visible = supplierUiState.supplier.id != 0L) {
                        IconButton(onClick = {
                            coroutineScope.launch {
                                viewModel.deleteSupplier()
                                    .onSuccess {
                                        navigateBack()
                                    }
                                    .onFailure {
                                        Log.e("saveSupplier", it.message.toString())
                                    }
                            }

                        },
                        ) {
                            Icon(imageVector = Icons.Filled.Delete, contentDescription = "delete")

                        }
                    }
                    IconButton(onClick = {
                        coroutineScope.launch {
                            viewModel.saveSupplier()
                                .onSuccess {
                                navigateBack()
                            }
                                .onFailure {
                                Log.e("saveSupplier", it.message.toString())
                            }
                        }

                    },
                        enabled = supplierUiState.supplier.name.isNotBlank()
                    ) {
                        Icon(imageVector = Icons.Filled.Done, contentDescription = "save")

                    }
                }
            )
        },
    ) {innerPadding->

        SupplierFormBody(
            modifier = Modifier.padding(innerPadding),
            supplier = supplierUiState.supplier.toSupplierDetail(),
            onValueChange = viewModel::setSupplierUiState
        )

    }

}

@Composable
fun SupplierFormBody(
    modifier: Modifier,
    supplier: SupplierDetail,
    onValueChange : (SupplierDetail) -> Unit
) {

    Column(modifier= modifier.padding(dimensionResource(id = R.dimen.padding_small))) {

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = supplier.name,
            onValueChange = { onValueChange(supplier.copy(name = it) ) },
            label = { Text(stringResource(id = R.string.nameText)) },
            singleLine = true,
            isError = supplier.name.isBlank()
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = supplier.phoneNumber?:"",
            onValueChange = { onValueChange(supplier.copy(phoneNumber = it) ) },
            label = { Text(stringResource(id = R.string.phoneNumber)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            isError = false
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = supplier.email?:"",
            onValueChange = { onValueChange(supplier.copy(email = it) ) },
            label = { Text(stringResource(id = R.string.emailText)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = if (supplier.email != null) { !isValidEmail(supplier.email)
            } else {false }
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = supplier.address?:"",
            onValueChange = { onValueChange(supplier.copy(address = it) ) },
            label = { Text(stringResource(id = R.string.addressText)) },
            singleLine = true,
            isError = false
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = supplier.city?:"",
            onValueChange = { onValueChange(supplier.copy(city = it) ) },
            label = { Text(stringResource(id = R.string.cityText)) },
            singleLine = true,
            isError = false
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = supplier.country?:"",
            onValueChange = { onValueChange(supplier.copy(country = it) ) },
            label = { Text(stringResource(id = R.string.countryText)) },
            singleLine = true,
            isError = false
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = supplier.website?:"",
            onValueChange = { onValueChange(supplier.copy(website = it) ) },
            label = { Text(stringResource(id = R.string.websiteText)) },
            singleLine = true,
            isError = false
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = supplier.description?:"",
            onValueChange = { onValueChange(supplier.copy(description = it) ) },
            label = { Text(stringResource(id = R.string.description)) },
            singleLine = true,
            isError = false
        )
    }

}