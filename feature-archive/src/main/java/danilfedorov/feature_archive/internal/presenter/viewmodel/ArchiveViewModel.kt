package danilfedorov.feature_archive.internal.presenter.viewmodel

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import danilfedorov.archive.R
import danilfedorov.domain_core.model.ShoppingList
import danilfedorov.domain_core.usecase.product.GetProductListByListIdUseCase
import danilfedorov.domain_core.usecase.product.GetSelectedProductCountUseCase
import danilfedorov.domain_core.usecase.product.GetTotalProductCountUseCase
import danilfedorov.domain_core.usecase.shoppinglist.DeleteListsUseCase
import danilfedorov.domain_core.usecase.shoppinglist.ObserveArchivedShoppingListsUseCase
import danilfedorov.domain_core.usecase.shoppinglist.RestoreShoppingListUseCase
import danilfedorov.feature_archive.internal.presenter.mvi.Action
import danilfedorov.feature_archive.internal.presenter.mvi.Effect
import danilfedorov.feature_archive.internal.presenter.mvi.ScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ArchiveViewModel @AssistedInject constructor(
    @Assisted private val navHome: () -> Unit,
    private val observeArchivedShoppingListsUseCase: ObserveArchivedShoppingListsUseCase,
    private val getSelectedProductCountUseCase: GetSelectedProductCountUseCase,
    private val getTotalProductCountUseCase: GetTotalProductCountUseCase,
    private val deleteListsUseCase: DeleteListsUseCase,
    private val getProductListByListIdUseCase: GetProductListByListIdUseCase,
    private val restoreShoppingListUseCase: RestoreShoppingListUseCase,
    private val res: Resources,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(navHome: () -> Unit): ArchiveViewModel
    }

    private val _screenState = MutableStateFlow(ScreenState())
    val screenState = _screenState.asStateFlow()

    private val _effect = MutableSharedFlow<Effect>()
    val effect = _effect.asSharedFlow()

    private var recentlyDeletedIds = MutableStateFlow<Set<Long>>(emptySet())

    init {
        viewModelScope.launch(Dispatchers.Default) {
            observeArchivedShoppingListsUseCase()
                .combine(recentlyDeletedIds) { archives, deletedIds ->
                    archives.filter {
                        it.id !in deletedIds
                    }
                }.onEach { filteredArchives ->
                    _screenState.update {
                        it.copy(
                            archiveLists = filteredArchives.reversed(),
                            isLoading = false
                        )
                    }

                    if (filteredArchives.isEmpty()) return@onEach

                    filteredArchives.forEach { list ->
                        launch {
                            combine(
                                getSelectedProductCountUseCase(list.id),
                                getTotalProductCountUseCase(list.id)
                            ) { selected, total ->
                                list.id to "$selected/$total"
                            }.collect { (id, progressString) ->
                                _screenState.update { state ->
                                    state.copy(
                                        progress = state.progress + (id to progressString)
                                    )
                                }
                            }
                        }
                    }
                }
                .launchIn(this)
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
        viewModelScope.launch(Dispatchers.IO) {
            restoreShoppingListUseCase(archive.id)
        }
    }

    private fun deleteArchive(archive: ShoppingList) {
        viewModelScope.launch(Dispatchers.Main) {
            val oldIds = recentlyDeletedIds.value
            if (oldIds.isNotEmpty()) {
                withContext(Dispatchers.IO) {
                    deleteListsUseCase(oldIds.toList())
                }
            }
            recentlyDeletedIds.value = setOf(archive.id)
            _effect.emit(Effect.ShowSnackbarDelete(res.getString(R.string.list_deleted)))
        }
    }

    private fun restoreState() {
        recentlyDeletedIds.value = emptySet()
    }

    private fun confirmDelete() {
        val ids = recentlyDeletedIds.value
        if (ids.isNotEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                deleteListsUseCase(ids.toList())
            }
        }
    }

    private fun showProductsSheet(archive: ShoppingList) {
        viewModelScope.launch(Dispatchers.Main) {
            val productList =
                withContext(Dispatchers.IO) { getProductListByListIdUseCase(archive.id) }
            _screenState.update {
                it.copy(
                    productList = productList,
                    isArchiveProductsExpanded = archive
                )
            }
        }
    }

    private fun dismissProductsSheet() {
        viewModelScope.launch(Dispatchers.Main) {
            _screenState.update {
                it.copy(isArchiveProductsExpanded = null)
            }
        }
    }

    private fun deleteAll() {
        viewModelScope.launch(Dispatchers.Main) {
            if (recentlyDeletedIds.value.isNotEmpty()) {
                deleteListsUseCase(recentlyDeletedIds.value.toList())
            }
            recentlyDeletedIds.value = _screenState.value.archiveLists.map { it.id }.toSet()
            _effect.emit(Effect.ShowSnackbarDelete(res.getString(R.string.lists_deleted)))
        }
    }
}












