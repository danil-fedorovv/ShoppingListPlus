package danilfedorov.common.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(32.dp))
        Image(
            painter = painterResource(id = R.drawable.abc),
            contentDescription = null
        )
        Spacer(Modifier.height(32.dp))
        Text(
            text = title,
            color = colorScheme.secondary,
            style = typography.titleMedium,
            textAlign = TextAlign.Center,
            )
        Spacer(Modifier.height(16.dp))
        Text(
            text = subtitle,
            color = colorScheme.outline,
            style = typography.titleSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}