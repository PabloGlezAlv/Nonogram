package pckg.practica_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.LinearLayout;

import com.example.androidengine.AndroidAds;
import com.example.androidengine.AndroidEngine;
import com.example.game.Game;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {
    private static final long DELAY_TIME = 10 * 1000;   // Tiempo que espera para notificar al jugador tras salir de la app
    private static int COINS_REWARD = 25;               // Premio recibido al volver a la app pulsando la notificacion

    private AdView banner_;
    private RewardedAd rewardedAd_;
    private AndroidEngine e;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //View en la que se pinta el juego
        SurfaceView view = new SurfaceView(this);
        //View que servira para dibujar el juego y el banner
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);

        //Iniciar la SDK de anuncios
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });

        MobileAds.setRequestConfiguration(
                new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("ABCDEF012345"))
                        .build());

        //Inicializacion de banner
        banner_ = new AdView(this);
        banner_.setId(View.generateViewId());
        banner_.setAdSize(AdSize.BANNER);
        banner_.setAdUnitId("ca-app-pub-3940256099942544/6300978111");

        AdRequest adRequest = new AdRequest.Builder().build();
        banner_.loadAd(adRequest);

        //Se anade la vista del juego y la vista del banner a la misma view compuesta
        ll.addView(banner_);
        ll.addView(view);
        setContentView(ll);

        AndroidAds ads = new AndroidAds();
        ads.setActivity(this);
        ads.setBanner(banner_);

        //Inicializacion de RewardedAd
        loadRewardedAd(adRequest, ads);

        Game g;

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        g = new Game();
        e = new AndroidEngine(view, sensorManager);
        g.setEngine(e);
        e.setGame(g);
        e.setAds(ads);
        ads.setEngine(e);

        e.resume();
    }

    @Override
    protected void onStop() {
        super.onStop();
        /*NOTIFICACIONES*/
        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(NotificationWorker.class)
                .setInitialDelay(DELAY_TIME, TimeUnit.MILLISECONDS)
                .build();
        // Enviar la solicitud de trabajo a WorkManager
        WorkManager.getInstance(this).enqueue(workRequest);

        //Avisar al engine de que hemos salido de la app
        if(e!=null) e.onPause();
    }

    @Override
    protected void onPause() {
        super.onPause();

        //Avisar al engine de que hemos salido de la app
        if(e!=null) e.onPause();
    }


    @Override
    protected void onResume() {
        super.onResume();

        // Recupera el "extra" que indica si se debe llamar al método AnadeMonedas() o no
        boolean b = getIntent().getBooleanExtra("llamar_anade_monedas", false);

        if (b) {
            e.OnNotificationClicked(COINS_REWARD);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //Avisar al engine de que hemos salido de la app
        if(e!=null) e.onPause();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    // Metodo para ocultar las barras superiores de Android
    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    //Carga de anuncio recompensado
    public void loadRewardedAd(AdRequest adRequest, AndroidAds ads) {
        RewardedAd.load(this, "ca-app-pub-3940256099942544/5224354917",
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        rewardedAd_ = null;
                        System.out.println("Anuncio no cargado primero");
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        rewardedAd_ = rewardedAd;
                        rewardedAd_.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdClicked() {
                                //Se llama cuando se pulsa el anuncio
                                System.out.println("Anuncio pulsado");
                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Se llama cuando se evita el anuncio
                                rewardedAd_ = null;
                                System.out.println("Anuncio pulsado");
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when ad fails to show.
                                System.out.println("Erro al enseñar el anuncio");
                                rewardedAd_ = null;
                            }

                            @Override
                            public void onAdImpression() {
                                // Called when an impression is recorded for an ad.
                                System.out.println("Anuncio comprobo una impression");
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when ad is shown.
                                System.out.println("Se ha enseñado el anuncio a pantalla completa");
                                loadRewardedAd(adRequest, ads);
                            }
                        });
                        System.out.println("Anuncio cargado correctamente primero.");
                        ads.setRewardedAd(rewardedAd_);
                    }
                });
    }
}