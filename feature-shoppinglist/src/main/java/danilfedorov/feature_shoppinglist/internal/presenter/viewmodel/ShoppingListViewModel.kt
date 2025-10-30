package danilfedorov.feature_shoppinglist.internal.presenter.viewmodel

import android.content.Context
import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import danilfedorov.common.shareListUtil
import danilfedorov.domain_core.model.ShoppingList
import danilfedorov.domain_core.usecase.product.CreateProductUseCase
import danilfedorov.domain_core.usecase.product.GetProductListByListIdUseCase
import danilfedorov.domain_core.usecase.product.GetSelectedProductCountUseCase
import danilfedorov.domain_core.usecase.product.GetTotalProductCountUseCase
import danilfedorov.domain_core.usecase.shoppinglist.ArchiveShoppingListsUseCase
import danilfedorov.domain_core.usecase.shoppinglist.CreateListUseCase
import danilfedorov.domain_core.usecase.shoppinglist.GetListsUseCase
import danilfedorov.domain_core.usecase.shoppinglist.RenameShoppingListUseCase
import danilfedorov.feature_shoppinglist.internal.presenter.mvi.Action
import danilfedorov.feature_shoppinglist.internal.presenter.mvi.Effect
import danilfedorov.feature_shoppinglist.internal.presenter.mvi.ScreenState
import io.avdev.feature_shoppinglist.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

