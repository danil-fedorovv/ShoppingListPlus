@file:OptIn(ExperimentalMaterial3Api::class)

package danilfedorov.feature_settings.internal.presenter.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import danilfedorov.common.compose.NoRippleInteractionSource
import danilfedorov.common.core_res.colors
import danilfedorov.common.extension.modifier.noRippleClickable
import io.avdev.feature_settings.R
import danilfedorov.feature_settings.internal.compose.IconBox


@Composable
internal fun SettingsScreen(
    darkTheme: Boolean?,
    colorIndex: Int?,
    changeTheme: (Boolean) -> Unit,
    changeColor: (Int) -> Unit,
    navBack: () -> Unit
) {
    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .background(colorScheme.onBackground)
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
                        .noRippleClickable(navBack)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = stringResource(R.string.settings),
                    color = colorScheme.secondary,
                    style = typography.titleLarge
                )
            }
        },
        bottomBar = {
            Spacer(Modifier.size(0.dp))
        }
    ) { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = stringResource(R.string.select_primary_color),
                style = typography.titleLarge,
                color = colorScheme.secondary,
                modifier = Modifier
                    .padding(start = 32.dp, top = 16.dp, bottom = 4.dp)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                colors.chunked(4).forEach { rowColors ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 32.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        rowColors.forEach { color ->
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .padding(top = 16.dp)
                                    .size(56.dp)
                                    .clip(CircleShape)
                                    .border(4.dp, color, CircleShape)
                                    .clickable(
                                        indication = null,
                                        interactionSource = NoRippleInteractionSource
                                    ) {
                                        changeColor(colors.indexOf(color))
                                    }
                            ) {
                                colorIndex?.let {
                                    if (colors[it] == color) {
                                        Box(
                                            modifier = Modifier
                                                .size(36.dp)
                                                .clip(CircleShape)
                                                .background(color)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.app_theme),
                    style = typography.titleLarge,
                    color = colorScheme.secondary,
                    modifier = Modifier
                        .padding(start = 32.dp)
                        .padding(vertical = 16.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 32.dp)
                ) {

                    IconBox(onClick = {
                        changeTheme(false)
                    }, isSelected = darkTheme == false) {
                        Image(
                            painter = painterResource(R.drawable.img_sun),
                            contentDescription = "lightTheme",
                            modifier = Modifier.size(80.dp)
                        )
                    }
                    IconBox(onClick = {
                        changeTheme(true)
                    }, isSelected = darkTheme == true) {
                        Image(
                            painter = painterResource(R.drawable.ic_moon),
                            contentDescription = "darkTheme"
                        )
                    }
                }
            }
        }
    }
}


