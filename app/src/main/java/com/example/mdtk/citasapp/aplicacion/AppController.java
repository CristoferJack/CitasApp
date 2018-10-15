package com.example.mdtk.citasapp.aplicacion;

/**
 * Created by Cristofer B. on 09/10/2018.
 */

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SyncRequest;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.example.mdtk.citasapp.sync.NotificacionPeriodica;
import com.example.mdtk.citasapp.sync.Sincronizacion;
import com.example.mdtk.citasapp.constantes.G;
import com.example.mdtk.citasapp.proveedor.Contrato;
import com.example.mdtk.citasapp.volley.Utils.LruBitmapCache;

public class AppController extends Application {

    public static final String TAG = AppController.class
            .getSimpleName();

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    private static AppController mInstance;
    private static ContentResolver resolvedor;

    // The authority for the sync adapter's content provider
    public static final String AUTHORITY = Contrato.AUTHORITY;
    // An account type, in the form of a domain name
    public static final String ACCOUNT_TYPE = "com.example.mdtk.citasapp";
    // The account name
    public static final String ACCOUNT = "SyncProgramate";
    // Sync interval constants
    public static final int SYNC_INTERVAL = G.SYNC_INTERVAL ;//* 180;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;
    public static final int NOTIFICACION_INTERVAL = 1000*60; /* dos horas 1000*60*60*2;*/

    private Sincronizacion sincronizacion;

    NotificationCompat.Builder notificacion;

    // Instance fields
    Account mAccount;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        resolvedor = getContentResolver();

        configurePeriodicSync(SYNC_INTERVAL, SYNC_FLEXTIME);

        activarNotificacionPeriodica();
    }

    private void activarNotificacionPeriodica() {
        AlarmManager manager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

        Intent alarmIntent = new Intent(this, NotificacionPeriodica.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);

        manager.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), NOTIFICACION_INTERVAL, pendingIntent);
    }

    public static ContentResolver getResolvedor(){
        return resolvedor;
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public static Account CreateSyncAccount(Context context) {
        // Create the account type and default account
        Account newAccount = new Account(
                ACCOUNT, ACCOUNT_TYPE);
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(
                        ACCOUNT_SERVICE);
        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
        if (accountManager.addAccountExplicitly(newAccount, null, null)) {
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call context.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */
        } else {
            /*
             * The account exists or some other error occurred. Log this, report it,
             * or handle it internally.
             */
        }
        return newAccount;
    }

    private void configurePeriodicSync(int syncInterval, int flexTime) {
        // Create the dummy account
        mAccount = CreateSyncAccount(this);
        //Se lanza la sincronización siempre que hay conexión de INTERNET

       //resolvedor.setIsSyncable(mAccount, AUTHORITY, 1); //Creo que no hace falta ponerlo porque ya está en el proveedor en el manifest android:syncable="true"
        resolvedor.setSyncAutomatically(mAccount, AUTHORITY, true);
        resolvedor.setMasterSyncAutomatically(true);
        resolvedor.addPeriodicSync(mAccount, AUTHORITY, new Bundle(), syncInterval);//Bundle.EMPTY

/*        //////
        //String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            *//*SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(mAccount, AUTHORITY).
                    setExtras(new Bundle()).build();
            resolvedor.requestSync(request);*//*
            resolvedor.addPeriodicSync(mAccount,
                    AUTHORITY, new Bundle(), flexTime);
        } else {
            resolvedor.addPeriodicSync(mAccount,
                    AUTHORITY, new Bundle(), flexTime);
        }
        resolvedor.setSyncAutomatically(mAccount, AUTHORITY, true);
        resolvedor.setMasterSyncAutomatically(true);*/
    }


    public Sincronizacion getSincronizacion() {
        return sincronizacion;
    }

    public void setSincronizacion(Sincronizacion sincronizacion) {
        this.sincronizacion = sincronizacion;
    }

}
