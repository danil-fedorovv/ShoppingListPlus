package danilfedorov.shared_shoppinglist.presentation.viewmodel

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import danilfedorov.common.core_res.fruitsRes
import danilfedorov.common.deeplink.DeepLink
import danilfedorov.common.deeplink.DeepLink.LiveEdit
import danilfedorov.common.deeplink.DeepLinkFactory
import danilfedorov.domain_core.model.Product
import danilfedorov.domain_core.model.ShoppingList
import danilfedorov.domain_core.model.firebase.PushListResult
import danilfedorov.domain_core.usecase.shoppinglist.PushListUseCase
import danilfedorov.domain_core.usecase.shoppinglist.RenameShoppingListUseCase
import danilfedorov.shared_shoppinglist.R
import danilfedorov.shared_shoppinglist.presentation.compose.sheet.push_list.PushListSheetState
import danilfedorov.shared_shoppinglist.presentation.mvi.SharedAction
import danilfedorov.shared_shoppinglist.presentation.mvi.SharedScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class BaseListViewModel(
    private val pushListUseCase: PushListUseCase,
    private val renameShoppingListUseCase: RenameShoppingListUseCase
) : ViewModel() {

    private val _sharedState = MutableStateFlow(SharedScreenState())
    val sharedState = _sharedState.asStateFlow()

    open fun sharedAction(action: SharedAction) {
        when (action) {
            is SharedAction.ShareText -> shareText(
                context = action.context,
                listName = action.listName,
                products = action.products
            )

            is SharedAction.ShareLiveEditLink -> action.shoppingList?.let {
                shareLiveEditLink(
                    context = action.context,
                    list = it,
                    products = action.products
                )
            }

            SharedAction.HidePushListSheet -> hidePushListSheet()
            is SharedAction.ChangeTempListName -> changeTempListName(action.newValue)
            is SharedAction.SaveListName -> saveListName()
            is SharedAction.ShowRenameListSheet -> showRenameListSheet(action.shoppingList)
            SharedAction.HideRenameListSheet -> hideRenameListSheet()
        }
    }

    protected open fun shareText(
        context: Context,
        listName: String,
        products: List<Product>,
    ) {
        val defaultListName = context.getString(R.string.default_list_name)
        val emptyListText = context.getString(R.string.empty_list_text)
        val displayListName = listName.ifBlank { defaultListName }

        val formattedList = buildString {
            appendLine("🛒 $displayListName")

            if (products.isEmpty()) {
                appendLine(emptyListText)
            } else {
                products.forEachIndexed { index, product ->
                    append("${index + 1}. ${product.name}")
                    if (product.amount.isNotBlank()) {
                        append(" — ${product.amount} ${product.unit}")
                    }
                    appendLine()
                }
            }
        }
        val message = context.getString(R.string.share_link_template, formattedList)
        shareMessage(context, message)
    }

    protected open fun shareLiveEditLink(
        list: ShoppingList,
        products: List<Product>,
        context: Context,
    ) {
        viewModelScope.launch {
            _sharedState.update {
                it.copy(
                    isLinkLoading = true,
                    pushListSheetState = PushListSheetState.Hidden
                )
            }
            when (val result = pushListUseCase(list, products)) {
                is PushListResult.Success -> {
                    val link = DeepLinkFactory.createLink(
                        LiveEdit(result.remoteId)
                    )
                    val message = context.getString(R.string.share_live_edit_template, link)
                    shareMessage(context, message)
                }

                is PushListResult.Failure -> {
                    _sharedState.update {
                        it.copy(
                            pushListSheetState = PushListSheetState.Error(
                                list,
                                products,
                                context
                            )
                        )
                    }
                }

                is PushListResult.Timeout -> {
                    _sharedState.update {
                        it.copy(
                            pushListSheetState = PushListSheetState.Timeout(
                                list,
                                products,
                                context
                            )
                        )
                    }
                }
            }

            _sharedState.update { it.copy(isLinkLoading = false) }
        }
    }

    protected open fun shareMessage(context: Context, message: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, message)
        }
        val chooser = Intent.createChooser(intent, context.getString(R.string.share_link))
        context.startActivity(chooser)
    }

    protected open fun hidePushListSheet() {
        _sharedState.update {
            it.copy(pushListSheetState = PushListSheetState.Hidden)
        }
    }

    protected open fun changeTempListName(newValue: String) {
        _sharedState.update {
            it.copy(tempListName = newValue)
        }
    }

    protected open fun saveListName() {
        val newName = _sharedState.value.tempListName.trim()

        if (newName.isEmpty()) {
            hideRenameListSheet()
            return
        }
        viewModelScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                _sharedState.value.renamingList?.let {
                    renameShoppingListUseCase(
                        remoteListId = it.remoteListId,
                        id = it.id,
                        newName = _sharedState.value.tempListName
                    )
                }
            }
            hideRenameListSheet()
        }
    }

    protected open fun showRenameListSheet(shoppingList: ShoppingList) {
        viewModelScope.launch {
            getRandomActiveUnlockedFruit()
            _sharedState.update {
                it.copy(
                    renamingList = shoppingList,
                    tempListName = shoppingList.name
                )
            }
        }

    }


    protected open suspend fun getRandomActiveUnlockedFruit() {
        _sharedState.update {
            it.copy(
                renameSheetFruitResId = fruitsRes.random())
        }

    }

    protected open fun hideRenameListSheet() {
        _sharedState.update {
            it.copy(renamingList = null)
        }
    }


}