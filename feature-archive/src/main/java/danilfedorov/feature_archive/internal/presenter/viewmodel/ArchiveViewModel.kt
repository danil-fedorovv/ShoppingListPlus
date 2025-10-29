package danilfedorov.feature_archive.internal.presenter.viewmodel

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import danilfedorov.domain_core.model.ShoppingList
import danilfedorov.domain_core.usecase.product.GetProductListByListIdUseCase
import danilfedorov.domain_core.usecase.product.GetSelectedProductCountUseCase
import danilfedorov.domain_core.usecase.product.GetTotalProductCountUseCase
import danilfedorov.domain_core.usecase.shoppinglist.DeleteListsUseCase
import danilfedorov.domain_core.usecase.shoppinglist.GetArchivedShoppingListsUseCase
import danilfedorov.domain_core.usecase.shoppinglist.RestoreShoppingListUseCase
import danilfedorov.feature_archive.internal.presenter.mvi.Action
import danilfedorov.feature_archive.internal.presenter.mvi.Effect
import danilfedorov.feature_archive.internal.presenter.mvi.ScreenState
import io.avdev.archive.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ArchiveViewModel(
    private val navHome: () -> Unit,
    private val getArchivedShoppingListsUseCase: GetArchivedShoppingListsUseCase,
    private val getSelectedProductCountUseCase: GetSelectedProductCountUseCase,
    private val getTotalProductCountUseCase: GetTotalProductCountUseCase,
    private val deleteListsUseCase: DeleteListsUseCase,
    private val getProductListByListIdUseCase: GetProductListByListIdUseCase,
    private val restoreShoppingListUseCase: RestoreShoppingListUseCase,
    private val res: Resources
) : ViewModel() {
    private val _screenState = MutableStateFlow(ScreenState())
    val screenState = _screenState.asStateFlow()

    private val _effect = MutableSharedFlow<Effect>()
    val effect = _effect.asSharedFlow()

    private var undoState: ScreenState? = null
    private var deleteArchiveList = mutableListOf<ShoppingList>()

    init {
        viewModelScope.launch {
            val archives =
                withContext(Dispatchers.IO) { getArchivedShoppingListsUseCase() }.reversed()

            _screenState.update { it.copy(archiveLists = archives) }

            if (archives.isEmpty()) {
                _screenState.update { it.copy(isLoading = false) }
                return@launch
            }

            archives.forEach { list ->
                viewModelScope.launch {
                    combine(
                        getSelectedProductCountUseCase(list.id),
                        getTotalProductCountUseCase(list.id)
                    ) { selected, total ->
                        list.id to "$selected/$total"
                    }.collect { (id, progressString) ->
                        _screenState.update { state ->
                            state.copy(
                                progress = state.progress + (id to progressString),
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
            is Action.NavHome -> navHome()
            is Action.RestoreArchive -> restoreArchive(action.archive)
            is Action.DeleteArchive -> deleteArchive(action.archive)
            is Action.RestoreState -> restoreState()
            is Action.ConfirmDeleteArchive -> confirmDelete()
            is Action.ShowProductsSheet -> showProductsSheet(action.archive)
            is Action.DismissProductsSheet -> dismissProductsSheet()
            is Action.DeleteAll -> deleteAll()
        }
    }

    private fun restoreArchive(archive: ShoppingList) {
        viewModelScope.launch(Dispatchers.Main) {
            _screenState.update {
                it.copy(archiveLists = it.archiveLists - archive)
            }
            deleteArchiveList.remove(archive)
            undoState?.let {
                undoState = it.copy(archiveLists = it.archiveLists - archive)
            }
            withContext(Dispatchers.IO) { restoreShoppingListUseCase(archive.id) }
        }
    }

    private fun deleteArchive(archive: ShoppingList) {
        viewModelScope.launch(Dispatchers.Main) {
            _screenState.update {
                undoState = it
                deleteArchiveList.add(archive)
                it.copy(archiveLists = it.archiveLists - archive)
            }
            _effect.emit(Effect.ShowSnackbarDelete(res.getString(R.string.list_deleted)))
        }
    }

    private fun restoreState() {
        undoState?.let { undoState ->
            _screenState.update {
                undoState
            }
            deleteArchiveList.clear()
            this.undoState = null
        }
    }

    private fun confirmDelete() {
        viewModelScope.launch(Dispatchers.IO) {
            deleteListsUseCase(deleteArchiveList.map { it.id })
            deleteArchiveList.clear()
        }
    }

    private fun showProductsSheet(archive: ShoppingList) {
        viewModelScope.launch(Dispatchers.Main) {
            val productList =
                withContext(Dispatchers.IO) { getProductListByListIdUseCase(archive.id) }
            _screenState.update {
                it.copy(
                    productList = productList,
                    isProductsSheetExpandName = archive.name
                )
            }
        }
    }

    private fun dismissProductsSheet() {
        viewModelScope.launch(Dispatchers.Main) {
            _screenState.update {
                it.copy(isProductsSheetExpandName = "")
            }
        }
    }

    private fun deleteAll() {
        viewModelScope.launch(Dispatchers.Main) {
            _screenState.update {
                undoState = it
                deleteArchiveList.addAll(it.archiveLists)
                it.copy(archiveLists = emptyList())
            }
            _effect.emit(Effect.ShowSnackbarDelete(res.getString(R.string.lists_deleted)))
        }
    }
}












