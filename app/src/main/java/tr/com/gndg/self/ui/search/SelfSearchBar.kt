package tr.com.gndg.self.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import tr.com.gndg.self.R

@Composable
fun SearchRow(
    onSearch: (String) -> Unit,
    searchTextState: MutableState<String>,
    startBarcodeScanner: () -> Unit,
    showBarcodeScannerIcon: Boolean,
    modifier: Modifier = Modifier,
    hint: String
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = dimensionResource(id = R.dimen.padding_small), end = dimensionResource(
                    id = R.dimen.padding_small
                )
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        SelfSearchBar(
            modifier = modifier.weight(1F),
            hint = hint,
            onSearch = onSearch,
            searchTextState = searchTextState
        )

        if (showBarcodeScannerIcon) {
            IconButton(
                modifier = modifier,
                onClick = startBarcodeScanner) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_qr_code_scanner_24),
                    contentDescription = "Start barcode scanner"
                )
            }
        }

    }
}

@Composable
fun SelfSearchBar(
    modifier: Modifier,
    hint : String = "",
    searchTextState : MutableState<String>,
    onSearch: (String) -> Unit = {}
){

    val isHintDisplayed = remember {
        mutableStateOf(hint != "")
    }

    Box(modifier =  modifier) {

        BasicTextField(value = searchTextState.value, onValueChange ={
            searchTextState.value=it
            onSearch(searchTextState.value)
        },
            maxLines = 1,
            singleLine = true,
            textStyle = TextStyle(color= Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(5.dp, CircleShape)
                .background(Color.White, CircleShape)
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .onFocusChanged {
                    isHintDisplayed.value = it.isFocused != true && searchTextState.value.isEmpty()
                })

        if(isHintDisplayed.value){
            Text(text = hint , color = Color.LightGray, modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp))
        }

    }


}