package danilfedorov.feature_fruit_album.internal.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import danilfedorov.common.compose.AppTopBar
import danilfedorov.common.core_res.TextWhite
import danilfedorov.common.core_res.fruitsRes
import danilfedorov.common.extension.modifier.noRippleClickable
import danilfedorov.feature_fruit_album.R
import danilfedorov.feature_fruit_album.internal.compose.FruitItem
import danilfedorov.feature_fruit_album.internal.compose.sheet.UnlockNewFruitSheet
import danilfedorov.feature_fruit_album.internal.mvi.Action
import danilfedorov.feature_fruit_album.internal.mvi.Effect
import danilfedorov.feature_fruit_album.internal.mvi.ScreenState
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@Composable
fun FruitAlbumScreen(
    screenState: ScreenState,
    action: (Action) -> Unit,
    effect: Flow<Effect>,
) {

    val fruitFlow = screenState.fruitAlbum.entries.toList()
    val allUnlocked = fruitFlow.all { it.value.first }

    var snackbarJob by remember { mutableStateOf<Job?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val errorString = stringResource(danilfedorov.common.R.string.ad_load_failed)
    val okString = stringResource(danilfedorov.common.R.string.ok).uppercase()

    LaunchedEffect(effect) {
        effect.collect { effect ->
            when (effect) {
                is Effect.FailedToLoadAd -> {

                    snackbarJob?.cancelAndJoin()

                    snackbarJob = launch {
                        val result = snackbarHostState.showSnackbar(
                            message = errorString,
                            actionLabel = okString,
                            duration = SnackbarDuration.Short
                        )

                        if (result == SnackbarResult.ActionPerformed) {
                            snackbarJob?.cancelAndJoin()
                        }
                    }
                }

            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 32.dp),
            ) { data ->
                Snackbar(

                    containerColor = colorScheme.onBackground,
                    shape = shapes.medium,
                    action = {
                        data.visuals.actionLabel?.let { actionLabel ->
                            TextButton(
                                onClick = { data.performAction() }
                            ) {
                                Text(
                                    text = actionLabel,
                                    color = colorScheme.primary,
                                    style = typography.titleLarge
                                )
                            }
                        }
                    }
                ) {
                    Text(
                        data.visuals.message,
                        color = colorScheme.secondary,
                        style = typography.titleMedium
                    )
                }
            }
        },
        topBar = {
            AppTopBar(
                title = stringResource(danilfedorov.common.R.string.title_collection),
                navBack = { action(Action.NavHome) }
            )
        },
        bottomBar = {
            Column(Modifier.padding(16.dp)) {

                val buttonTitleResId = if (allUnlocked) R.string.title_all_unlocked
                else R.string.unlock_random_fruit_title

                val buttonSubtitleResId = if (allUnlocked) R.string.subtitle_all_unlocked
                else danilfedorov.common.R.string.watch_short_ad

                Column(
                    Modifier
                        .fillMaxWidth()
                        .background(color = colorScheme.primary, shape = shapes.medium)
                        .noRippleClickable {
                            if (!allUnlocked) {
                                action(Action.ShowAd)
                            }
                        },
                ) {
                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = stringResource(buttonTitleResId),
                        style = typography.titleLarge,
                        color = TextWhite,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = stringResource(buttonSubtitleResId),
                        style = typography.titleSmall,
                        color = TextWhite,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(8.dp))
                }
            }
        }
    ) { paddingValues ->
        Column(
            Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(danilfedorov.common.R.drawable.img_coop),
                contentDescription = null,
                modifier = Modifier.size(125.dp)
            )
            Text(
                text = stringResource(R.string.cta),
                style = typography.titleMedium,
                color = colorScheme.secondary,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.unlocked),
                    style = typography.titleMedium,
                    color = colorScheme.secondary
                )
                Text(
                    text = "${fruitFlow.count { it.value.first }}/${fruitFlow.count()}",
                    style = typography.titleMedium,
                    color = colorScheme.secondary
                )

            }
            Spacer(Modifier.height(4.dp))

            Box(
                Modifier
                    .fillMaxSize()
                    .background(color = colorScheme.onBackground, shape = shapes.medium),
                contentAlignment = Alignment.Center
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    items(fruitFlow, key = { it.key }) { entry ->
                        val index = entry.key
                        val (isUnlocked, isEnabled) = entry.value

                        val (fruitResId, fruitNameId) = fruitsRes[index]
                        FruitItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f),
                            fruitResId = fruitResId,
                            fruitName = if (isUnlocked) stringResource(fruitNameId) else null,
                            isUnlocked = isUnlocked,
                            isEnabled = isEnabled,
                            enable = {
                                if (isUnlocked) {
                                    val currentlyEnabledCount =
                                        screenState.fruitAlbum.count { it.value.second }
                                    if (!isEnabled || currentlyEnabledCount > 1) {
                                        action(Action.ToggleEnabled(index))
                                    }
                                }
                            })

                    }
                }

            }

        }
        if (screenState.unlockedIndex != null) {
            val (fruitImageResId, fruitNameResId) = fruitsRes[screenState.unlockedIndex]
            UnlockNewFruitSheet(
                fruitResId = fruitImageResId,
                fruitNameResId = fruitNameResId,
                onDismiss = { action(Action.HideUnlockFruitSheet) }
            )
        }
    }
}