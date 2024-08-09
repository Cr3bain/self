package tr.com.gndg.self.ui.dialogs

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import tr.com.gndg.self.R


@Composable
fun SelfAlertDialogScreen(
    modifier: Modifier,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
    dismissButton: @Composable (() -> Unit)?
) {
    AlertDialog(
        icon = {
            Icon(icon, contentDescription = "Alert Dialog Icon")
        },
        title = {
            Text(text = dialogTitle, modifier = modifier)
        },
        text = {
            Text(text = dialogText, modifier = modifier)
        },
        onDismissRequest = {

        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text(
                    text = stringResource(id = R.string.confirm),
                    modifier = modifier)
            }
        },
        dismissButton = {
            dismissButton?.let {
                dismissButton()
            }
        }
    )
}