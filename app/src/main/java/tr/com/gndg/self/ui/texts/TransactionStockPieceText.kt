package tr.com.gndg.self.ui.texts

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import tr.com.gndg.self.domain.model.transactions.TransactionData

@Composable
fun TransactionStockPieceText(
    modifier: Modifier,
    textStyle: TextStyle,
    transactionDataList : List<TransactionData>?
) {

    var piece by remember {
        mutableFloatStateOf(0F)
    }

    var pieceCount = 0F

    transactionDataList?.forEach {
        pieceCount += it.piece
    }

    piece = pieceCount

    Text(
        style = textStyle,
        modifier = modifier,
        text = piece.toString()
    )

}