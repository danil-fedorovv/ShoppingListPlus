@file:OptIn(ExperimentalMaterial3Api::class)

package danilfedorov.feature_settings.internal.presenter.screen

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import danilfedorov.common.compose.AppTopBar
import danilfedorov.common.extension.modifier.elevatedCard
import danilfedorov.feature_settings.R
import danilfedorov.feature_settings.internal.compose.SettingsOption
import danilfedorov.feature_settings.internal.compose.SettingsOptionTitle
import danilfedorov.feature_settings.internal.compose.sheet.ChangeColorSheet
import danilfedorov.feature_settings.internal.presenter.mvi.Action
import danilfedorov.feature_settings.internal.presenter.mvi.Effect
import danilfedorov.feature_settings.internal.presenter.mvi.ScreenState
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.rustore.sdk.review.RuStoreReviewManagerFactory

@Composable
internal fun SettingsScreen(
    screenState: ScreenState,
    action: (Action) -> Unit,
    effect: Flow<Effect>,
) {
    var snackbarJob by remember { mutableStateOf<Job?>(null) }
    val snackbarHostState = remember { SnackbarHostState() }
    val errorString = stringResource(danilfedorov.common.R.string.ad_load_failed)
    val okString = stringResource(danilfedorov.common.R.string.ok).uppercase()
    val context = LocalContext.current

    LaunchedEffect(effect) {
        effect.collect { effect ->
            when (effect) {
                is Effect.FailedToLoadAd -> {

                    snackbarJob?.cancelAndJoin()

                    snackbarJob = launch {
                        val result = snackbarHostState.showSnackbar(
                            message = errorString,
                            actionLabel = okString,
                            duration = SnackbarDuration.Short
                        )

                        if (result == SnackbarResult.ActionPerformed) {
                            snackbarJob?.cancelAndJoin()
                        }
                    }
                }

                Effect.NavContactUs -> openContactUsEmail(context)
                Effect.NavRateUs -> showReviewFlow(
                    context = context,
                    onFallback = { openInRuStore(context) }
                )
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 32.dp),
            ) { data ->
                Snackbar(

                    containerColor = colorScheme.onBackground,
                    shape = shapes.medium,
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
        topBar = {
            AppTopBar(
                title = stringResource(R.string.settings),
                navBack = { action(Action.NavBack) }
            )
        },
        bottomBar = {
            Spacer(Modifier.size(0.dp))
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            SettingsOptionTitle(title = stringResource(R.string.settings_section_appearance))

            SettingsOption(
                title = stringResource(R.string.settings_primary_color),
                rightContent = {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(color = colorScheme.primary, shape = CircleShape)
                    )
                }
            ) {
                action(Action.ShowChangeColorSheet)
            }

            HorizontalDivider(
                color = colorScheme.surface,
                thickness = 1.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .elevatedCard(colorScheme.surface)
            )
            SettingsOption(
                title = stringResource(R.string.settings_dark_theme),
                rightContent = {
                    Switch(
                        checked = screenState.darkTheme,
                        onCheckedChange = { action(Action.SetDarkTheme(!screenState.darkTheme)) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = colorScheme.primary,
                            uncheckedThumbColor = colorScheme.outline,
                            uncheckedTrackColor = Color.Transparent,
                            uncheckedBorderColor = colorScheme.outline
                        ),
                        modifier = Modifier.scale(0.8f)
                    )
                }
            ) {

            }
            SettingsOptionTitle(title = stringResource(R.string.settings_section_application))
            HorizontalDivider(
                color = colorScheme.surface,
                thickness = 1.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .elevatedCard(colorScheme.surface)
            )

            SettingsOption(
                title = stringResource(R.string.settings_rate_app),
                rightContent = {
                    Icon(
                        painterResource(danilfedorov.common.R.drawable.ic_external),
                        contentDescription = null,
                        tint = colorScheme.secondary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            ) {
                action(Action.RateUs)
            }
            HorizontalDivider(
                color = colorScheme.surface,
                thickness = 1.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .elevatedCard(colorScheme.surface)
            )

            SettingsOption(
                title = stringResource(R.string.settings_contact_us),
                rightContent = {
                    Icon(
                        painterResource(danilfedorov.common.R.drawable.ic_external),
                        contentDescription = null,
                        tint = colorScheme.secondary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            ) {
                action(Action.ContactUs)
            }
        }
    }

    if (screenState.isChangeColorSheetVisible) {
        ChangeColorSheet(
            colorIndex = screenState.colorIndex,
            saveColor = { action(Action.SaveColor(it)) },
            onDismissRequest = { action(Action.HideChangeColorSheet) }
        )
    }
}

private fun showReviewFlow(
    context: Context,
    onFallback: () -> Unit
) {
    val manager = RuStoreReviewManagerFactory.create(context)

    manager.requestReviewFlow()
        .addOnSuccessListener { reviewInfo ->
            manager.launchReviewFlow(reviewInfo)
                .addOnFailureListener {
                    onFallback()
                }
        }
        .addOnFailureListener {
            onFallback()
        }
}

fun openInRuStore(context: Context) {
    val uri = "https://apps.rustore.ru/app/${context.packageName}".toUri()
    val intent = Intent(Intent.ACTION_VIEW, uri).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    context.startActivity(intent)
}

fun openContactUsEmail(context: Context) {
    val email = "danildotkt@yandex.ru"
    val subject = context.getString(R.string.feedback_subject)
    val message = context.getString(R.string.feedback_prefilled_message)

    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = "mailto:".toUri()
        putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, message)
    }

    try {
        context.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        e.printStackTrace()
    }
}