package danilfedorov.shoppinglistplus.ad

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.yandex.mobile.ads.banner.BannerAdEventListener
import com.yandex.mobile.ads.banner.BannerAdSize
import com.yandex.mobile.ads.banner.BannerAdView
import com.yandex.mobile.ads.common.AdRequest
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.AdTheme
import com.yandex.mobile.ads.common.ImpressionData
import danilfedorov.shoppinglistplus.BuildConfig
import kotlinx.coroutines.delay

@Composable
fun YandexStickyBanner(
    isDarkTheme: Boolean,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val bannerView = remember {
        BannerAdView(context).apply {
            setAdSize(
                BannerAdSize.inlineSize(
                    context,
                    context.resources.displayMetrics.widthPixels,
                    75
                )
            )
            setAdUnitId(BuildConfig.YANDEX_BANNER_KEY)
            setBannerAdEventListener(object : BannerAdEventListener {
                override fun onAdLoaded() {}
                override fun onAdFailedToLoad(error: AdRequestError) {
                    Log.d("YandexBanner", "Load failed: ${error.description}")
                }
                override fun onAdClicked() {}
                override fun onLeftApplication() {}
                override fun onReturnedToApplication() {}
                override fun onImpression(data: ImpressionData?) {}
            })
        }
    }

    LaunchedEffect(isDarkTheme) {
        while (true) {
            val adRequest = AdRequest.Builder()
                .setPreferredTheme(if (isDarkTheme) AdTheme.DARK else AdTheme.LIGHT)
                .build()

            bannerView.loadAd(adRequest)
            delay(30_000)
        }
    }

    DisposableEffect(bannerView) {
        onDispose {
            bannerView.destroy()
        }
    }

    AndroidView(
        modifier = modifier,
        factory = { bannerView }
    )
}