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
import com.example.mdtk.citasapp.aplicacion.AppController;
import com.example.mdtk.citasapp.cita.CitaPendienteActivity;

import java.util.Calendar;

import static com.example.mdtk.citasapp.proveedor.CitaProveedor.cantidadCitasPendientesDia;
import static com.example.mdtk.citasapp.proveedor.LoginProveedor.getDefault;

public class NotificacionPeriodica extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        Intent pendientes_intent = new Intent(context,CitaPendienteActivity.class);
        pendientes_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,100,pendientes_intent,PendingIntent.FLAG_UPDATE_CURRENT);


        Calendar calFecha = Calendar.getInstance();
        int hour = calFecha.get(Calendar.HOUR_OF_DAY);
        if(hour>6){
            int id_trabajador_registrado = getDefault(AppController.getResolvedor());
            if(id_trabajador_registrado!=0){
                int citasPendientes = cantidadCitasPendientesDia(AppController.getResolvedor(),id_trabajador_registrado);
                if(citasPendientes>0){
                    NotificationCompat.Builder notificacion = new NotificationCompat.Builder(context,"Actividades" )
                            .setContentIntent(pendingIntent)
                            .setContentTitle("Recordatorio")
                            .setContentText("Tienes notificaciones pendientes")
                            .setSmallIcon(R.drawable.ic_notificacion)
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setAutoCancel(true);

                    notificationManager.notify(100,notificacion.build());
                }
            }
        }
    }
}