package com.example.mdtk.citasapp.sync;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;

import com.example.mdtk.citasapp.R;
import com.example.mdtk.citasapp.cita.CitaActivity;

public class NotificacionPeriodica extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        Intent pendientes_intent = new Intent(context,CitaActivity.class);
        pendientes_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,100,pendientes_intent,PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.Builder notificacion = new NotificationCompat.Builder(context,"Actividades" )
                .setContentIntent(pendingIntent)
                .setContentTitle("Recordatorio")
                .setContentText("Tienes notificaciones pendientes")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        notificationManager.notify(100,notificacion.build());
    }
}


class NotificacionActividadPendiente extends AsyncTask<String, Void, String> {

    private Notification notificacion;
    private Context ApplicationContext;

    public NotificacionActividadPendiente(Context context) {
        ApplicationContext = context;
    }

    @Override
    protected String doInBackground(String... params) {

        return null;
    }

}