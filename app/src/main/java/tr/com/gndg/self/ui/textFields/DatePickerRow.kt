package tr.com.gndg.self.ui.textFields

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.joda.time.DateTime
import tr.com.gndg.self.R

@Composable
fun DatePickerRow(
    selectedDate: DateTime,
    info: Boolean,
    dateIconClick : () -> Unit
) {

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        OutlinedTextField(
            modifier = Modifier
                .weight(1F)

            ,
            readOnly = true,
            value = selectedDate.toString("dd.MM.yyyy"),
            onValueChange = {  /*arrivalDetailChange( arrivalProduct.arrivalDetail.copy( date = DateTime(selectedDate).millis) )*/ },
            label = { Text(stringResource(id = R.string.date)) },
            singleLine = true,
            isError = false,
            trailingIcon = {
                IconButton(
                    enabled = !info,
                    onClick = dateIconClick) {
                    Icon(
                        Icons.Rounded.DateRange, "",
                        modifier = Modifier.size(48.dp))
                }
            }
        )

    }

}