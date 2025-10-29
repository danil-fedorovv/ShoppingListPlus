package danilfedorov.feature_productlist.internal.presenter.screen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.avdev.productlist.R
import danilfedorov.feature_productlist.internal.compose.topbar.ProductListTopBar
import danilfedorov.feature_productlist.internal.compose.topbar.ProgressIndicator
import danilfedorov.feature_productlist.internal.compose.hide_selected_row.HideSelectedRow
import danilfedorov.feature_productlist.internal.compose.TitledContent
import danilfedorov.feature_productlist.internal.compose.item.ProductListItem
import danilfedorov.feature_productlist.internal.compose.sheet.edit_product.EditProductBottomSheet
import danilfedorov.feature_productlist.internal.presenter.mvi.Action
import danilfedorov.feature_productlist.internal.presenter.mvi.Action.ConfirmDeleteProduct
import danilfedorov.feature_productlist.internal.presenter.mvi.Action.RestoreState
import danilfedorov.feature_productlist.internal.presenter.mvi.Effect
import danilfedorov.feature_productlist.internal.presenter.mvi.ScreenState
import kotlinx.coroutines.flow.Flow
import androidx.compose.ui.platform.LocalResources
import danilfedorov.common.compose.ActionRow
import danilfedorov.common.compose.EditShoppingListNameSheet
import danilfedorov.common.extension.modifier.noRippleClickable
import danilfedorov.feature_productlist.internal.compose.hide_selected_row.FloatingHideSelected
import danilfedorov.feature_productlist.internal.compose.item.ProductSwipeContainer
import danilfedorov.feature_productlist.internal.compose.sheet.options.OptionsSheet
import danilfedorov.feature_productlist.internal.compose.search.ProductSearchRow
import danilfedorov.feature_productlist.internal.compose.search.SearchInfoColumn
import danilfedorov.feature_productlist.internal.compose.sheet.copy.CopyProductsSheet
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ProductListScreen(
    screenState: ScreenState,
    action: (Action) -> Unit,
    effect: Flow<Effect>
) {
    val listState = rememberLazyListState()
    var snackbarJob by remember { mutableStateOf<Job?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val animateSnackBarBottomPadding by animateDpAsState(
        targetValue = if (screenState.multiSelectProducts.isNotEmpty()) 64.dp else 0.dp,
        animationSpec = tween(
            durationMillis = 500
        )
    )

    val context = LocalContext.current
    val res = LocalResources.current
    val undoString = stringResource(R.string.undo).uppercase()
    val headerKey = "header_key"

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val selectedIdsSet by remember(screenState.selectedProducts) {
        derivedStateOf { screenState.selectedProducts.map { it.id }.toSet() }
    }

    val visiblePair by remember(listState) {
        derivedStateOf {
            val infos = listState.layoutInfo.visibleItemsInfo

            val headerVisible = infos.any { it.key == headerKey }
            if (headerVisible) {
                emptySet<Long>() to true
            } else {
                val visibleIds = infos.mapNotNull { it.key as? Long }.toSet()
                visibleIds to false
            }
        }
    }

    val (visibleIdsSet, isHeaderVisible) = visiblePair

    val shouldShowFloatingHideSelected by remember(
        visibleIdsSet,
        selectedIdsSet,
        isHeaderVisible,
        screenState.searchMode
    ) {
        derivedStateOf {
            when {
                isHeaderVisible -> false
                selectedIdsSet.isEmpty() -> false
                screenState.searchMode -> false
                else -> visibleIdsSet.any { it in selectedIdsSet }
            }
        }
    }

    LaunchedEffect(effect) {
        effect.collect { effect ->
            when (effect) {
                is Effect.ShowSnackbarInfo -> {

                    snackbarJob?.cancelAndJoin()

                    snackbarJob = launch {
                        snackbarHostState.showSnackbar(
                            message = effect.message,
                            duration = SnackbarDuration.Short
                        )
                    }
                }

                is Effect.ShowSnackbarDelete -> {

                    snackbarJob?.let {
                        action(ConfirmDeleteProduct())
                        it.cancelAndJoin()
                    }


                    snackbarJob = launch {
                        val result = snackbarHostState.showSnackbar(
                            message = res.getQuantityString(
                                R.plurals.items_deleted,
                                effect.count,
                                effect.count
                            ),
                            actionLabel = undoString,
                            duration = SnackbarDuration.Short
                        )

                        if (result == SnackbarResult.ActionPerformed) {
                            action(RestoreState())
                        } else {
                            action(ConfirmDeleteProduct())
                        }
                    }
                }
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            action(ConfirmDeleteProduct())
        }
    }

    BackHandler(enabled = screenState.multiSelectProducts.isNotEmpty()) {
        action(Action.ClearMultiSelect())
    }
    BackHandler(enabled = screenState.searchMode) {
        action(Action.DisableSearchMode())
    }

    val progress by remember(screenState.products) {
        derivedStateOf {
            val total = screenState.products.size
            if (total == 0) 0f
            else screenState.products.count { it.isSelected }.toFloat() / total.toFloat()
        }
    }

    Scaffold(
        topBar = {
            if (screenState.searchMode) {
                ProductSearchRow(
                    searchValue = screenState.searchValue,
                    onChangeSearchValue = { action(Action.OnChangeSearchValue(it)) },
                    isMultiSelect = screenState.multiSelectProducts.isNotEmpty(),
                    navBack = {
                        action(Action.DisableSearchMode())
                        coroutineScope.launch {
                            listState.animateScrollToItem(index = 0)
                        }
                    },
                    disableMultiSelect = { action(Action.ClearMultiSelect()) }
                )
            } else {
                Column(Modifier.fillMaxWidth()) {
                    ProductListTopBar(
                        isMultiSelect = screenState.multiSelectProducts.isNotEmpty(),
                        shoppingListName = screenState.shoppingListName,
                        navHome = { action(Action.NavigateHome()) },
                        onClickOptions = { action(Action.ShowOptionsSheet()) },
                        scrollBehavior = scrollBehavior,
                        draftAddProduct = screenState.draftAddProduct,
                        onChangeAddProduct = { action(Action.OnChangeAddProduct(it)) },
                        addProduct = { action(Action.AddProduct()) },
                        disableMultiSelect = { action(Action.ClearMultiSelect()) }
                    )
                    ProgressIndicator(progress)
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
                .fillMaxSize()
                .background(colorScheme.background)
                .nestedScroll(scrollBehavior.nestedScrollConnection)
        ) {
            if (screenState.products.isEmpty() && !screenState.isLoading) {
                item {
                    TitledContent(
                        modifier = Modifier.animateItem(),
                        title = stringResource(R.string.list_is_still_empty),
                        subtitle = stringResource(R.string.shopping_list_done)
                    )
                }
            }

            if (screenState.products.isNotEmpty() &&
                screenState.products.size == screenState.selectedProducts.size &&
                !screenState.searchMode && !screenState.isLoading
            ) {
                item {
                    TitledContent(
                        modifier = Modifier.animateItem(),
                        title = stringResource(R.string.all_goods_purchased),
                        subtitle = stringResource(R.string.shopping_list_done)
                    )
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = colorScheme.surface,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }


            itemsIndexed(
                items = screenState.unselectedProducts,
                key = { index, item -> item.id }
            ) { index, product ->
                ProductSwipeContainer(
                    item = product,
                    onDelete = { action(Action.DeleteProduct(product)) },
                    onSelect = { action(Action.ToggleSelect(product)) },
                    modifier = Modifier.animateItem(),
                ) {
                    Column {
                        ProductListItem(
                            product = product,
                            action = action,
                            backgroundColor = colorScheme.onBackground,
                            screenState = screenState,
                            textColor = colorScheme.secondary,
                            isInMultiSelect = screenState.multiSelectProducts.any { it.id == product.id }
                        )
                        HorizontalDivider(
                            thickness = 1.dp,
                            color = colorScheme.surface,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            if (screenState.selectedProducts.isNotEmpty() && !screenState.searchMode) {
                item(key = headerKey) {
                    HideSelectedRow(
                        Modifier
                            .animateItem()
                            .noRippleClickable { action(Action.ToggleHideSelectedRowExpanded()) },
                        screenState.isSelectedListExpanded,
                        screenState.selectedProducts.size
                    )
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = colorScheme.surface,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }



            if (screenState.isSelectedListExpanded || screenState.searchMode) {
                itemsIndexed(
                    items = screenState.selectedProducts,
                    key = { index, item -> item.id }
                ) { index, product ->
                    ProductSwipeContainer(
                        item = product,
                        onDelete = { action(Action.DeleteProduct(product)) },
                        onSelect = { action(Action.ToggleSelect(product)) },
                        modifier = Modifier.animateItem(),
                    ) {
                        ProductListItem(
                            product = product,
                            action = action,
                            backgroundColor = colorScheme.background,
                            textColor = colorScheme.outline,
                            screenState = screenState,
                            isInMultiSelect = screenState.multiSelectProducts.any { it.id == product.id }
                        )
                    }
                }
            }
            if (screenState.searchMode && screenState.searchValue.isNotEmpty()) {
                item(key = "search info") {
                    SearchInfoColumn(
                        filteredCount = screenState.filteredCount,
                        itemsCount = screenState.products.size,
                        onClickClear = { action(Action.OnChangeSearchValue("")) }
                    )
                }
            }
            item(key = "spacer") {
                Spacer(
                    Modifier
                        .height(200.dp)
                        .animateItem()
                )
            }
        }

        FloatingHideSelected(
            modifier = Modifier.padding(innerPadding),
            isVisible = shouldShowFloatingHideSelected,
            onClick = { action(Action.ToggleHideSelectedRowExpanded()) }
        )


        ActionRow(
            modifier = Modifier.padding(innerPadding),
            visible = screenState.multiSelectProducts.isNotEmpty(),
            multiSelectCount = screenState.multiSelectProducts.size,
            onClickEdit = { action(Action.InitEditProduct(screenState.multiSelectProducts[0])) },
            onClickCopy = { action(Action.ShowCopySheet()) },
            onClickDelete = { action(Action.MultiDelete()) },
            onDismiss = { action(Action.ClearMultiSelect()) }
        )

        val editingProduct = screenState.editingProductId?.let { id ->
            screenState.products.find { it.id == id }
        }
        if (editingProduct != null) {
            EditProductBottomSheet(
                draftName = screenState.draftProductName,
                draftAmount = screenState.draftAmount,
                draftUnit = screenState.draftUnit,
                onChangeName = { action(Action.OnChangeProductName(it)) },
                onChangeAmount = { action(Action.OnChangeAmount(it)) },
                onChangeUnit = { action(Action.OnChangeUnit(it)) },
                incAmount = { action(Action.IncAmount()) },
                decAmount = { action(Action.DecAmount()) },
                onDismiss = {
                    action(Action.EditProduct(editingProduct.id))
                }
            )
        }
        if (screenState.isOptionsSheetVisible) {
            OptionsSheet(
                search = { action(Action.EnableSearchMode()) },
                shareList = { action(Action.ShareShoppingList(context)) },
                renameList = { action(Action.ShowRenameListSheet()) },
                selectAll = { action(Action.SelectAll()) },
                clearSelection = { action(Action.ClearSelection()) },
                deleteAllSelected = { action(Action.DeleteAllSelected()) },
                onDismissRequest = { action(Action.DismissOptionsSheet()) },
                isCheckedSort = screenState.isSortByAlphabet,
                switchSort = { action(Action.SetSortByAlphabet(it)) }
            )
        }
        if (screenState.isRenameListVisible) {
            EditShoppingListNameSheet(
                value = screenState.draftShoppingListName,
                onValueChange = { action(Action.OnChangeShoppingListName(it)) },
                onClick = { action(Action.SaveNewShoppingListName()) },
                onDismissRequest = { action(Action.DismissRenameListSheet()) }
            )
        }

        if (screenState.isCopySheetVisible) {
            CopyProductsSheet(
                shoppingListProgress = screenState.shoppingListProgress,
                copyItems = { action(Action.CopyProducts(it)) },
                createNewListAndCopy = { action(Action.CreateNewListAndCopy()) },
                onDismiss = { action(Action.DismissCopySheet()) }
            )
        }
    }
}