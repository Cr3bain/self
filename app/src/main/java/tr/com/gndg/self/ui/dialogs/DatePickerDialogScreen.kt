package tr.com.gndg.self.ui.dialogs

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import tr.com.gndg.self.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialogScreen(
    datePickerState: DatePickerState,
    datePickerClosed : () -> Unit,

) {
    val openDialog = remember { mutableStateOf(true) }
    if (openDialog.value) {

        val confirmEnabled = remember {
            derivedStateOf { datePickerState.selectedDateMillis != null }
        }
        DatePickerDialog(
            onDismissRequest = {
                // Dismiss the dialog when the user clicks outside the dialog or on the back
                // button. If you want to disable that functionality, simply use an empty
                // onDismissRequest.
                openDialog.value = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis
                        datePickerClosed()
                        openDialog.value = false
                    },
                    enabled = confirmEnabled.value
                ) {
                    Text(stringResource(id = R.string.ok))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        datePickerClosed()
                        openDialog.value = false
                    }
                ) {
                    Text(stringResource(id = R.string.cancel))
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}
