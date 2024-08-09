package tr.com.gndg.self.ui.product.stateScreens

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun EditStatePreview() {
    EditStateScreen(
        modifier = Modifier
    )
}

data class EditScreenItem(
    val headLineText : String,
    val leadingContentIcon : ImageVector,
    val leadingContentDesc : String,
    val leadingContentFunc: () -> Unit,
    val trailingContentIcon: ImageVector,
    val trailingContentDesc : String,
    val trailingContentFunc : ()-> Unit,
    val supportingText : String?,
    val overlineText : String?,
)

val editScreenItemList = listOf(
    EditScreenItem(
        headLineText = "",
        leadingContentIcon = Icons.Rounded.Add,
        leadingContentDesc = "Add Category",
        leadingContentFunc = {  },
        trailingContentIcon = Icons.Filled.Clear,
        trailingContentDesc = "Clear Category",
        trailingContentFunc = { },
        supportingText = null,
        overlineText = "Category"
    ),
    EditScreenItem(
        headLineText = "",
        leadingContentIcon = Icons.Rounded.Add,
        leadingContentDesc = "Add brand",
        leadingContentFunc = {  },
        trailingContentIcon = Icons.Filled.Clear,
        trailingContentDesc = "Clear brand",
        trailingContentFunc = { },
        supportingText = null,
        overlineText = "Brand"
    ),
    EditScreenItem(
        headLineText = "",
        leadingContentIcon = Icons.Rounded.Add,
        leadingContentDesc = "Add size",
        leadingContentFunc = {  },
        trailingContentIcon = Icons.Filled.Clear,
        trailingContentDesc = "Clear size",
        trailingContentFunc = { },
        supportingText = "",
        overlineText = "Size"
    ),
    EditScreenItem(
        headLineText = "",
        leadingContentIcon = Icons.Rounded.Add,
        leadingContentDesc = "Add label",
        leadingContentFunc = {  },
        trailingContentIcon = Icons.Filled.Clear,
        trailingContentDesc = "Clear label",
        trailingContentFunc = { },
        supportingText = null,
        overlineText = "Label"
    )
)


@Composable
fun EditStateScreen(
    modifier: Modifier
    ) {

    BoxWithConstraints(
        modifier = modifier
    ) {
        LazyColumn(
            modifier= Modifier
                .fillMaxSize()
                .height(this.maxHeight)
        ) {

            item {
                Text(text = "Product Features", style = MaterialTheme.typography.labelMedium)
            }

            items(editScreenItemList) {
                ListItem(
                    leadingContent = {
                        FilledTonalIconButton(onClick = { it.leadingContentFunc }) {
                            Icon(it.leadingContentIcon, contentDescription = it.leadingContentDesc)
                        }
                    },
                    headlineContent = {
                        Text(it.headLineText.ifEmpty { "No data assigned" }, style = MaterialTheme.typography.titleMedium)
                    },
                    overlineContent = { Text(text = it.overlineText?:"") },
                    trailingContent = {
                        IconButton(onClick = { it.trailingContentFunc }) {
                            Icon(it.trailingContentIcon, contentDescription = it.trailingContentDesc)
                        }
                    }
                )
            }

        }
    }



}