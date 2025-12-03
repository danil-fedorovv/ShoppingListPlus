package danilfedorov.feature_productlist.internal.presenter.viewmodel

import android.content.res.Resources
import androidx.lifecycle.viewModelScope
import danilfedorov.domain_core.model.Product
import danilfedorov.domain_core.model.ShoppingList
import danilfedorov.domain_core.usecase.fruitalbum.GetRandomActiveUnlockedFruitUseCase
import danilfedorov.domain_core.usecase.product.DeleteProductsUseCase
import danilfedorov.domain_core.usecase.product.GetShoppingListProgressUseCase
import danilfedorov.domain_core.usecase.product.InsertProductUseCase
import danilfedorov.domain_core.usecase.product.ObserveConnectionStatusUseCase
import danilfedorov.domain_core.usecase.product.ObserveProductListByListIdUseCase
import danilfedorov.domain_core.usecase.product.ObserveRemoteProductChangesUseCase
import danilfedorov.domain_core.usecase.product.SelectAllProductsUseCase
import danilfedorov.domain_core.usecase.product.UnselectAllProductsUseCase
import danilfedorov.domain_core.usecase.product.UpdateProductCardUseCase
import danilfedorov.domain_core.usecase.product.UpdateProductSelectionUseCase
import danilfedorov.domain_core.usecase.shoppinglist.ChangeSortByAlphabetUseCase
import danilfedorov.domain_core.usecase.shoppinglist.CreateListUseCase
import danilfedorov.domain_core.usecase.shoppinglist.GetShoppingListsUseCase
import danilfedorov.domain_core.usecase.shoppinglist.ObserveRemoteListNameUseCase
import danilfedorov.domain_core.usecase.shoppinglist.ObserveShoppingListByIdUseCase
import danilfedorov.domain_core.usecase.shoppinglist.PushListUseCase
import danilfedorov.domain_core.usecase.shoppinglist.RenameShoppingListUseCase
import danilfedorov.feature_productlist.internal.presenter.mvi.Action
import danilfedorov.feature_productlist.internal.presenter.mvi.Effect
import danilfedorov.feature_productlist.internal.presenter.mvi.ScreenState
import danilfedorov.productlist.R
import danilfedorov.shared_shoppinglist.presentation.viewmodel.BaseListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class ProductsViewModel(
    private val shoppingListId: Long,
    private val onNavigateHome: () -> Unit,
    private val res: Resources,
    private val observeProductListByListIdUseCase: ObserveProductListByListIdUseCase,
    private val insertProductUseCase: InsertProductUseCase,
    private val updateProductSelectionUseCase: UpdateProductSelectionUseCase,
    private val selectAllProductsUseCase: SelectAllProductsUseCase,
    private val unselectAllProductsUseCase: UnselectAllProductsUseCase,
    private val updateProductCardUseCase: UpdateProductCardUseCase,
    private val deleteProductsUseCase: DeleteProductsUseCase,
    private val changeSortByAlphabetUseCase: ChangeSortByAlphabetUseCase,
    private val getShoppingListProgressUseCase: GetShoppingListProgressUseCase,
    private val createListUseCase: CreateListUseCase,
    private val observeShoppingListByIdUseCase: ObserveShoppingListByIdUseCase,
    private val getShoppingListsUseCase: GetShoppingListsUseCase,
    private val observeRemoteProductChangesUseCase: ObserveRemoteProductChangesUseCase,
    private val observeConnectionStatusUseCase: ObserveConnectionStatusUseCase,
    private val observeRemoteListNameUseCase: ObserveRemoteListNameUseCase,
    pushListUseCase: PushListUseCase,
    renameShoppingListUseCase: RenameShoppingListUseCase,
    getRandomActiveUnlockedFruitUseCase: GetRandomActiveUnlockedFruitUseCase
) : BaseListViewModel(
    pushListUseCase,
    renameShoppingListUseCase,
    getRandomActiveUnlockedFruitUseCase
) {

    private val _screenState = MutableStateFlow(ScreenState())
    val screenState = _screenState.asStateFlow()

    private val _effect = MutableSharedFlow<Effect>()
    val effect = _effect.asSharedFlow()

    private val recentlyDeletedSet = MutableStateFlow<Set<Product>>(emptySet())

    private var remoteListId: String? = null

    init {
        viewModelScope.launch {
            observeProductListByListIdUseCase(shoppingListId)
                .combine(observeShoppingListByIdUseCase(shoppingListId)) { products, shoppingList ->
                    remoteListId = shoppingList.remoteListId
                    shoppingList to sortProducts(products, shoppingList)
                }
                .combine(recentlyDeletedSet) { (shoppingList, sortedProducts), deletedSet ->
                    val visibleProducts = sortedProducts.filter { product ->
                        product !in deletedSet
                    }
                    shoppingList to visibleProducts
                }
                .combine(_screenState.map { it.searchValue.trim() }
                    .distinctUntilChanged()) { (shoppingList, products), query ->
                    shoppingList to products.filter { it.name.contains(query, ignoreCase = true) }
                }
                .collect { (shoppingList, filteredTriple) ->
                    updateScreenState(shoppingList, filteredTriple)
                    _screenState.update { it.copy(shoppingList = shoppingList) }
                    shoppingList.remoteListId?.let {
                        launch {
                            observeRemoteListNameUseCase(it).collect {}
                        }
                        launch {
                            observeRemoteProductChangesUseCase(it, shoppingListId).collect {}
                        }
                        launch {
                            observeConnectionStatusUseCase().collect { connectionStatus ->
                                withContext(Dispatchers.Main) {
                                    _screenState.update {
                                        it.copy(connectionStatus = connectionStatus)
                                    }
                                }
                            }
                        }
                    }
                }
        }
    }

    private fun sortProducts(products: List<Product>, shoppingList: ShoppingList): List<Product> {
        return if (shoppingList.isSortedByAlphabet) {
            products.sortedBy { it.name.lowercase() }
        } else {
            products.sortedBy { it.roomId }
        }
    }

    private fun updateScreenState(
        shoppingList: ShoppingList,
        products: List<Product>,
    ) {
        _screenState.update {
            it.copy(
                products = products,
                shoppingListName = shoppingList.name,
                isSortByAlphabet = shoppingList.isSortedByAlphabet,
                filteredCount = products.size,
                isLoading = false
            )
        }
    }

    fun action(action: Action) {
        when (action) {
            is Action.InitEditProduct -> initEditProduct(action.product)
            is Action.EditProduct -> editProduct(action.product)

            is Action.OnChangeProductName -> changeProductName(action.newValue)
            is Action.OnChangeAmount -> changeAmount(action.newValue)
            is Action.IncAmount -> incAmount()
            is Action.DecAmount -> decAmount()
            is Action.OnChangeUnit -> changeUnit(action.newValue)
            is Action.OnChangeAddProduct -> changeAddProduct(action.newValue)

            is Action.ToggleSelect -> toggleSelect(action.product)
            is Action.AddProduct -> addProduct()
            is Action.DeleteProduct -> deleteProduct(action.product)
            is Action.RestoreState -> restoreState()
            is Action.ConfirmDeleteProduct -> confirmDelete()
            is Action.ToggleHideSelectedRowExpanded -> toggleSelectedListExpanded()

            is Action.NavigateHome -> onNavigateHome()

            is Action.AddToMultiSelect -> addToMultiSelect(action.product)
            is Action.DeleteFromMultiSelect -> deleteFromMultiSelect(action.product)
            is Action.ClearMultiSelect -> clearMultiSelect()
            is Action.MultiDelete -> multiDelete()
            is Action.ConfirmMultiDelete -> confirmMultiDelete()

            is Action.ShowOptionsSheet -> showOptionsSheet()
            is Action.DismissOptionsSheet -> dismissOptionsSheet()
            is Action.SelectAll -> selectAll()
            is Action.ClearSelection -> clearSelection()
            is Action.DeleteAllSelected -> deleteAllSelected()
            is Action.EnableSearchMode -> enableSearchMode()
            is Action.DisableSearchMode -> disableSearchMode()
            is Action.OnChangeSearchValue -> onChangeSearchValue(action.newValue)
            is Action.SetSortByAlphabet -> setSortByAlphabet(action.checked)

            is Action.ShowCopySheet -> showCopySheet()
            is Action.DismissCopySheet -> dismissCopySheet()
            is Action.CopyProducts -> copyProducts(action.shoppingListId)
            is Action.CreateNewListAndCopy -> createNewListAndCopy()
        }
    }

    private fun deleteProduct(product: Product) {
        viewModelScope.launch {
            if (recentlyDeletedSet.value.isNotEmpty()) {
                deleteProductsUseCase(remoteListId, recentlyDeletedSet.value.toList())
            }
            recentlyDeletedSet.value = setOf(product)
            _effect.emit(Effect.ShowSnackbarDelete(1))
        }
    }

    private fun restoreState() {
        recentlyDeletedSet.value = emptySet()
    }

    private fun confirmDelete() {
        if (recentlyDeletedSet.value.isNotEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                deleteProductsUseCase(remoteListId, recentlyDeletedSet.value.toList())
                recentlyDeletedSet.value = emptySet()

            }
        }
    }

    private fun addProduct() {
        viewModelScope.launch {
            val productName = _screenState.value.draftAddProduct.trim()

            if (productName.isEmpty()) {
                _screenState.update { it.copy(draftAddProduct = "") }
                return@launch
            }

            val product = Product(
                listId = shoppingListId,
                name = productName
            )

            withContext(Dispatchers.IO) {
                insertProductUseCase(remoteListId, product)
            }

            _screenState.update { it.copy(draftAddProduct = "") }
        }
    }


    private fun toggleSelect(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            updateProductSelectionUseCase(remoteListId, product, !product.isSelected)
        }
    }

    private fun initEditProduct(product: Product) {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                _screenState.update {
                    it.copy(
                        editingProductId = product.roomId,
                        draftProductName = product.name,
                        draftAmount = product.amount,
                        draftUnit = product.unit
                    )
                }
            }
            clearMultiSelect()
        }
    }

    private fun editProduct(product: Product) {
        viewModelScope.launch {
            val currentState = _screenState.value
            updateProductCardUseCase(
                remoteListId = remoteListId,
                product = product,
                name = currentState.draftProductName.trim(),
                amount = currentState.draftAmount.trim(),
                unit = currentState.draftUnit.trim()
            )
            _screenState.update { it.copy(editingProductId = null) }
        }
    }

    private fun changeAddProduct(newValue: String) {
        viewModelScope.launch(Dispatchers.Main) {
            _screenState.update { it.copy(draftAddProduct = newValue) }
        }
    }

    private fun changeProductName(newValue: String) {
        viewModelScope.launch(Dispatchers.Main) {
            _screenState.update { it.copy(draftProductName = newValue) }
        }
    }

    private fun changeAmount(newValue: String) {
        viewModelScope.launch(Dispatchers.Main) {
            _screenState.update { it.copy(draftAmount = newValue) }
        }
    }

    private fun incAmount() {
        viewModelScope.launch(Dispatchers.Main) {
            _screenState.update {
                val incAmount = (it.draftAmount.toIntOrNull()?.inc() ?: 1).toString()
                it.copy(draftAmount = incAmount)
            }
        }
    }

    private fun decAmount() {
        viewModelScope.launch(Dispatchers.Main) {
            _screenState.update {
                val amount = it.draftAmount.toIntOrNull() ?: 0
                val decAmount = amount.dec().coerceAtLeast(0).toString()
                it.copy(draftAmount = if (decAmount == "0") "" else decAmount)
            }
        }
    }

    private fun changeUnit(newValue: String) {
        viewModelScope.launch(Dispatchers.Main) {
            _screenState.update { it.copy(draftUnit = newValue) }
        }
    }

    private fun toggleSelectedListExpanded() {
        viewModelScope.launch(Dispatchers.Main) {
            _screenState.update { it.copy(isSelectedListExpanded = !it.isSelectedListExpanded) }
        }
    }

    private fun addToMultiSelect(product: Product) {
        viewModelScope.launch(Dispatchers.Main) {
            _screenState.update { it.copy(multiSelectProducts = it.multiSelectProducts + product) }
        }
    }

    private fun deleteFromMultiSelect(product: Product) {
        viewModelScope.launch(Dispatchers.Main) {
            _screenState.update { it.copy(multiSelectProducts = it.multiSelectProducts - product) }
        }
    }

    private fun clearMultiSelect() {
        viewModelScope.launch(Dispatchers.Main) {
            _screenState.update { it.copy(multiSelectProducts = emptyList()) }
        }
    }

    private fun multiDelete() {
        viewModelScope.launch {
            val deletedItems = _screenState.value.multiSelectProducts
            recentlyDeletedSet.update { deletedItems.toSet() }
            _effect.emit(Effect.ShowSnackbarDelete(deletedItems.size))
            _screenState.update { it.copy(multiSelectProducts = emptyList()) }

        }
    }

    private fun confirmMultiDelete() {
        viewModelScope.launch(Dispatchers.IO) {
            if (recentlyDeletedSet.value.isNotEmpty()) {
                deleteProductsUseCase(remoteListId, recentlyDeletedSet.value.toList())
            }
            restoreState()
        }
    }

    private fun showOptionsSheet() {
        viewModelScope.launch(Dispatchers.Main) {
            _screenState.update { it.copy(isOptionsSheetVisible = true) }
        }
    }

    private fun dismissOptionsSheet() {
        viewModelScope.launch(Dispatchers.Main) {
            _screenState.update { it.copy(isOptionsSheetVisible = false) }
        }
    }

    private fun selectAll() {
        viewModelScope.launch(Dispatchers.IO) {
            if (_screenState.value.products.isEmpty()) {
                _effect.emit(Effect.ShowSnackbarInfo(res.getString(R.string.empty_list)))
            } else {
                selectAllProductsUseCase(remoteListId, shoppingListId)
                _effect.emit(Effect.ShowSnackbarInfo(res.getString(R.string.all_selected)))
            }
            _screenState.update { it.copy(isOptionsSheetVisible = false) }

        }
    }

    private fun clearSelection() {
        viewModelScope.launch(Dispatchers.IO) {
            val products = _screenState.value.products
            if (products.isEmpty()) {
                _effect.emit(Effect.ShowSnackbarInfo(res.getString(R.string.empty_list)))
            } else if (products.none { it.isSelected }) {
                _effect.emit(Effect.ShowSnackbarInfo(res.getString(R.string.no_selected_products)))
            } else {
                unselectAllProductsUseCase(remoteListId, shoppingListId)
                _effect.emit(Effect.ShowSnackbarInfo(res.getString(R.string.all_unselected)))

            }
            _screenState.update { it.copy(isOptionsSheetVisible = false) }
        }
    }

    private fun deleteAllSelected() {
        viewModelScope.launch {
            if (_screenState.value.products.none { it.isSelected }) {
                _effect.emit(Effect.ShowSnackbarInfo(res.getString(R.string.no_products_to_delete)))
            } else {
                _effect.emit(Effect.ShowSnackbarDelete(_screenState.value.products.count { it.isSelected }))
            }

            recentlyDeletedSet.update {
                _screenState.value.products.filter { it.isSelected }.toSet()
            }
            _screenState.update {
                it.copy(isOptionsSheetVisible = false)
            }
        }
    }

    override fun showRenameListSheet(shoppingList: ShoppingList) {
        super.showRenameListSheet(shoppingList)
        _screenState.update {
            it.copy(
                isOptionsSheetVisible = false
            )
        }
    }

    private fun enableSearchMode() {
        viewModelScope.launch(Dispatchers.Main) {
            _screenState.update {
                it.copy(
                    searchMode = true,
                    isOptionsSheetVisible = false
                )
            }
        }
    }

    private fun disableSearchMode() {
        viewModelScope.launch(Dispatchers.Main) {
            _screenState.update { it.copy(searchMode = false, searchValue = "") }
        }
    }

    private fun onChangeSearchValue(newValue: String) {
        viewModelScope.launch(Dispatchers.Main) {
            _screenState.update { it.copy(searchValue = newValue) }
        }
    }

    private fun setSortByAlphabet(checked: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            changeSortByAlphabetUseCase(shoppingListId, checked)
        }
    }

    private fun showCopySheet() {
        viewModelScope.launch(Dispatchers.Main) {
            val progressMap = withContext(Dispatchers.IO) {
                getShoppingListsUseCase().associateWith { getShoppingListProgressUseCase(it.id) }
            }

            _screenState.update {
                it.copy(
                    shoppingListProgress = progressMap,
                    isCopySheetVisible = true
                )
            }
        }
    }

    private fun dismissCopySheet() {
        viewModelScope.launch(Dispatchers.Main) {
            _screenState.update { it.copy(isCopySheetVisible = false) }
        }
    }

    private fun copyProducts(shoppingListId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val selectedProducts = _screenState.value.multiSelectProducts

            val copiedProducts = selectedProducts.map { product ->
                val newId = insertProductUseCase(
                    remoteListId,
                    product.copy(roomId = 0L, listId = shoppingListId)
                )
                product.copy(roomId = newId, listId = shoppingListId)
            }

            withContext(Dispatchers.Main) {
                _effect.emit(
                    Effect.ShowSnackbarInfo(
                        if (copiedProducts.size > 1) {
                            res.getString(R.string.products_copied)
                        } else {
                            res.getString(R.string.product_copied)
                        }
                    )
                )

                _screenState.update {
                    it.copy(
                        isCopySheetVisible = false,
                        multiSelectProducts = emptyList()
                    )
                }
            }
        }
    }

    private fun createNewListAndCopy() {
        viewModelScope.launch(Dispatchers.IO) {
            val shoppingListId = createListUseCase(res.getString(R.string.new_list))
            copyProducts(shoppingListId)
        }
    }
}
