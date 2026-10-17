@file:OptIn(ExperimentalLayoutApi::class)

package danilfedorov.feature_add_product.internal.presenter.screen

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imeAnimationTarget
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.feed.FeedAd
import com.yandex.mobile.ads.feed.FeedAdAppearance
import com.yandex.mobile.ads.feed.FeedAdLoadListener
import com.yandex.mobile.ads.feed.FeedAdRequestConfiguration
import danilfedorov.common.R
import danilfedorov.common.compose.AppTextField
import danilfedorov.common.compose.NoRippleInteractionSource
import danilfedorov.common.compose.rememberVoiceInputLauncher
import danilfedorov.common.core_res.TextWhite
import danilfedorov.common.core_res.outline
import danilfedorov.common.extension.modifier.noRippleClickable
import danilfedorov.domain_core.model.ProductSuggestion
import danilfedorov.feature_add_product.internal.ad.YandexFeedAd
import danilfedorov.feature_add_product.internal.compose.SuggestionItem
import danilfedorov.feature_add_product.internal.presenter.mvi.Action
import danilfedorov.feature_add_product.internal.presenter.mvi.ScreenState
import java.util.Locale

@Composable
fun AddProductScreen(
    screenState: ScreenState,
    action: (Action) -> Unit,
) {
    val context = LocalContext.current
    var showPartnerOffers by rememberSaveable { mutableStateOf(false) }
    val shouldShowPartnerOffers = remember(screenState.suggestions) {
        screenState.suggestions.size > 7
    }
    val screenWidthPx = LocalConfiguration.current.screenWidthDp
    val cardWidthDp = screenWidthPx - 32

    val appearance = remember(cardWidthDp) {
        FeedAdAppearance
            .Builder(cardWidthDp)
            .setCardCornerRadius(20.0)
            .build()
    }

    val config = remember {
        FeedAdRequestConfiguration
            .Builder("R-M-17580296-3")
            .build()
    }

    val feedAd = remember {
        FeedAd.Builder(context, config, appearance).build()
    }

    DisposableEffect(feedAd) {
        val listener = object : FeedAdLoadListener {
            override fun onAdLoaded() {
                showPartnerOffers = true
            }

            override fun onAdFailedToLoad(error: AdRequestError) {
                Log.d("adLoaded", "-")
            }
        }

        feedAd.loadListener = listener
        feedAd.preloadAd()

        onDispose {
            feedAd.loadListener = null
        }
    }

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val nestedScrollConnection = remember { object : NestedScrollConnection {} }

    val voiceInputLauncher = rememberVoiceInputLauncher { recognizedText ->
        recognizedText?.let {
            action(Action.VoiceInputReceived(it))
        }
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        focusManager.moveFocus(FocusDirection.Next)
    }

    BackHandler {
        action(Action.SaveProducts)
    }

    val normalizedSelected = remember(screenState.productsToAdd) {
        screenState.productsToAdd.map { it.trim().lowercase(Locale.ROOT) }.toHashSet()
    }
    val normalizedInput = remember(screenState.searchInput) {
        screenState.searchInput.trim().lowercase(Locale.ROOT)
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = colorScheme.onBackground)
                    .padding(16.dp)
                    .systemBarsPadding(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_back),
                    contentDescription = stringResource(R.string.navback),
                    tint = colorScheme.secondary,
                    modifier = Modifier
                        .size(32.dp)
                        .noRippleClickable {
                            action(Action.SaveProducts)
                        }
                )
                AppTextField(
                    value = screenState.searchInput,
                    onValueChange = {
                        action(Action.OnChangeSearchInput(it))
                    },
                    trailingIcon = {
                        if (screenState.searchInput.isEmpty()) {
                            Icon(
                                painter = painterResource(R.drawable.ic_mic),
                                contentDescription = stringResource(R.string.add_product),
                                modifier = Modifier
                                    .size(24.dp)
                                    .noRippleClickable {
                                        voiceInputLauncher()
                                    },
                                tint = colorScheme.primary,
                            )
                        } else {
                            Box(
                                Modifier
                                    .size(20.dp)
                                    .background(shape = CircleShape, color = outline)
                                    .noRippleClickable {
                                        action(Action.OnChangeSearchInput(""))
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_cross),
                                    contentDescription = stringResource(R.string.clear_search),
                                    tint = colorScheme.surface,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    },
                    placeholder = {
                        Text(
                            text = stringResource(R.string.add_product),
                            style = typography.titleMedium,
                            color = colorScheme.outline
                        )
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_search),
                            contentDescription = stringResource(R.string.search),
                            tint = colorScheme.primary,
                            modifier = Modifier
                                .size(24.dp)
                        )
                    },
                    focusRequester = focusRequester,
                    onDone = {
                        if (screenState.searchInput.isNotBlank()) {
                            action(Action.ToggleSuggestion(screenState.searchInput))
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        Column(Modifier.padding(innerPadding)) {

            val displayItems = remember(
                screenState.productsToAdd,
                screenState.suggestions,
                screenState.searchInput
            ) {
                val selected = screenState.productsToAdd
                val searchList = screenState.suggestions
                val query = screenState.searchInput.trim()

                val map = LinkedHashMap<String, ProductSuggestion>(selected.size + searchList.size)

                if (query.isEmpty()) {
                    selected.forEach { name ->
                        val normalized = name.trim().lowercase(Locale.ROOT)
                        if (normalized.isNotEmpty() && !map.containsKey(normalized)) {
                            map[normalized] = ProductSuggestion(
                                displayName = name,
                                normalizedName = normalized,
                                usageCount = 0
                            )
                        }
                    }
                }

                searchList.forEach { suggestion ->
                    val normalized = suggestion.normalizedName.trim().lowercase(Locale.ROOT)
                    if (normalized.isNotEmpty() && !map.containsKey(normalized)) {
                        map[normalized] = suggestion
                    }
                }

                map.values.toList()
            }

            LazyColumn(
                modifier = Modifier
                    .nestedScroll(nestedScrollConnection)
            ) {
                items(
                    items = displayItems,
                    key = { suggestion ->
                        val normalized = suggestion.normalizedName.trim().lowercase(Locale.ROOT)
                        when {
                            normalized.isNotEmpty() && normalized == normalizedInput -> "input_echo"
                            normalizedSelected.contains(normalized) -> "selected_$normalized"
                            else -> "unselected_$normalized"
                        }
                    }
                ) { suggestion ->
                    Column(
                        Modifier.animateItem()
                    ) {
                        SuggestionItem(
                            isAdded = normalizedSelected.contains(
                                suggestion.displayName.trim().lowercase(Locale.ROOT)
                            ),
                            displayName = suggestion.displayName,
                            toggleSuggestion = {
                                action(Action.ToggleSuggestion(suggestion.displayName))
                            },
                        )
                        HorizontalDivider(
                            thickness = 1.dp,
                            color = colorScheme.surface,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                if (showPartnerOffers && shouldShowPartnerOffers) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .nestedScroll(nestedScrollConnection),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(Modifier.height(32.dp))
                            Text(
                                text = stringResource(danilfedorov.feature_add_product.R.string.partners_suggestions_title),
                                style = typography.titleLarge,
                                color = colorScheme.secondary
                            )
                            Spacer(Modifier.height(16.dp))
                            YandexFeedAd(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .nestedScroll(nestedScrollConnection),
                                feedAd = feedAd
                            )
                        }
                    }
                } else {
                    item {
                        Spacer(
                            Modifier
                                .fillMaxWidth()
                                .height(350.dp)
                                .background(colorScheme.background)
                        )
                    }
                }
            }
        }
    }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        if (screenState.productsToAdd != screenState.initialProducts) {

            val density = LocalDensity.current

            val keyboardHeightDp = with(density) {
                WindowInsets.imeAnimationTarget.getBottom(this).toDp()
            }

            val fabSize = 56.dp
            val margin = 8.dp

            FloatingActionButton(
                onClick = { action(Action.SaveProducts) },
                shape = CircleShape,
                modifier = Modifier
                    .then(
                        if (keyboardHeightDp > 0.dp) {
                            val screenHeightDp = with(density) {
                                LocalWindowInfo.current.containerSize.height.toDp()
                            }

                            Modifier
                                .align(Alignment.TopEnd)
                                .padding(
                                    top = screenHeightDp
                                            - keyboardHeightDp
                                            - fabSize
                                            - margin,
                                    end = margin
                                )
                        } else {
                            Modifier
                                .align(Alignment.BottomEnd)
                                .padding(margin)
                        }
                    )
                    .size(fabSize),
                containerColor = colorScheme.primary,
                contentColor = TextWhite,
                interactionSource = NoRippleInteractionSource

            ) {
                Icon(
                    painter = painterResource(
                        danilfedorov.feature_add_product.R.drawable.ic_check
                    ),
                    contentDescription = stringResource(R.string.save),
                    tint = TextWhite,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}