package danilfedorov.feature_productlist.internal.presenter.viewmodel

import android.content.Context
import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import danilfedorov.common.shareListUtil
import danilfedorov.domain_core.model.Product
import danilfedorov.domain_core.usecase.product.CreateProductUseCase
import danilfedorov.domain_core.usecase.product.DeleteProductsUseCase
import danilfedorov.domain_core.usecase.product.GetProductListByListIdUseCase
import danilfedorov.domain_core.usecase.product.GetShoppingListProgressUseCase
import danilfedorov.domain_core.usecase.product.SelectAllProductsUseCase
import danilfedorov.domain_core.usecase.product.UnselectAllProductsUseCase
import danilfedorov.domain_core.usecase.product.UpdateProductCardUseCase
import danilfedorov.domain_core.usecase.product.UpdateProductSelectionUseCase
import danilfedorov.domain_core.usecase.shoppinglist.ChangeSortByAlphabetUseCase
import danilfedorov.domain_core.usecase.shoppinglist.CreateListUseCase
import danilfedorov.domain_core.usecase.shoppinglist.GetListsUseCase
import danilfedorov.domain_core.usecase.shoppinglist.GetShoppingListByIdUseCase
import danilfedorov.domain_core.usecase.shoppinglist.RenameShoppingListUseCase
import io.avdev.productlist.R
import danilfedorov.feature_productlist.internal.presenter.mvi.Action
import danilfedorov.feature_productlist.internal.presenter.mvi.Effect
import danilfedorov.feature_productlist.internal.presenter.mvi.ScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class ProductListViewModel(
    private val shoppingListId: Long,
    private val onNavigateHome: () -> Unit,
    private val res: Resources,
    private val renameShoppingListUseCase: RenameShoppingListUseCase,
    private val getProductListByListIdUseCase: GetProductListByListIdUseCase,
    private val createProductUseCase: CreateProductUseCase,
    private val updateProductSelectionUseCase: UpdateProductSelectionUseCase,
    private val selectAllProductsUseCase: SelectAllProductsUseCase,
    private val unselectAllProductsUseCase: UnselectAllProductsUseCase,
    private val updateProductCardUseCase: UpdateProductCardUseCase,
    private val deleteProductsUseCase: DeleteProductsUseCase,
    private val getShoppingListByIdUseCase: GetShoppingListByIdUseCase,
    private val changeSortByAlphabetUseCase: ChangeSortByAlphabetUseCase,
    private val getShoppingListsUseCase: GetListsUseCase,
    private val getShoppingListProgressUseCase: GetShoppingListProgressUseCase,
    private val createListUseCase: CreateListUseCase
) : ViewModel() {

    private val _screenState = MutableStateFlow(ScreenState())
    val screenState = _screenState.asStateFlow()

    private val _effect = MutableSharedFlow<Effect>()
    val effect = _effect.asSharedFlow()

    private var undoState: ScreenState? = null
    private val recentlyDeletedList = mutableListOf<Product>()

    init {
        viewModelScope.launch {
            val shoppingList =
                withContext(Dispatchers.IO) { getShoppingListByIdUseCase(shoppingListId) }
            val products =
                withContext(Dispatchers.IO) { getProductListByListIdUseCase(shoppingListId) }

            val sortedProducts = withContext(Dispatchers.Default) {
                if (shoppingList.isSortedByAlphabet) {
                    products.sortedBy { it.name.lowercase() }
                } else {
                    products.sortedBy { it.id }
                }
            }

            withContext(Dispatchers.Main) {
                _screenState.update {
                    it.updateState(sortedProducts).copy(
                        shoppingListName = shoppingList.name,
                        isSortByAlphabet = shoppingList.isSortedByAlphabet,
                        isLoading = false
                    )
                }
            }
        }
    }

    init {
        viewModelScope.launch {
            _screenState
                .map { state -> Triple(state.searchMode, state.searchValue.trim(), state.products) }
                .distinctUntilChanged()
                .collect { (searchMode, q, products) ->
                    val (filtered, selected, unselected) = withContext(Dispatchers.Default) {
                        val filteredLocal = if (q.isBlank()) products
                        else products.filter { it.name.contains(q, ignoreCase = true) }
                        val (s, u) = filteredLocal.partition { it.isSelected }
                        Triple(filteredLocal, s, u)
                    }

                    withContext(Dispatchers.Main) {
                        _screenState.update {
                            it.copy(
                                filteredCount = filtered.size,
                                selectedProducts = selected,
                                unselectedProducts = unselected
                            )
                        }
                    }
                }
        }

    }

    fun action(action: Action) {
        when (action) {
            is Action.InitEditProduct -> initEditProduct(action.product)
            is Action.EditProduct -> editProduct(action.id)

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
            is Action.ShareShoppingList -> shareShoppingList(action.context)
            is Action.OnChangeShoppingListName -> changeShoppingListName(action.newValue)
            is Action.SaveNewShoppingListName -> saveNewShoppingListName()
            is Action.ShowRenameListSheet -> showRenameListSheet()
            is Action.DismissRenameListSheet -> dismissRenameListSheet()
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
            withContext(Dispatchers.Main) {
                undoState = _screenState.value
                recentlyDeletedList.add(product)
                val products = _screenState.value.products - product
                _screenState.update { it.updateState(products) }
            }

            withContext(Dispatchers.Main) {
                _effect.emit(Effect.ShowSnackbarDelete(1))
            }
        }
    }

    private fun restoreState() {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                undoState?.let { undoState ->
                    _screenState.update { undoState }
                    this@ProductListViewModel.undoState = null
                    recentlyDeletedList.clear()
                }
            }
        }
    }

    private fun confirmDelete() {
        viewModelScope.launch(Dispatchers.IO) {
            if (recentlyDeletedList.isNotEmpty()) {
                deleteProductsUseCase(recentlyDeletedList.map { it.id })
                recentlyDeletedList.clear()
            }
        }
    }

    private fun addProduct() {
        viewModelScope.launch {
            val productName = _screenState.value.draftAddProduct.trim()

            if (productName.isEmpty()) {
                _screenState.update {
                    it.copy(draftAddProduct = "")
                    return@launch
                }
            }

            val productWithoutId = Product(
                listId = shoppingListId,
                name = _screenState.value.draftAddProduct.trim(),
            )

            val newId = withContext(Dispatchers.IO) { createProductUseCase(productWithoutId) }
            val productWithId = productWithoutId.copy(id = newId)

            val newProducts = withContext(Dispatchers.Default) {
                val current = _screenState.value.products
                if (_screenState.value.isSortByAlphabet) {
                    val key = productWithId.name.lowercase()
                    val idx = current.binarySearchBy(key) { it.name.lowercase() }
                    val insertionIndex = if (idx >= 0) idx + 1 else -idx - 1
                    val mutable = current.toMutableList()
                    mutable.add(insertionIndex.coerceIn(0, mutable.size), productWithId)
                    mutable.toList()
                } else {
                    (current + productWithId).sortedBy { it.id }
                }
            }

            withContext(Dispatchers.Main) {
                _screenState.update {
                    it.updateState(newProducts).copy(draftAddProduct = "")
                }
            }
        }
    }


    private fun toggleSelect(product: Product) {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                val products = _screenState.value.products.map {
                    if (it.id == product.id) it.copy(isSelected = !it.isSelected) else it
                }
                _screenState.update { it.updateState(products) }
            }

            withContext(Dispatchers.IO) {
                updateProductSelectionUseCase(product.id, !product.isSelected)
            }
        }
    }

    private fun initEditProduct(product: Product) {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                _screenState.update {
                    it.copy(
                        editingProductId = product.id,
                        draftProductName = product.name,
                        draftAmount = product.amount,
                        draftUnit = product.unit
                    )
                }
            }
            clearMultiSelect()
        }
    }

    private fun editProduct(id: Long) {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                val current = _screenState.value
                val products = current.products.map { product ->
                    if (product.id == id) product.copy(
                        name = current.draftProductName.trim(),
                        amount = current.draftAmount.trim(),
                        unit = current.draftUnit.trim()
                    ) else product
                }

                _screenState.update { it.updateState(products).copy(editingProductId = null) }
            }

            val currentState = _screenState.value
            withContext(Dispatchers.IO) {
                updateProductCardUseCase(
                    id = id,
                    name = currentState.draftProductName.trim(),
                    amount = currentState.draftAmount.trim(),
                    unit = currentState.draftUnit.trim()
                )
            }
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
            withContext(Dispatchers.Main) {
                undoState = _screenState.value
                recentlyDeletedList.addAll(deletedItems)
                _screenState.update { it.updateState(it.products - deletedItems) }
                _effect.emit(Effect.ShowSnackbarDelete(deletedItems.size))
                _screenState.update { it.copy(multiSelectProducts = emptyList()) }
                undoState = undoState?.copy(multiSelectProducts = emptyList())
            }
        }
    }

    private fun confirmMultiDelete() {
        viewModelScope.launch(Dispatchers.IO) {
            if (recentlyDeletedList.isNotEmpty()) {
                deleteProductsUseCase(recentlyDeletedList.map { it.id })
            }
            recentlyDeletedList.clear()
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
        viewModelScope.launch {
            if (_screenState.value.products.isEmpty()) {
                withContext(Dispatchers.Main) {
                    _effect.emit(Effect.ShowSnackbarInfo(res.getString(R.string.empty_list)))
                    _screenState.update { it.copy(isOptionsSheetVisible = false)}
                }
            } else {
                withContext(Dispatchers.Main) {
                    _screenState.update {
                        it.updateState(it.products.map { it.copy(isSelected = true) })
                            .copy(isOptionsSheetVisible = false)
                    }
                }

                withContext(Dispatchers.IO) {
                    selectAllProductsUseCase(shoppingListId)
                }

                withContext(Dispatchers.Main) {
                    _effect.emit(Effect.ShowSnackbarInfo(res.getString(R.string.all_selected)))
                }
            }
        }
    }

    private fun clearSelection() {
        viewModelScope.launch {
            if(_screenState.value.products.isEmpty()) {
                _effect.emit(Effect.ShowSnackbarInfo(res.getString(R.string.empty_list)))
                _screenState.update { it.copy(isOptionsSheetVisible = false)}
            }
            else if (_screenState.value.products.any { !it.isSelected }) {
                _effect.emit(Effect.ShowSnackbarInfo(res.getString(R.string.no_selected_products)))
                _screenState.update { it.copy(isOptionsSheetVisible = false)}
            } else {
                withContext(Dispatchers.Main) {
                    _screenState.update {
                        it.updateState(it.products.map { it.copy(isSelected = false) })
                            .copy(isOptionsSheetVisible = false)
                    }
                }

                withContext(Dispatchers.IO) {
                    unselectAllProductsUseCase(shoppingListId)
                }

                withContext(Dispatchers.Main) {
                    _effect.emit(Effect.ShowSnackbarInfo(res.getString(R.string.all_unselected)))
                }
            }
        }
    }

    private fun deleteAllSelected() {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                if (_screenState.value.selectedProducts.isEmpty()) {
                    _effect.emit(Effect.ShowSnackbarInfo(res.getString(R.string.no_products_to_delete)))
                } else {
                    _effect.emit(Effect.ShowSnackbarDelete(_screenState.value.selectedProducts.size))
                }
            }

            withContext(Dispatchers.Main) {
                undoState = _screenState.value.copy(isOptionsSheetVisible = false)
                recentlyDeletedList.addAll(_screenState.value.selectedProducts)
                _screenState.update {
                    it.updateState(it.products - it.selectedProducts)
                        .copy(isOptionsSheetVisible = false)
                }
            }
        }
    }

    private fun shareShoppingList(context: Context) {
        viewModelScope.launch(Dispatchers.Main) {
            _screenState.update { it.copy(isOptionsSheetVisible = false) }
            shareListUtil(context, _screenState.value.products.map { it.name })
        }
    }

    private fun showRenameListSheet() {
        viewModelScope.launch(Dispatchers.Main) {
            _screenState.update {
                it.copy(
                    draftShoppingListName = it.shoppingListName,
                    isOptionsSheetVisible = false,
                    isRenameListVisible = true
                )
            }
        }
    }

    private fun dismissRenameListSheet() {
        viewModelScope.launch(Dispatchers.Main) {
            _screenState.update { it.copy(isRenameListVisible = false) }
        }
    }

    private fun changeShoppingListName(newValue: String) {
        viewModelScope.launch(Dispatchers.Main) {
            _screenState.update { it.copy(draftShoppingListName = newValue) }
        }
    }

    private fun saveNewShoppingListName() {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                _screenState.update {
                    it.copy(
                        shoppingListName = it.draftShoppingListName,
                        isRenameListVisible = false
                    )
                }
            }

            withContext(Dispatchers.IO) {
                renameShoppingListUseCase(
                    id = shoppingListId,
                    newName = _screenState.value.draftShoppingListName
                )
            }
        }
    }

    private fun enableSearchMode() {
        viewModelScope.launch(Dispatchers.Main) {
            _screenState.update { it.copy(searchMode = true, isOptionsSheetVisible = false) }
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
        viewModelScope.launch {
            val sortedProducts = withContext(Dispatchers.Default) {
                if (checked) {
                    _screenState.value.products.sortedBy { it.name.lowercase() }
                } else {
                    _screenState.value.products.sortedBy { it.id }
                }
            }

            withContext(Dispatchers.Main) {
                _screenState.update {
                    it.updateState(sortedProducts).copy(isSortByAlphabet = checked)
                }
            }

            withContext(Dispatchers.IO) {
                changeSortByAlphabetUseCase(shoppingListId, checked)
            }
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
                val newId = createProductUseCase(product.copy(id = 0L, listId = shoppingListId))
                product.copy(id = newId, listId = shoppingListId)
            }

            withContext(Dispatchers.Main) {
                if (shoppingListId == this@ProductListViewModel.shoppingListId) {
                    _screenState.update {
                        it.updateState(it.products + copiedProducts)
                    }
                }

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
