package tr.com.gndg.self.ui.customers

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tr.com.gndg.self.R
import tr.com.gndg.self.SelfTopAppBar
import tr.com.gndg.self.core.util.isValidEmail
import tr.com.gndg.self.domain.model.CustomerDetail
import tr.com.gndg.self.domain.model.toCustomerDetail
import tr.com.gndg.self.ui.customers.presentation.CustomersViewModel
import tr.com.gndg.self.ui.navigation.NavigationDestination

object CustomerFormDestination : NavigationDestination {
    override val route = "new_customer"
    override val titleRes = R.string.customer
    const val customerUUIDArgs = "customerUUID"
    val routeWithArgs = "${route}/{$customerUUIDArgs}"
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerFormScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack : Boolean = true,
    customerUUID : String?,
    viewModel : CustomersViewModel
){

    LaunchedEffect(key1 = customerUUID) {
        if (customerUUID != null) {
            this.launch(Dispatchers.IO) {
                viewModel.getCustomerByUUID(customerUUID)
            }
        }
    }

    val customerUiState = viewModel.customerUiState
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            SelfTopAppBar(
                title = stringResource(CustomerFormDestination.titleRes),
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
                    AnimatedVisibility(visible = customerUiState.customer.id != 0L) {
                        IconButton(onClick = {
                            coroutineScope.launch {
                                viewModel.deleteCustomer()
                                    .onSuccess {
                                        navigateBack()
                                    }
                                    .onFailure {
                                        Log.e("deleteCustomer", it.message.toString())
                                    }
                            }

                        },
                        ) {
                            Icon(imageVector = Icons.Filled.Delete, contentDescription = "delete")

                        }
                    }
                    IconButton(onClick = {
                        coroutineScope.launch {
                            viewModel.saveCustomer()
                                .onSuccess {
                                    navigateBack()
                                }
                                .onFailure {
                                    Log.e("saveSupplier", it.message.toString())
                                }
                        }

                    },
                        enabled = customerUiState.customer.name.isNotBlank()
                    ) {
                        Icon(imageVector = Icons.Filled.Done, contentDescription = "save")

                    }
                }
            )
        },
    ) {innerPadding->

        CustomerFormBody(
            modifier = Modifier.padding(innerPadding),
            customer = customerUiState.customer.toCustomerDetail(),
            onValueChange = viewModel::setCustomerUiState
        )

    }

}

@Composable
fun CustomerFormBody(
    modifier: Modifier,
    customer: CustomerDetail,
    onValueChange : (CustomerDetail) -> Unit
) {

    Column(modifier= modifier.padding(dimensionResource(id = R.dimen.padding_small))) {

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = customer.name,
            onValueChange = { onValueChange(customer.copy(name = it) ) },
            label = { Text(stringResource(id = R.string.nameText)) },
            singleLine = true,
            isError = customer.name.isBlank()
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = customer.phoneNumber?:"",
            onValueChange = { onValueChange(customer.copy(phoneNumber = it) ) },
            label = { Text(stringResource(id = R.string.phoneNumber)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            isError = false
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = customer.email?:"",
            onValueChange = { onValueChange(customer.copy(email = it) ) },
            label = { Text(stringResource(id = R.string.emailText)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = if (customer.email != null) { !isValidEmail(customer.email)
            } else { false }
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = customer.address?:"",
            onValueChange = { onValueChange(customer.copy(address = it) ) },
            label = { Text(stringResource(id = R.string.addressText)) },
            singleLine = true,
            isError = false
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = customer.city?:"",
            onValueChange = { onValueChange(customer.copy(city = it) ) },
            label = { Text(stringResource(id = R.string.cityText)) },
            singleLine = true,
            isError = false
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = customer.country?:"",
            onValueChange = { onValueChange(customer.copy(country = it) ) },
            label = { Text(stringResource(id = R.string.countryText)) },
            singleLine = true,
            isError = false
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = customer.website?:"",
            onValueChange = { onValueChange(customer.copy(website = it) ) },
            label = { Text(stringResource(id = R.string.websiteText)) },
            singleLine = true,
            isError = false
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = customer.description?:"",
            onValueChange = { onValueChange(customer.copy(description = it) ) },
            label = { Text(stringResource(id = R.string.description)) },
            singleLine = true,
            isError = false
        )
    }

}