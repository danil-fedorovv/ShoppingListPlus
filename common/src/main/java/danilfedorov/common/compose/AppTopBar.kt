package danilfedorov.common.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import danilfedorov.common.extension.modifier.noRippleClickable

@Composable
fun AppTopBar(
    title: String,
    navBack: () -> Unit
) {
    Row(
        modifier = Modifier
            .background(colorScheme.background)
            .statusBarsPadding()
            .fillMaxWidth()
            .height(64.dp)
            .padding(start = 16.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(danilfedorov.common.R.drawable.ic_arrow_back),
            contentDescription = stringResource(danilfedorov.common.R.string.navback),
            tint = colorScheme.secondary,
            modifier = Modifier
                .size(32.dp)
                .noRippleClickable {
                    navBack()
                }
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = title,
            color = colorScheme.secondary,
            style = typography.titleLarge
        )
    }
}