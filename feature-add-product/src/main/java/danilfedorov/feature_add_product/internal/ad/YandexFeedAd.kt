package danilfedorov.feature_add_product.internal.ad

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yandex.mobile.ads.common.ImpressionData
import com.yandex.mobile.ads.feed.FeedAd
import com.yandex.mobile.ads.feed.FeedAdAdapter
import com.yandex.mobile.ads.feed.FeedAdEventListener

@Composable
fun YandexFeedAd(
    modifier: Modifier = Modifier,
    feedAd: FeedAd
) {

    AndroidView(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .clip(MaterialTheme.shapes.medium),
        factory = { ctx ->

            val adapter = FeedAdAdapter(feedAd).apply {
                 object : FeedAdEventListener {
                    override fun onAdClicked() {}
                    override fun onImpression(impressionData: ImpressionData?) {}
                }
            }

            RecyclerView(ctx).apply {
                layoutManager = LinearLayoutManager(ctx)
                this.adapter = adapter
                isNestedScrollingEnabled = true
            }
        }
    )
}