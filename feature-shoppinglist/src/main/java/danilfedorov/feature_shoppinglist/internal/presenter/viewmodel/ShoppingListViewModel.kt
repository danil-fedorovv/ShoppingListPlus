package danilfedorov.feature_shoppinglist.internal.presenter.viewmodel

import android.content.res.Resources
import android.util.Log.i
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import danilfedorov.common.deeplink.DeepLink
import danilfedorov.domain_core.model.ShoppingList
import danilfedorov.domain_core.model.firebase.FetchRemoteListResult
import danilfedorov.domain_core.model.firebase.GetRemoteListNameResult
import danilfedorov.domain_core.usecase.product.GetProductListByListIdUseCase
import danilfedorov.domain_core.usecase.product.GetSelectedProductCountUseCase
import danilfedorov.domain_core.usecase.product.GetTotalProductCountUseCase
import danilfedorov.domain_core.usecase.product.InsertProductsUseCase
import danilfedorov.domain_core.usecase.shoppinglist.ArchiveShoppingListsUseCase
import danilfedorov.domain_core.usecase.shoppinglist.CreateListUseCase
import danilfedorov.domain_core.usecase.shoppinglist.FetchListUseCase
import danilfedorov.domain_core.usecase.shoppinglist.GetRemoteShoppingListNameUseCase
import danilfedorov.domain_core.usecase.shoppinglist.ObserveArchivedShoppingListsUseCase
import danilfedorov.domain_core.usecase.shoppinglist.ObserveRemoteListNameUseCase
import danilfedorov.domain_core.usecase.shoppinglist.ObserveShoppingListsUseCase
import danilfedorov.domain_core.usecase.shoppinglist.PushListUseCase
import danilfedorov.domain_core.usecase.shoppinglist.RenameShoppingListUseCase
import danilfedorov.feature_shoppinglist.R
import danilfedorov.feature_shoppinglist.internal.compose.sheet.deeplink.FetchListSheetState
import danilfedorov.feature_shoppinglist.internal.presenter.mvi.Action
import danilfedorov.feature_shoppinglist.internal.presenter.mvi.Effect
import danilfedorov.feature_shoppinglist.internal.presenter.mvi.ScreenState
import danilfedorov.shared_shoppinglist.presentation.mvi.SharedAction
import danilfedorov.shared_shoppinglist.presentation.viewmodel.BaseListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ShoppingListViewModel @AssistedInject constructor(
    @Assisted("navArchive") private val navArchive: () -> Unit,
    @Assisted("navSettings") private val navSettings: () -> Unit,
    @Assisted private val navProducts: (Long) -> Unit,
    private val observeShoppingListsUseCase: ObserveShoppingListsUseCase,
    private val createListUseCase: CreateListUseCase,
    private val archiveShoppingListsUseCase: ArchiveShoppingListsUseCase,
    private val getSelectedProductCountUseCase: GetSelectedProductCountUseCase,
    private val getTotalProductCountUseCase: GetTotalProductCountUseCase,
    private val getProductListByListIdUseCase: GetProductListByListIdUseCase,
    private val fetchListUseCase: FetchListUseCase,
    private val insertProductsUseCase: InsertProductsUseCase,
    private val getRemoteShoppingListNameUseCase: GetRemoteShoppingListNameUseCase,
    private val observeRemoteListNameUseCase: ObserveRemoteListNameUseCase,
    private val observeArchivedShoppingLists: ObserveArchivedShoppingListsUseCase,
    private val res: Resources,
    pushListUseCase: PushListUseCase,
    renameShoppingListUseCase: RenameShoppingListUseCase,
) : BaseListViewModel(
    pushListUseCase,
    renameShoppingListUseCase
) {

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("navArchive") navArchive: () -> Unit,
            @Assisted("navSettings") navSettings: () -> Unit,
            navProducts: (Long) -> Unit,
        ): ShoppingListViewModel
    }

    private val _screenState = MutableStateFlow(ScreenState())
    val screenState = _screenState.asStateFlow()

    private val _effect = MutableSharedFlow<Effect>()
    val effect = _effect.asSharedFlow()

    private val _recentlyDeletedIds = MutableStateFlow(setOf<Long>())
    private var remoteNameJobs = mutableMapOf<String, Job>()
    private var progressJobs = mutableMapOf<Long, Job>()

    init {
        observeShoppingLists()
    }

    init {
        viewModelScope.launch(Dispatchers.Default) {
            observeArchivedShoppingLists().collect { archives ->
                _screenState.update {
                    it.copy(
                        archivedCount = archives.size
                    )
                }
            }

        }
    }

    private fun observeShoppingLists() {
        viewModelScope.launch(Dispatchers.Default) {

            observeShoppingListsUseCase()
                .combine(_recentlyDeletedIds) { lists, deleted ->
                    lists.filter { it.id !in deleted }
                }
                .collect { visibleLists ->

                    // обновляем UI
                    _screenState.update {
                        it.copy(
                            shoppingLists = visibleLists,
                            isLoading = false
                        )
                    }

                    // обновляем слушателей
                    updateRemoteNameListeners(visibleLists)
                    updateProgressListeners(visibleLists)
                }
        }
    }

    private fun updateRemoteNameListeners(lists: List<ShoppingList>) {
        val currentIds = lists.mapNotNull { it.remoteListId }.toSet()

        // удаляем старые
        val toRemove = remoteNameJobs.keys - currentIds
        toRemove.forEach { id ->
            remoteNameJobs[id]?.cancel()
            remoteNameJobs.remove(id)
        }

        // добавляем новые
        val toAdd = currentIds - remoteNameJobs.keys
        toAdd.forEach { remoteId ->
            remoteNameJobs[remoteId] = viewModelScope.launch {
                observeRemoteListNameUseCase(remoteId).collect {}
            }
        }
    }

    private fun updateProgressListeners(lists: List<ShoppingList>) {
        val currentIds = lists.map { it.id }.toSet()

        // удаляем устаревшие
        val toRemove = progressJobs.keys - currentIds
        toRemove.forEach { id ->
            progressJobs[id]?.cancel()
            progressJobs.remove(id)
        }

        // добавляем новые
        val toAdd = currentIds - progressJobs.keys
        toAdd.forEach { id ->
            progressJobs[id] = viewModelScope.launch {

                combine(
                    getSelectedProductCountUseCase(id),
                    getTotalProductCountUseCase(id)
                ) { selected, total ->
                    id to (selected to total)
                }
                    .distinctUntilChanged()
                    .collect { (listId, counts) ->

                        _screenState.update { state ->
                            state.copy(progress = state.progress + (listId to counts))
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

            is Action.ConfirmDeleteList -> confirmDelete()
            is Action.ShowListOptions -> showListOptionsVisible(action.shoppingList)
            is Action.HideListOptions -> hideListOptionsVisible()
            is Action.RestoreState -> restoreState()

            is Action.CreateList -> {
                createList()
            }

            is Action.ShowAddListSheet -> showAddListSheet()
            is Action.HideAddListSheet -> hideAddListSheet()
            is Action.ChangeDraftListName -> changeAddListName(action.newValue)
            is Action.ExtendList -> extendShoppingList(action.extendedList)
            is Action.CopyList -> copyList(action.shoppingList)
            is Action.AddMultiSelect -> addMultiSelect(action.shoppingList)
            is Action.RemoveMultiSelect -> removeMultiSelect(action.shoppingList)
            is Action.ClearMultiSelect -> clearMultiSelect()
            is Action.MultiCopy -> multiCopy()
            is Action.DeleteLists -> multiDelete()

            is Action.HandleDeepLink -> handleDeepLink(action.deepLink)
            is Action.HideDeepLingSheet -> hideFetchSheet()
            is Action.OnClickJoinOrRetry -> onClickJoinOrRetry()
            is Action.OnClickAddUnzipList -> onClickAddUnzipList()
            is Action.DeleteList -> delete(action.list)
        }
    }

    override fun sharedAction(action: SharedAction) {
        when (action) {
            is SharedAction.ShareText -> {
                viewModelScope.launch(Dispatchers.Main) {
                    val products = action.products.ifEmpty {
                        withContext(Dispatchers.IO) {
                            getProductListByListIdUseCase(action.shoppingList?.id ?: 0)
                        }
                    }

                    action.shoppingList?.let {
                        shareText(
                            context = action.context,
                            listName = it.name,
                            products = products
                        )
                    }
                }
            }

            is SharedAction.ShareLiveEditLink -> {
                viewModelScope.launch(Dispatchers.Main) {
                    val products = action.products.ifEmpty {
                        withContext(Dispatchers.IO) {
                            getProductListByListIdUseCase(action.shoppingList?.id ?: 0)
                        }
                    }

                    action.shoppingList?.let {
                        shareLiveEditLink(
                            context = action.context,
                            list = it,
                            products = products
                        )
                    }
                }
            }

            else -> super.sharedAction(action)
        }
    }


    private fun delete(list: ShoppingList) {
        viewModelScope.launch {
            if (_recentlyDeletedIds.value.isNotEmpty()) {
                archiveShoppingListsUseCase(_recentlyDeletedIds.value.toList())
                _recentlyDeletedIds.value = emptySet()
            }
            _recentlyDeletedIds.value = setOf(list.id)
            _effect.emit(Effect.ShowSnackbarDelete(1))
        }
    }

    private fun restoreState() {
        _recentlyDeletedIds.value = emptySet()
    }

    private fun confirmDelete() {
        if (_recentlyDeletedIds.value.isNotEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                archiveShoppingListsUseCase(_recentlyDeletedIds.value.toList())
                _recentlyDeletedIds.value = emptySet()
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

            withContext(Dispatchers.Main) {
                navProducts(id)
                _screenState.update {
                    it.copy(
                        isAddListSheetVisible = false,
                        draftListName = ""
                    )
                }
            }

        }
    }

    private fun showAddListSheet() {
        viewModelScope.launch {
            getRandomActiveUnlockedFruit()
            _screenState.update {
                it.copy(isAddListSheetVisible = true)
            }
        }
    }

    private fun hideAddListSheet() {
        _screenState.update {
            it.copy(isAddListSheetVisible = false)
        }
    }

    private fun changeAddListName(newValue: String) {
        _screenState.update {
            it.copy(draftListName = newValue)
        }
    }

    private fun extendShoppingList(extendedList: ShoppingList?) {
        _screenState.update {
            it.copy(extendedList = extendedList)
        }
    }

    private fun copyList(shoppingList: ShoppingList) {
        viewModelScope.launch(Dispatchers.Main) {
            val copyName = generateCopyName(shoppingList.name)

            val id = withContext(Dispatchers.IO) { createListUseCase(copyName) }

            withContext(Dispatchers.IO) {
                val products = getProductListByListIdUseCase(shoppingList.id)
                insertProductsUseCase(products.map { it.copy(roomId = 0, listId = id) })
            }

            val oldProgress = _screenState.value.progress[shoppingList.id] ?: (0 to 0)
            val newProgressMap = _screenState.value.progress + (id to oldProgress)

            _screenState.update {
                it.copy(
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
            if (deletedItems.isEmpty()) {
                _screenState.update { it.copy(multiSelectLists = emptyList()) }
                return@launch
            }

            if (_recentlyDeletedIds.value.isNotEmpty()) {
                archiveShoppingListsUseCase(_recentlyDeletedIds.value.toList())
                _recentlyDeletedIds.value = emptySet()
            }
            _recentlyDeletedIds.value = deletedItems.map { it.id }.toSet()
            _effect.emit(Effect.ShowSnackbarDelete(deletedItems.size))
            _screenState.update { it.copy(multiSelectLists = emptyList()) }
        }
    }


    private fun multiCopy() {
        viewModelScope.launch(Dispatchers.IO) {
            val selectedLists = _screenState.value.multiSelectLists
            if (selectedLists.isEmpty()) return@launch

            for (list in selectedLists) {
                val copyName = generateCopyName(list.name)
                val newId = withContext(Dispatchers.IO) { createListUseCase(copyName) }

                val products = getProductListByListIdUseCase(list.id)
                insertProductsUseCase(products.map { it.copy(roomId = 0, listId = newId) })
            }

            withContext(Dispatchers.Main) {
                _screenState.update { it.copy(multiSelectLists = emptyList()) }

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
    }

    private fun generateCopyName(originalName: String): String {
        val suffix = res.getString(R.string.copy_suffix)
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

    private fun handleDeepLink(deepLink: DeepLink) {

        when (deepLink) {
            is DeepLink.LiveEdit -> {
                loadLiveEditInvite(deepLink.suffix)
            }
        }
    }


    private fun loadLiveEditInvite(remoteListId: String) {
        _screenState.update { it.copy(fetchListSheetState = FetchListSheetState.Loading) }

        viewModelScope.launch {
            val newState = when (val result = getRemoteShoppingListNameUseCase(remoteListId)) {
                is GetRemoteListNameResult.Success -> {
                    FetchListSheetState.Invite(result.listName, remoteListId)
                }

                is GetRemoteListNameResult.AlreadyJoined -> {
                    withContext(Dispatchers.Main) {
                        navProducts(result.localListId)
                    }
                    FetchListSheetState.Hidden
                }

                is GetRemoteListNameResult.Timeout -> {
                    FetchListSheetState.Timeout(remoteListId)
                }

                GetRemoteListNameResult.NotFound -> {
                    FetchListSheetState.NotFound
                }

                is GetRemoteListNameResult.Error -> {
                    FetchListSheetState.Error(remoteListId)
                }
            }

            _screenState.update { it.copy(fetchListSheetState = newState) }
        }
    }

    private fun onClickJoinOrRetry() {
        val currentState = _screenState.value.fetchListSheetState

        if (currentState is FetchListSheetState.Invite) {
            val invite = currentState.copy(isJoining = true)
            _screenState.update { it.copy(fetchListSheetState = invite) }

            viewModelScope.launch {
                val result = fetchListUseCase(invite.remoteListId)

                if (result is FetchRemoteListResult.Success) {
                    navProducts(result.localListId)
                    _screenState.update { it.copy(fetchListSheetState = FetchListSheetState.Hidden) }
                } else {
                    _screenState.update {
                        it.copy(
                            fetchListSheetState = FetchListSheetState.Error(
                                invite.remoteListId
                            )
                        )
                    }
                }
            }
            return
        }

        val remoteListId = when (currentState) {
            is FetchListSheetState.Timeout -> currentState.remoteListId
            is FetchListSheetState.Error -> currentState.remoteListId
            else -> return
        }

        loadLiveEditInvite(remoteListId)
    }

    private fun onClickAddUnzipList() {
        val currentState = _screenState.value.fetchListSheetState
        if (currentState is FetchListSheetState.CopyInvite) {
            viewModelScope.launch(Dispatchers.IO) {
                val shoppingListId = createListUseCase(currentState.listName)

                insertProductsUseCase(currentState.products.map { it.copy(listId = shoppingListId) })
                withContext(Dispatchers.Main) {
                    _screenState.update { it.copy(fetchListSheetState = FetchListSheetState.Hidden) }
                    navProducts(shoppingListId)
                }
            }

        }
    }

    fun hideFetchSheet() {
        _screenState.update { it.copy(fetchListSheetState = FetchListSheetState.Hidden) }
    }
}