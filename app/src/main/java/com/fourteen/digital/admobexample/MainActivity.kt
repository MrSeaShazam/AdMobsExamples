package com.fourteen.digital.admobexample

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.ads.nativetemplates.NativeTemplateStyle
import com.google.android.ads.nativetemplates.TemplateView
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.NativeAd


class MainActivity : AppCompatActivity() {
    private var mInterstitialAd: InterstitialAd? = null
    private final var TAG = "MainActivity"

    lateinit var fullScreenAdbtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeData()

        fullScreenAdbtn.setOnClickListener {
            loadInterstitialAd()
            showInterstitialAd()
        }

    }



    private fun initializeData() {

        //initialize ads
        MobileAds.initialize(this) {}
        //load interstitial add
        loadInterstitialAd()
        //load banner ads
        loadBannerAd()
        //load native ads
        loadNativeAd()
        fullScreenAdbtn = findViewById(R.id.fullScreenAdbtn)
    }

    private fun loadNativeAd() {
        val adLoader = AdLoader.Builder(this, "ca-app-pub-3940256099942544/2247696110")
            .forNativeAd(object : NativeAd.OnNativeAdLoadedListener{
                override fun onNativeAdLoaded(nativeAd: NativeAd) {
                   val styles = NativeTemplateStyle.Builder().withMainBackgroundColor(ColorDrawable(Color.WHITE)).build()
                    val my_template = findViewById<TemplateView>(R.id.my_template)
                    my_template.setStyles(styles)
                    my_template.setNativeAd(nativeAd)
                }

            }).build()
        adLoader.loadAd(AdRequest.Builder().build())

        val refreshAdbtn = findViewById<Button>(R.id.refreshAdbtn)
        refreshAdbtn.setOnClickListener {
            adLoader.loadAd(AdRequest.Builder().build())

        }
    }

    private fun loadBannerAd() {
        val adView = AdView(this)

        adView.adSize = AdSize.BANNER

        adView.adUnitId = "ca-app-pub-3940256099942544/6300978111"

        //set add to view in xml
       val mAdView = findViewById<AdView>(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        mAdView.adListener = object: AdListener() {
            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            override fun onAdFailedToLoad(adError : LoadAdError) {
                // Code to be executed when an ad request fails.
            }

            override fun onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            override fun onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        }

    }

    private fun showInterstitialAd() {
        if (mInterstitialAd != null) {
            mInterstitialAd?.show(this)
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.")
        }
    }

    private fun loadInterstitialAd() {

        var adRequest = AdRequest.Builder().build()

        InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712", adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d(TAG, adError?.message)
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                Log.d(TAG, "Ad was loaded.")
                mInterstitialAd = interstitialAd
            }
        })

        mInterstitialAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                Log.d(TAG, "Ad was dismissed.")
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                Log.d(TAG, "Ad failed to show.")
            }

            override fun onAdShowedFullScreenContent() {
                Log.d(TAG, "Ad showed fullscreen content.")
                mInterstitialAd = null
            }
        }
    }
}