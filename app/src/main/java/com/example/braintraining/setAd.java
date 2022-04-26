package com.example.braintraining;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yandex.mobile.ads.banner.AdSize;
import com.yandex.mobile.ads.banner.BannerAdEventListener;
import com.yandex.mobile.ads.banner.BannerAdView;
import com.yandex.mobile.ads.common.AdRequest;
import com.yandex.mobile.ads.common.AdRequestError;
import com.yandex.mobile.ads.common.ImpressionData;

public class setAd implements Runnable{

    private static final String AdUnitId = "adf-279013/966631";
    private BannerAdView mBannerAdView;

    public setAd(BannerAdView mBannerAdView){
        this.mBannerAdView = mBannerAdView;
    }

    @Override
    public void run() {
        final AdRequest adRequest = new AdRequest.Builder().build();

        mBannerAdView.setAdUnitId(AdUnitId);
        mBannerAdView.setAdSize(AdSize.BANNER_320x50);
        mBannerAdView.setBannerAdEventListener(new BannerAdEventListener() {
            @Override
            public void onAdLoaded() { }

            @Override
            public void onAdFailedToLoad(@NonNull AdRequestError adRequestError) { }

            @Override
            public void onAdClicked() { }

            @Override
            public void onLeftApplication() { }

            @Override
            public void onReturnedToApplication() { }

            @Override
            public void onImpression(@Nullable ImpressionData impressionData) { }
        });

        // Загрузка объявления
        mBannerAdView.loadAd(adRequest);
    }
}
