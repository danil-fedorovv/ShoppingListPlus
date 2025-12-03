package danilfedorov.shared_shoppinglist.presentation.compose.sheet.push_list

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import danilfedorov.common.extension.modifier.noRippleClickable
import danilfedorov.shared_shoppinglist.R

@Composable
fun DeepLinkSheetContent(
    imageResId: Int? = null,
    iconResId: Int? = null,
    title: String,
    description: String? = null,
    buttongColor: Color = colorScheme.surface,
    buttonText: String,
    listName: String? = null,
    isJoining: Boolean = false,
    onClick: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when {
            imageResId != null -> {
                Image(
                    painter = painterResource(imageResId),
                    contentDescription = stringResource(R.string.share_sheet_title),
                    modifier = Modifier.size(96.dp)
                )
            }

            iconResId != null -> {
                Icon(
                    painter = painterResource(iconResId),
                    contentDescription = title,
                    tint = colorScheme.secondary,
                    modifier = Modifier.size(96.dp)
                )
            }
        }
        Spacer(Modifier.height(16.dp))
        Text(
            text = buildAnnotatedString {
                pushStyle(
                    SpanStyle(
                        color = colorScheme.secondary,
                        fontSize = typography.titleMedium.fontSize,
                        fontWeight = typography.titleMedium.fontWeight
                    )
                )
                append(title)
                listName?.let {
                    append(" ")

                    pushStyle(
                        SpanStyle(
                            color = colorScheme.primary,
                            fontSize = typography.titleMedium.fontSize,
                            fontWeight = typography.titleMedium.fontWeight
                        )
                    )
                    append(listName)
                }
            },
            textAlign = TextAlign.Center,
            lineHeight = typography.titleMedium.lineHeight
        )
        description?.let {
            Spacer(Modifier.height(8.dp))
            Text(
                text = it,
                style = typography.titleSmall,
                color = colorScheme.outline,
                textAlign = TextAlign.Center
            )
        }
        Spacer(Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .background(color = buttongColor, shape = shapes.medium)
                .noRippleClickable {
                    onClick()
                },
            contentAlignment = Alignment.Center
        ) {
            if (isJoining) {
                CircularProgressIndicator(
                    color = colorScheme.secondary,
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = buttonText,
                    style = typography.titleLarge,
                    color = colorScheme.secondary
                )
            }
        }
        Spacer(Modifier.height(4.dp))
    }
}