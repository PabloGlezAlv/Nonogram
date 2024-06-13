package pckg.practica_1;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

//Clase encargada de ser llamada por un WorkManager para lanzar una notificacion push tras haber salido de la aplicacion durante 10 segundos
public class NotificationWorker extends Worker{

    private static final String CHANNEL_ID = "1";
    private static final int NOTIFICATION_ID = 1;


    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {    //Metodo llamado por el WorkManager
        // Crear el canal de notificación si es necesario
        createNotificationChannel();

        // Crear la notificación
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_stat_category)
                .setContentTitle("En Nonogramas te echamos de menos :(")
                .setContentText("Vuelve con nosotros para recibir una recompensa!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true) // Establece la opción para cancelar la notificación al pulsarla
                .setVibrate(new long[0]);

        // Crear un intent para abrir la aplicación cuando se haga clic en la notificación
        Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
        notificationIntent.putExtra("llamar_anade_monedas", true);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
        builder.setContentIntent(pendingIntent);

        // Enviar la notificación
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, builder.build());

        return Result.success();
    }

    //Crea e inicializa el canal de notificacion si la version lo requiere
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "My channel", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("My channel description");
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            NotificationManager notificationManager = getApplicationContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
