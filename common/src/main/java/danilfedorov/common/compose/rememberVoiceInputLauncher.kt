package danilfedorov.common.compose

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import danilfedorov.common.R

@Composable
fun rememberVoiceInputLauncher(
    onResult: (String?) -> Unit
): () -> Unit {
    val context = LocalContext.current
    val activity = context as? ComponentActivity
        ?: throw IllegalStateException("Voice input requires ComponentActivity")

    val promptText = stringResource(R.string.voice_input_prompt)

    val launcher = remember {
        activity.activityResultRegistry.register(
            "voice_input_key_${System.currentTimeMillis()}",
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val results = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                val spokenText = results?.firstOrNull()
                onResult(spokenText)
            } else {
                onResult(null)
            }
        }
    }

    return remember {
        {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_PROMPT, promptText)
                putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5)
            }

            try {
                launcher.launch(intent)
            } catch (e: ActivityNotFoundException) {
                onResult(null)
            }
        }
    }
}