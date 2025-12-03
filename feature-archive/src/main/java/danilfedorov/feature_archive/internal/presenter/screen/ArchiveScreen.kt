package danilfedorov.feature_archive.internal.presenter.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import danilfedorov.archive.R
import danilfedorov.common.compose.EmptyContent
import danilfedorov.feature_archive.internal.compose.ArchiveItem
import danilfedorov.feature_archive.internal.compose.ArchiveProductsSheet
import danilfedorov.feature_archive.internal.compose.ArchiveTopBar
import danilfedorov.feature_archive.internal.presenter.mvi.Action
import danilfedorov.feature_archive.internal.presenter.mvi.Effect
import danilfedorov.feature_archive.internal.presenter.mvi.ScreenState
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@Composable
fun ArchiveScreen(
    screenState: ScreenState,
    action: (Action) -> Unit,
    effect: Flow<Effect>,
) {
    var snackbarJob by remember { mutableStateOf<Job?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val undoString = stringResource(R.string.undo).uppercase()

    LaunchedEffect(effect) {
        effect.collect { effect ->

            snackbarHostState.currentSnackbarData?.dismiss()
            snackbarJob?.cancelAndJoin()

            snackbarJob = launch {
                when (effect) {
                    is Effect.ShowSnackbarDelete -> {
                        val result = snackbarHostState.showSnackbar(
                            message = effect.message,
                            actionLabel = undoString,
                            duration = SnackbarDuration.Short
                        )

                        if (result == SnackbarResult.ActionPerformed) {
                            action(Action.RestoreState())
                        } else {
                            action(Action.ConfirmDeleteArchive())
                        }
                    }
                }

            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            action(Action.ConfirmDeleteArchive())
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorScheme.background),
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
                    modifier = Modifier.height(56.dp),
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
            ArchiveTopBar(
                navHome = { action(Action.NavHome()) },
                deleteAll = { action(Action.DeleteAll()) },
                showDeleteIcon = screenState.archiveLists.isNotEmpty()
            )
        },
        bottomBar = {
            Spacer(Modifier.size(0.dp))
        }
    ) { innerPadding ->

        LazyColumn(
            Modifier
                .padding(innerPadding)
        ) {

            if (screenState.archiveLists.isEmpty() && !screenState.isLoading) {
                item {
                    EmptyContent(
                        title = stringResource(R.string.archive_empty_title),
                        subtitle = stringResource(R.string.archive_empty_subtitle)
                    )
                }
            }
            items(
                items = screenState.archiveLists,
                key = { item -> item.id }
            ) { archive ->
                ArchiveItem(
                    modifier = Modifier
                        .animateItem()
                        .padding(horizontal = 16.dp),
                    listName = archive.name,
                    progress = screenState.progress[archive.id] ?: "",
                    deleteAt = archive.deletedAt ?: 0L,
                    onClick = { action(Action.ShowProductsSheet(archive)) },
                    onDelete = { action(Action.DeleteArchive(archive)) },
                    onRestore = { action(Action.RestoreArchive(archive)) }
                )
            }
            item {
                Spacer(
                    Modifier
                        .height(200.dp)
                        .animateItem()
                )
            }
        }
    }
    if (screenState.isArchiveProductsExpanded != null) {
        val archive = screenState.isArchiveProductsExpanded
        ArchiveProductsSheet(
            archiveName = archive.name,
            productList = screenState.productList,
            onDismiss = { action(Action.DismissProductsSheet()) },
            onDelete = { action(Action.DeleteArchive(archive)) },
            onRestore = { action(Action.RestoreArchive(archive)) }
        )
    }
}