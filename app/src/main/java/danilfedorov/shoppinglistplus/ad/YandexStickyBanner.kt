package danilfedorov.shoppinglistplus.ad

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.yandex.mobile.ads.banner.BannerAdEventListener
import com.yandex.mobile.ads.banner.BannerAdSize
import com.yandex.mobile.ads.banner.BannerAdView
import com.yandex.mobile.ads.common.AdRequest
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.AdTheme
import com.yandex.mobile.ads.common.ImpressionData

@Composable
fun YandexStickyBanner(
    isDarkTheme: Boolean,
) {
    AndroidView(
        modifier = Modifier
            .navigationBarsPadding()
            .background(colorScheme.background),
        factory = { context ->
            BannerAdView(context).apply {
                fitsSystemWindows = false
                setAdSize(
                    BannerAdSize.inlineSize(
                        context,
                        resources.displayMetrics.widthPixels,
                        75
                    )
                )
                setAdUnitId("R-M-17580296-1")
                setBannerAdEventListener(object : BannerAdEventListener {
                    override fun onAdLoaded() {}
                    override fun onAdFailedToLoad(error: AdRequestError) {}
                    override fun onAdClicked() {}
                    override fun onLeftApplication() {}
                    override fun onReturnedToApplication() {}
                    override fun onImpression(p0: ImpressionData?) {}
                })
            }
        },
        update = { bannerView ->
            val adRequest = AdRequest.Builder()
                .setPreferredTheme(if (isDarkTheme) AdTheme.DARK else AdTheme.LIGHT)
                .build()

            bannerView.loadAd(adRequest)
        }
    )
}
