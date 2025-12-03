package danilfedorov.common.ad

import android.app.Activity
import android.content.Context
import android.util.Log
import com.yandex.mobile.ads.common.AdError
import com.yandex.mobile.ads.common.AdRequestConfiguration
import com.yandex.mobile.ads.common.AdRequestError
import com.yandex.mobile.ads.common.ImpressionData
import com.yandex.mobile.ads.rewarded.Reward
import com.yandex.mobile.ads.rewarded.RewardedAd
import com.yandex.mobile.ads.rewarded.RewardedAdEventListener
import com.yandex.mobile.ads.rewarded.RewardedAdLoadListener
import com.yandex.mobile.ads.rewarded.RewardedAdLoader

class AdManager(private val yandexRewardedKey: String, context: Context) {
    private val activity = context as Activity
    private var rewardedAd: RewardedAd? = null
    private val loader = RewardedAdLoader(activity)

    init {
        loader.setAdLoadListener(object : RewardedAdLoadListener {
            override fun onAdLoaded(rewarded: RewardedAd) {
                rewardedAd = rewarded
            }

            override fun onAdFailedToLoad(error: AdRequestError) {
                Log.e("YandexAds", "Failed to load: ${error.description}")
            }
        })
        loadAd()
    }

    private fun loadAd() {
        val adConfig = AdRequestConfiguration.Builder(yandexRewardedKey).build()
        loader.loadAd(adConfig)
    }

    fun showAd(
        onReward: () -> Unit,
        onAdFailedToShow: () -> Unit
    ) {
        rewardedAd?.apply {
            setAdEventListener(object : RewardedAdEventListener {
                override fun onRewarded(reward: Reward) {
                    onReward()
                }

                override fun onAdDismissed() {
                    rewardedAd = null
                    loadAd()
                }

                override fun onAdFailedToShow(adError: AdError) {
                    onAdFailedToShow()
                    rewardedAd = null
                    Log.e("YandexAds", "Failed to show: ${adError.description}")
                }

                override fun onAdShown() {}
                override fun onAdClicked() {}
                override fun onAdImpression(data: ImpressionData?) {}
            })
            show(activity)
        } ?: onAdFailedToShow()

    }

    fun clear() {
        rewardedAd?.setAdEventListener(null)
        rewardedAd = null
        loader.setAdLoadListener(null)
    }
}