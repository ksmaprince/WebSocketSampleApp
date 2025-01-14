package com.khun.websocketsample.presentation.ui.widget

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun CircularLoadingView() {
    CircularProgressIndicator(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentWidth(Alignment.CenterHorizontally),
        color = MaterialTheme.colorScheme.onPrimary
    )
}

@Composable
fun LinearLoadingView() {
    LinearProgressIndicator(
        modifier = Modifier
            .fillMaxWidth(),
        color = MaterialTheme.colorScheme.onPrimary
    )
}