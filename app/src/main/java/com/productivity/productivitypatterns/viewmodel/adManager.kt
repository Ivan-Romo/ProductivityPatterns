package com.productivity.productivitypatterns.viewmodel

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class AdManager(val context: Context) {
    var mInterstitialAd: InterstitialAd? = null
    //ESTAS SON LAS DE DEBUG
//        private val interstitialId = "ca-app-pub-3940256099942544/1033173712"
//        private val bannerId = "ca-app-pub-3940256099942544/9214589741"


    //ESTAS NO SOSIO. CUIDADO
    private val interstitialId = "ca-app-pub-4336258340172398/9910781745"
    private val bannerId = "ca-app-pub-4336258340172398/3076441358"

    init {
        loadInterstitialAd({})
    }

    fun loadInterstitialAd(adStatus: (Boolean) -> Unit) {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(context, interstitialId, adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(error: LoadAdError) {
                mInterstitialAd = null
                Log.i("AD_TAG", "onAdFailedToLoad: ${error.message}")
                adStatus(false)
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                mInterstitialAd = interstitialAd
                Log.i("AD_TAG", "onAdLoaded:")
                adStatus(true)
            }
        })
    }

    fun showInterstitialAd(activity: Activity, onAdClosed: () -> Unit) {
        mInterstitialAd?.let { ad ->
            ad.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    mInterstitialAd = null
                    onAdClosed()
                    Log.i("AD_TAG", "onAdDismissedFullScreenContent")
                }

                override fun onAdImpression() {
                    Log.i("AD_TAG", "onAdImpression")
                }

                override fun onAdClicked() {
                    Log.i("AD_TAG", "onAdClicked")
                }
            }
            ad.show(activity)
        }
    }

    @Composable
    fun loadBannerAd(modifier: Modifier) {

        Column(modifier = modifier){
            AndroidView(
                modifier = Modifier.fillMaxWidth(),
                factory = {context ->
                    loadBannerAd()
                }
            )
        }

    }
    fun loadBannerAd() : AdView{
       return AdView(context).apply {
            setAdSize(AdSize.BANNER)
            adUnitId = bannerId
            loadAd(AdRequest.Builder().build())
        }
    }
}