package com.khun.websocketsample.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.khun.websocketsample.domain.model.Ticker
import com.khun.websocketsample.presentation.ui.widget.LinearLoadingView
import java.text.DecimalFormat
import com.khun.websocketsample.R
import com.khun.websocketsample.presentation.ui.theme.GradientFirstGreen
import com.khun.websocketsample.presentation.ui.theme.GradientFirstRed
import com.khun.websocketsample.presentation.ui.theme.GradientThird
import com.khun.websocketsample.presentation.ui.theme.Green
import com.khun.websocketsample.presentation.ui.theme.RED

@Composable
fun HomeScreen(padding: PaddingValues, onNavigateToDetail: (String) -> Unit) {
    val viewModel: TickerViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getCryptos()
    }

    Column(
        modifier = Modifier
            .padding(padding)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (uiState.isLoading) {
            LinearLoadingView()
        }
        if (!uiState.isOnline) {
            Text(
                text = stringResource(id = if (uiState.isLoading) R.string.offline else R.string.offline_still),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color(0xffc1f8fe))
                    .padding(vertical = 10.dp),
                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.primary),
                textAlign = TextAlign.Center
            )
        }
        if (uiState.data.isNotEmpty()) {
            TickerList(data = uiState.data, onNavigateToDetail)
        }
        if (uiState.data.isEmpty() && !uiState.isLoading) {
            Image(
                painter = painterResource(id = R.drawable.ic_empty),
                modifier = Modifier
                    .padding(top = 40.dp)
                    .size(250.dp),
                contentDescription = null
            )
            Text(
                text = stringResource(R.string.no_data),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp),
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )
        }
    }

}

@Composable
fun TickerList(data: List<Ticker>, onNavigateToDetail: (String) -> Unit) {
    LazyColumn {
        items(data.sortedBy { ticker -> ticker.productId }) { item ->
            TickerItem(item, onNavigateToDetail)
        }
    }
}

@Composable
fun TickerItem(item: Ticker, onNavigateToDetail: (String) -> Unit) {
    val asc = (item.openPrice < item.price)
    Row(
        modifier = Modifier
            .clickable {
                onNavigateToDetail(item.productId)
            }
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 10.dp)
            .background(
                brush = Brush.horizontalGradient(
                    colors = mutableListOf(
                        GradientThird,
                        if (asc) GradientFirstGreen else GradientFirstRed
                    ),
                ),
                shape = RoundedCornerShape(10.dp)
            )
            .padding(horizontal = 6.dp, vertical = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painterResource(getProductResource(item.productCode)),
            contentDescription = null,
            modifier = Modifier
                .requiredSize(50.dp)
                .clip(shape = RoundedCornerShape(20.dp))

        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                .padding(horizontal = 10.dp)
                .height(50.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxHeight()
            ) {
                Text(
                    text = item.productCode,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Vol. ${DecimalFormat("#.###").format(item.volume24)}",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxHeight()

            ) {
                Text(
                    text = item.price.toString(),
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.bodySmall.copy(
                        textAlign = TextAlign.Right,
                        color = if (asc) Green else RED
                    )
                )
                Text(
                    text = "$${item.openPrice} 24h",
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.Right)
                )
            }


        }

    }
}

fun getProductResource(productCode: String): Int {
    return when (productCode) {
        "BTC" -> R.drawable.ic_bitcoin
        "ETH" -> R.drawable.ic_ethereum
        "ADA" -> R.drawable.ic_cardano
        "LINK" -> R.drawable.ic_chainlink
        "LTC" -> R.drawable.ic_litecoin
        else -> {
            R.drawable.ic_bitcoin
        }
    }
}