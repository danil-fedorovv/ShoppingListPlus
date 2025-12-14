@file:OptIn(ExperimentalMaterial3Api::class)

package danilfedorov.feature_shoppinglist.internal.presenter.screen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import danilfedorov.common.compose.EmptyContent
import danilfedorov.common.compose.NoRippleInteractionSource
import danilfedorov.feature_shoppinglist.R
import danilfedorov.feature_shoppinglist.internal.compose.ShoppingListTopBar
import danilfedorov.feature_shoppinglist.internal.compose.item.ShoppingListItemContainer
import danilfedorov.feature_shoppinglist.internal.compose.sheet.deeplink.FetchListResultSheet
import danilfedorov.feature_shoppinglist.internal.compose.sheet.deeplink.FetchListSheetState
import danilfedorov.feature_shoppinglist.internal.compose.sheet.options.ListOptionsSheet
import danilfedorov.feature_shoppinglist.internal.presenter.mvi.Action
import danilfedorov.feature_shoppinglist.internal.presenter.mvi.Effect
import danilfedorov.feature_shoppinglist.internal.presenter.mvi.ScreenState
import danilfedorov.shared_shoppinglist.presentation.compose.ActionRow
import danilfedorov.shared_shoppinglist.presentation.compose.sheet.editname.EditShoppingListNameSheet
import danilfedorov.shared_shoppinglist.presentation.compose.sheet.loading.LoadingSheet
import danilfedorov.shared_shoppinglist.presentation.compose.sheet.push_list.PushListResultSheet
import danilfedorov.shared_shoppinglist.presentation.compose.sheet.push_list.PushListSheetState
import danilfedorov.shared_shoppinglist.presentation.compose.sheet.share.ShareListSheet
import danilfedorov.shared_shoppinglist.presentation.mvi.SharedAction
import danilfedorov.shared_shoppinglist.presentation.mvi.SharedScreenState
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@Composable
internal fun ShoppingListScreen(
    screenState: ScreenState,
    sharedScreenState: SharedScreenState,
    action: (Action) -> Unit,
    sharedAction: (SharedAction) -> Unit,
    effect: Flow<Effect>,
) {
    val context = LocalContext.current
    val listState = rememberLazyListState()
    var snackbarJob by remember { mutableStateOf<Job?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val animateSnackBarBottomPadding by animateDpAsState(
        targetValue = if (screenState.multiSelectLists.isNotEmpty()) 64.dp else 0.dp,
        animationSpec = tween(
            durationMillis = 500
        )
    )
    val res = LocalResources.current
    val undoString = stringResource(R.string.undo).uppercase()

    val isScrollingUp by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex == 0
        }
    }

    BackHandler(enabled = screenState.multiSelectLists.isNotEmpty()) {
        action(Action.ClearMultiSelect())
    }

    LaunchedEffect(effect) {
        effect.collect { effect ->

            snackbarHostState.currentSnackbarData?.dismiss()
            snackbarJob?.cancelAndJoin()

            snackbarJob = launch {
                when (effect) {
                    is Effect.ShowSnackbarInfo -> {
                        snackbarHostState.showSnackbar(
                            message = effect.message,
                            duration = SnackbarDuration.Short
                        )
                    }

                    is Effect.ShowSnackbarDelete -> {
                        val result = snackbarHostState.showSnackbar(
                            message = res.getQuantityString(
                                R.plurals.lists_deleted,
                                effect.count,
                                effect.count
                            ),
                            actionLabel = undoString,
                            duration = SnackbarDuration.Short
                        )

                        when (result) {
                            SnackbarResult.ActionPerformed -> action(Action.RestoreState())
                            SnackbarResult.Dismissed -> action(Action.ConfirmDeleteList())
                        }
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            ShoppingListTopBar(
                navFruitAlbum = {
                    snackbarJob?.let {
                        action(Action.ConfirmDeleteList())
                    }
                    action(Action.NavigateFruitAlbum())
                },
                navSubscription = {
                    snackbarJob?.let {
                        action(Action.ConfirmDeleteList())
                    }
                    action(Action.NavigateSubscription())
                },
                navArchive = {
                    snackbarJob?.let {
                        action(Action.ConfirmDeleteList())
                    }
                    action(Action.NavigateArchive())
                },
                navSettings = {
                    snackbarJob?.let {
                        action(Action.ConfirmDeleteList())
                    }
                    action(Action.NavigateSettings())
                })
        },
        floatingActionButton = {
            AnimatedContent(
                targetState = screenState.multiSelectLists.isEmpty(),
                transitionSpec = {
                    slideInVertically(
                        initialOffsetY = { it / 3 }
                    ) + fadeIn() togetherWith
                            slideOutVertically(
                                targetOffsetY = { -it / 3 }
                            ) + fadeOut()
                },
                label = "BottomUpContent"
            ) { visible ->
                if (visible) {

                    ExtendedFloatingActionButton(
                        onClick = { action(Action.ShowAddListSheet()) },
                        icon = {
                            Icon(
                                imageVector = Icons.Rounded.Add,
                                contentDescription = stringResource(R.string.add).uppercase(),
                                modifier = Modifier.size(28.dp),
                                tint = colorScheme.background
                            )
                        },
                        text = {
                            Text(
                                text = stringResource(R.string.new_list).uppercase(),
                                style = typography.titleMedium,
                                color = colorScheme.background
                            )
                        },
                        expanded = isScrollingUp,
                        shape = CircleShape,
                        containerColor = colorScheme.primary,
                        interactionSource = NoRippleInteractionSource
                    )
                }
            }
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 32.dp + animateSnackBarBottomPadding),
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
        bottomBar = {
            Spacer(Modifier.size(0.dp))
        }
    ) { innerPadding ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .padding(innerPadding)
                .background(colorScheme.background)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (screenState.shoppingLists.isEmpty() && !screenState.isLoading) {
                item {
                    EmptyContent(
                        title = stringResource(R.string.empty_lists_title),
                        subtitle = stringResource(R.string.empty_lists_subtitle)
                    )
                }
            }

            items(
                items = screenState.shoppingLists,
                key = { item -> item.id }) { shoppingList ->
                val (selected, total) = screenState.progress[shoppingList.id] ?: (0 to 0)
                ShoppingListItemContainer(
                    modifier = Modifier.animateItem(),
                    shoppingList = shoppingList,
                    selected = selected,
                    total = total,
                    navProduct = { shoppingListId ->
                        snackbarJob?.let {
                            action(Action.ConfirmDeleteList())
                        }
                        action(Action.NavigateProduct(shoppingListId))
                    },
                    shareList = { sharedAction(SharedAction.ShowShareListSheet(shoppingList)) },
                    renameList = { sharedAction(SharedAction.ShowRenameListSheet(shoppingList)) },
                    extendedId = screenState.extendedListId,
                    extendShoppingList = { action(Action.ExtendList(it)) },
                    onClickOptions = { action(Action.ShowListOptions(shoppingList)) },
                    addMultiSelect = { action(Action.AddMultiSelect(shoppingList)) },
                    removeMultiSelect = { action(Action.RemoveMultiSelect(shoppingList)) },
                    isMultiSelectMode = screenState.multiSelectLists.isNotEmpty(),
                    isMultiSelect = screenState.multiSelectLists.any { it.id == shoppingList.id },
                    isLiveEditMode = shoppingList.remoteListId != null,
                    deleteList = { action(Action.DeleteList(shoppingList)) }
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
        if (screenState.isAddListSheetVisible) {
            EditShoppingListNameSheet(
                title = stringResource(R.string.add_new_list),
                titleImageResId = sharedScreenState.renameSheetFruitResId,
                placeholderText = stringResource(R.string.new_list),
                buttonText = stringResource(R.string.add),
                value = screenState.draftListName,
                onValueChange = { action(Action.ChangeDraftListName(it)) },
                onClick = { action(Action.CreateList()) },
                onDismissRequest = { action(Action.HideAddListSheet()) }
            )
        }
        if (sharedScreenState.renamingList != null) {
            EditShoppingListNameSheet(
                value = sharedScreenState.tempListName,
                titleImageResId = sharedScreenState.renameSheetFruitResId,
                onValueChange = { sharedAction(SharedAction.ChangeTempListName(it)) },
                onClick = { sharedAction(SharedAction.SaveListName) },
                onDismissRequest = { sharedAction(SharedAction.HideRenameListSheet) }
            )
        }
        if (screenState.listOptions != null) {
            val listOptions = screenState.listOptions
            ListOptionsSheet(
                listName = listOptions.name,
                onRename = { sharedAction(SharedAction.ShowRenameListSheet(listOptions)) },
                onShare = {
                    action(Action.HideListOptions())
                    sharedAction(SharedAction.ShowShareListSheet(listOptions))
                },
                onDelete = { action(Action.DeleteList(listOptions)) },
                onCopy = { action(Action.CopyList(listOptions)) },
                onDismiss = { action(Action.HideListOptions()) }

            )
        }

        if (sharedScreenState.isShareListSheetVisible) {
            ShareListSheet(
                shareText = {
                    sharedAction(
                        SharedAction.ShareText(
                            context = context,
                            shoppingList = screenState.sharedShoppingList,
                        )
                    )
                },
                shareLink = {
                    sharedAction(
                        SharedAction.ShareLink(
                            context = context,
                            shoppingList = screenState.sharedShoppingList,
                        )
                    )
                },
                liveEditLink = {
                    sharedAction(
                        SharedAction.ShareLiveEditLink(
                            context = context,
                            shoppingList = screenState.sharedShoppingList
                        )
                    )
                },
                dismissRequest = {
                    sharedAction(SharedAction.HideShareListSheet())
                })
        }

        if (screenState.fetchListSheetState is FetchListSheetState.Loading) {
            LoadingSheet(
                title = stringResource(danilfedorov.shared_shoppinglist.R.string.unpacking_list)
            )
        }

        if (screenState.fetchListSheetState !is FetchListSheetState.Hidden &&
            screenState.fetchListSheetState !is FetchListSheetState.Loading
        ) {
            FetchListResultSheet(
                fetchListSheetState = screenState.fetchListSheetState,
                onClickJoin = { action(Action.OnClickJoinOrRetry()) },
                onClickAddList = { action(Action.OnClickAddUnzipList()) },
                onDismissRequest = { action(Action.HideDeepLingSheet) }
            )
        }

        if (sharedScreenState.pushListSheetState !is PushListSheetState.Hidden) {
            PushListResultSheet(
                pushListSheetState = sharedScreenState.pushListSheetState,
                onClickRetry = { context, shoppingList, products ->
                    sharedAction(SharedAction.ShareLiveEditLink(context, shoppingList, products))
                },
                onDismissRequest = { sharedAction(SharedAction.HidePushListSheet) }
            )
        }
        if (sharedScreenState.isLinkLoading) {
            LoadingSheet(
                title = stringResource(danilfedorov.shared_shoppinglist.R.string.packing_list)
            )
        }
        ActionRow(
            modifier = Modifier.padding(innerPadding),
            visible = screenState.multiSelectLists.isNotEmpty(),
            multiSelectCount = screenState.multiSelectLists.size,
            onClickEdit = { sharedAction(SharedAction.ShowRenameListSheet(screenState.multiSelectLists.first())) },
            onClickCopy = { action(Action.MultiCopy()) },
            onClickDelete = { action(Action.DeleteLists(screenState.multiSelectLists)) },
            onDismiss = { action(Action.ClearMultiSelect()) }
        )
    }
}