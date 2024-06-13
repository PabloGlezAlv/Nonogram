package com.example.androidengine;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.engine.Ads;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

public class AndroidAds implements Ads {

    AdView banner_;
    RewardedAd rewardedAd_;
    AndroidEngine engine_;

    Activity activity;

    public AndroidAds() {}

    @Override
    public void init() {
        // Inicializacion de anuncios
    }

    public void setEngine(AndroidEngine engine) {
        engine_ = engine;
    }

    public void setActivity(Activity c) { activity = c; }
    public void setBanner(AdView b) { banner_ = b; }
    public void setRewardedAd(RewardedAd r) {rewardedAd_ = r; }

    //Envia al hilo principal el metodo que muestra el anuncio con recompensa
    @Override
    public void showRewardedAd() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (rewardedAd_ == null) {
                    System.out.println("Anuncio no cargado segundo.");
                } else {
                    rewardedAd_.show(activity, new OnUserEarnedRewardListener() {
                        @Override
                        public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                            // Manda el mensaje al motor de que hay recompensa
                            System.out.println("Anuncio mostrado");
                            engine_.getReward();
                        }
                    });
                }
            }
        });
    }

    //Muestra el banner
    @Override
    public void hideBanner() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                banner_.setVisibility(View.INVISIBLE);

            }
        });
    }

    //Oculta el banner
    @Override
    public void showBanner() {activity.runOnUiThread(new Runnable() {
        @Override
        public void run() {
            banner_.setVisibility(View.VISIBLE);
        }
    });
    }

}
