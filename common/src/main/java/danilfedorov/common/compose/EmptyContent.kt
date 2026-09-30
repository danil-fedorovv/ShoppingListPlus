package danilfedorov.common.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import danilfedorov.common.R

@Composable
fun EmptyContent(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(Modifier.height(32.dp))
        Image(
            modifier = Modifier.size(128.dp),
            painter = painterResource(id = R.drawable.img_empty_basket),
            contentDescription = null
        )
        Spacer(Modifier.height(8.dp))
        Text(
            modifier = Modifier.padding(horizontal = 24.dp),
            text = title,
            color = colorScheme.secondary,
            style = typography.titleMedium,
            textAlign = TextAlign.Center,
            )
        Spacer(Modifier.height(16.dp))
        Text(
            modifier = Modifier.padding(horizontal = 24.dp),
            text = subtitle,
            color = colorScheme.outline,
            style = typography.titleSmall,
            textAlign = TextAlign.Center
        )
    }
}