package tr.com.gndg.self.ui.reports.dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import tr.com.gndg.self.R
import tr.com.gndg.self.core.util.customerZero
import tr.com.gndg.self.domain.model.Customer

@Composable
fun OutgoingByCustomerDialog(
    onDismiss: () -> Unit,
    customerList : List<Customer>,
    selectedCustomer : (Customer) -> Unit,
) {


    val context = LocalContext.current
    val withZero = customerList.toMutableList()
    withZero.add(customerZero(context))
    withZero.sortByDescending { it.id }

    Dialog(onDismissRequest = onDismiss) {

        var selected by remember { mutableStateOf(withZero.first { it.id == 0L }) }
        var expanded by remember { mutableStateOf(false) } // initial value

        OutlinedCard(
            modifier = Modifier.clickable {
                expanded = !expanded
            }
        ) {

            Column {

                Row(
                    modifier = Modifier.padding(top = dimensionResource(id = R.dimen.padding_medium)),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top,
                ) {

                    Text(
                        text = selected.name,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                    Icon(Icons.Outlined.ArrowDropDown, null, modifier = Modifier.padding(8.dp))

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.wrapContentWidth()
                    ) {
                        withZero.forEach { listEntry ->

                            DropdownMenuItem(
                                onClick = {
                                    selected = listEntry
                                    expanded = false
                                    //onSelectionChanged(selected)
                                },
                                text = {
                                    Text(
                                        text = listEntry.name,
                                        modifier = Modifier
                                            //.wrapContentWidth()  //optional instad of fillMaxWidth
                                            .fillMaxWidth()
                                            .align(Alignment.Start)
                                    )
                                },
                            )
                        }
                    }

                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss ) {
                        Text(text = stringResource(id = R.string.cancel))
                    }

                    TextButton(
                        onClick = { selectedCustomer(selected) },
                    ) {
                        Text(text = stringResource(id = R.string.ok))
                    }
                }

            }



        }
    }

}