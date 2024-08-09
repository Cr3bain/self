package tr.com.gndg.self.ui.animations

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay
import tr.com.gndg.self.ui.scopes.WarehouseStateStockPiece

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MenuAnimation(
    modifier: Modifier,
    menuName: String
) {
    val pagerState = rememberPagerState(pageCount = {
        2
    })

    LaunchedEffect(Unit) {
        while (true) {
            delay(2000)
            val nextPage = (pagerState.currentPage + 1) % pagerState.pageCount
            pagerState.scrollToPage(nextPage)
        }
    }

    
    HorizontalPager(
        state = pagerState,
        modifier = modifier
            .wrapContentSize()
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically) {page->
        when(page) {
            0-> {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = menuName.uppercase(),
                        style = MaterialTheme.typography.titleSmall,
                    )
                }

            }
            1-> {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    WarehouseStateStockPiece(
                        modifier = Modifier,
                        MaterialTheme.typography.titleSmall
                    )
                }
            }
        }
    }
}