internal class ShoppingListViewModel(
    private val navArchive: () -> Unit,
    private val navSettings: () -> Unit,
    private val navProducts: (Long) -> Unit,
    private val getListsUseCase: GetListsUseCase,
    private val createListUseCase: CreateListUseCase,
    private val archiveShoppingListsUseCase: ArchiveShoppingListsUseCase,
    private val renameShoppingListUseCase: RenameShoppingListUseCase,
    private val getSelectedProductCountUseCase: GetSelectedProductCountUseCase,
    private val getTotalProductCountUseCase: GetTotalProductCountUseCase,
    private val getProductListByListIdUseCase: GetProductListByListIdUseCase,
    private val createProductUseCase: CreateProductUseCase,
    private val res: Resources,
) : ViewModel() {

    private val _screenState = MutableStateFlow(ScreenState())
    val screenState = _screenState.asStateFlow()

    private val _effect = MutableSharedFlow<Effect>()
    val effect = _effect.asSharedFlow()

    private var undoState: ScreenState? = null
    private val recentlyDeletedList = mutableListOf<ShoppingList>()


    init {
        viewModelScope.launch {
            val lists = withContext(Dispatchers.IO) { getListsUseCase() }.reversed()

            _screenState.update { it.copy(shoppingLists = lists) }

            if (lists.isEmpty()) {
                _screenState.update { it.copy(isLoading = false) }
                return@launch
            }

            lists.forEach { list ->
                viewModelScope.launch {
                    combine(
                        getSelectedProductCountUseCase(list.id),
                        getTotalProductCountUseCase(list.id)
                    ) { selected, total ->
                        list.id to (selected to total)
                    }.collect { (id, counts) ->
                        _screenState.update { state ->
                            state.copy(
                                progress = state.progress + (id to counts),
                                isLoading = false
                            )
                        }
                    }
                }
            }
        }
    }

    fun action(action: Action) {
        when (action) {
            is Action.NavigateArchive -> navArchive()
            is Action.NavigateSettings -> navSettings()
            is Action.NavigateProduct -> navProducts(action.id)

            is Action.DeleteList -> deleteList(action.list)
            is Action.ConfirmDeleteList -> confirmDelete()
            is Action.ShowListOptions -> showListOptionsVisible(action.shoppingList)
            is Action.HideListOptions -> hideListOptionsVisible()
            is Action.ShareList -> {
                shareShoppingList(action.id, action.context)
            }

            is Action.RestoreState -> restoreState()
            is Action.CreateList -> {
                createList()
            }

            is Action.HideAddListSheet -> switchAddListSheetVisible(false)
            is Action.ShowAddListSheet -> switchAddListSheetVisible(true)
            is Action.ChangeDraftListName -> changeAddListName(action.newValue)
            is Action.ExtendList -> extendShoppingList(action.id)
            is Action.ChangeRenameList -> changeRenameList(action.newValue)
            is Action.HideRenameListSheet -> hideRenameSheetVisible()
            is Action.ShowRenameListSheet -> showRenameSheetVisible(action.shoppingList)
            is Action.RenameList -> renameList(action.id)
            is Action.CopyList -> copyList(action.shoppingList)
            is Action.AddMultiSelect -> addMultiSelect(action.shoppingList)
            is Action.RemoveMultiSelect -> removeMultiSelect(action.shoppingList)
            is Action.ClearMultiSelect -> clearMultiSelect()
            is Action.MultiCopy -> multiCopy()
            is Action.MultiDelete -> multiDelete()
        }
    }

    private fun deleteList(shoppingList: ShoppingList) {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                undoState = _screenState.value
                recentlyDeletedList.add(shoppingList)
                val lists = _screenState.value.shoppingLists - shoppingList
                _screenState.update { it.copy(shoppingLists = lists) }
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
                    _screenState.update { undoState.copy(extendedListId = null) }
                    this@ShoppingListViewModel.undoState = null
                    recentlyDeletedList.clear()
                }
            }
        }
    }

    private fun confirmDelete() {
        viewModelScope.launch(Dispatchers.IO) {
            if (recentlyDeletedList.isNotEmpty()) {
                archiveShoppingListsUseCase(recentlyDeletedList.map { it.id })
                recentlyDeletedList.clear()
            }
        }
    }

    private fun showListOptionsVisible(shoppingList: ShoppingList) {
        viewModelScope.launch(Dispatchers.Main) {
            _screenState.update { it.copy(listOptions = shoppingList) }
        }
    }

    private fun hideListOptionsVisible() {
        viewModelScope.launch(Dispatchers.Main) {
            _screenState.update { it.copy(listOptions = null) }
        }
    }


    private fun createList() {
        viewModelScope.launch {
            var listName = _screenState.value.draftListName.trim()
            if (listName.isEmpty()) listName = res.getString(R.string.shopping_list)

            val id = withContext(Dispatchers.IO) { createListUseCase(listName) }
            navProducts(id)
            val list = ShoppingList(id = id, name = listName)

            withContext(Dispatchers.Main) {
                _screenState.update {
                    it.copy(
                        shoppingLists = listOf(list) + it.shoppingLists,
                        isAddListSheetVisible = false,
                        draftListName = ""
                    )
                }
            }

        }
    }

    private fun switchAddListSheetVisible(isVisible: Boolean) {
        _screenState.update {
            it.copy(isAddListSheetVisible = isVisible)
        }
    }

    private fun changeAddListName(newValue: String) {
        _screenState.update {
            it.copy(draftListName = newValue)
        }
    }

    private fun shareShoppingList(id: Long, context: Context) {
        viewModelScope.launch(Dispatchers.Main) {
            val products = withContext(Dispatchers.IO) { getProductListByListIdUseCase(id) }
            val productString = products.map {
                buildString {
                    append(it.name)
                    if (it.amount.isNotEmpty()) {
                        append(" ")
                        append(it.amount)
                        append(it.unit)
                    }
                }
            }
            shareListUtil(context, productString)
        }
    }

    private fun extendShoppingList(id: Long?) {
        _screenState.update {
            it.copy(extendedListId = id)
        }
    }

    private fun changeRenameList(newValue: String) {
        _screenState.update {
            it.copy(draftRenameList = newValue)
        }
    }

    private fun showRenameSheetVisible(shoppingList: ShoppingList) {
        _screenState.update {
            it.copy(
                draftRenameList = shoppingList.name,
                renameListId = shoppingList.id
            )
        }
    }

    private fun hideRenameSheetVisible() {
        _screenState.update {
            it.copy(renameListId = null)
        }
    }

    private fun renameList(id: Long) {
        viewModelScope.launch(Dispatchers.Main) {
            _screenState.update { state ->
                val newLists = state.shoppingLists.map { list ->
                    if (list.id == id) list.copy(name = state.draftRenameList)
                    else list
                }
                state.copy(shoppingLists = newLists)
            }
            withContext(Dispatchers.IO) {
                renameShoppingListUseCase(
                    id = id,
                    newName = _screenState.value.draftRenameList
                )
            }
        }
    }

    private fun copyList(shoppingList: ShoppingList) {
        viewModelScope.launch(Dispatchers.Main) {
            val copyName = generateCopyName(shoppingList.name)

            val id = withContext(Dispatchers.IO) { createListUseCase(copyName) }
            val newList = ShoppingList(id = id, name = copyName)

            withContext(Dispatchers.IO) {
                val products = getProductListByListIdUseCase(shoppingList.id)
                products.forEach { product ->
                    createProductUseCase(product.copy(id = 0, listId = id))
                }
            }

            val oldProgress = _screenState.value.progress[shoppingList.id] ?: (0 to 0)
            val newProgressMap = _screenState.value.progress + (id to oldProgress)

            _screenState.update {
                it.copy(
                    shoppingLists = listOf(newList) + it.shoppingLists,
                    progress = newProgressMap
                )
            }

            _effect.emit(Effect.ShowSnackbarInfo(res.getString(R.string.list_copied)))
        }
    }

    private fun addMultiSelect(shoppingList: ShoppingList) {
        viewModelScope.launch(Dispatchers.Main) {
            _screenState.update {
                it.copy(multiSelectLists = it.multiSelectLists + shoppingList)
            }
        }
    }

    private fun removeMultiSelect(shoppingList: ShoppingList) {
        viewModelScope.launch(Dispatchers.Main) {
            _screenState.update {
                it.copy(multiSelectLists = it.multiSelectLists - shoppingList)
            }
        }
    }

    private fun clearMultiSelect() {
        viewModelScope.launch(Dispatchers.Main) {
            _screenState.update {
                it.copy(multiSelectLists = emptyList())
            }
        }
    }

    private fun multiDelete() {
        viewModelScope.launch {
            val deletedItems = _screenState.value.multiSelectLists
            withContext(Dispatchers.Main) {
                undoState = _screenState.value
                recentlyDeletedList.addAll(deletedItems)
                _screenState.update { it.copy(shoppingLists = it.shoppingLists - deletedItems) }
                _effect.emit(Effect.ShowSnackbarDelete(deletedItems.size))
                _screenState.update { it.copy(multiSelectLists = emptyList()) }
                undoState = undoState?.copy(multiSelectLists = emptyList())
            }
        }
    }

    private fun multiCopy() {
        viewModelScope.launch(Dispatchers.Main) {
            val selectedLists = _screenState.value.multiSelectLists
            if (selectedLists.isEmpty()) return@launch

            val newLists = mutableListOf<ShoppingList>()
            val oldProgress = _screenState.value.progress.toMutableMap()

            for (list in selectedLists) {
                val copyName = generateCopyName(list.name)

                val id = withContext(Dispatchers.IO) { createListUseCase(copyName) }
                val newList = ShoppingList(id = id, name = copyName)
                newLists.add(newList)

                withContext(Dispatchers.IO) {
                    val products = getProductListByListIdUseCase(list.id)
                    products.forEach { product ->
                        createProductUseCase(product.copy(id = 0, listId = id))
                    }
                }

                val progress = _screenState.value.progress[list.id] ?: (0 to 0)
                oldProgress[id] = progress
            }

            _screenState.update {
                it.copy(
                    shoppingLists = newLists + it.shoppingLists,
                    progress = oldProgress,
                    multiSelectLists = emptyList()
                )
            }

            _effect.emit(
                Effect.ShowSnackbarInfo(
                    res.getQuantityString(
                        R.plurals.lists_copied,
                        selectedLists.size,
                        selectedLists.size
                    )
                )
            )
        }
    }

    private fun generateCopyName(originalName: String): String {
        val suffix = res.getString(R.string.copy_suffix) // например "копия"
        val regex = Regex("""(.*)\s+$suffix(?:\s+(\d+))?$""", RegexOption.IGNORE_CASE)

        return if (regex.matches(originalName)) {
            val match = regex.find(originalName)!!
            val baseName = match.groupValues[1].trim()
            val number = match.groupValues.getOrNull(2)?.toIntOrNull() ?: 1
            "$baseName $suffix ${number + 1}"
        } else {
            "$originalName $suffix"
        }
    }
}