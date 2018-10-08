package com.example.mdtk.citasapp.aplicacion;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.example.mdtk.citasapp.sync.Sincronizacion;
import com.example.mdtk.citasapp.constantes.G;
import com.example.mdtk.citasapp.proveedor.Contrato;
import com.example.mdtk.citasapp.volley.Utils.LruBitmapCache;
//import com.example.tiburcio.ejemploproveedorcontenido.volley.Utils.LruBitmapCache;

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
    public static final String ACCOUNT = "Convalidaciones";
    // Sync interval constants
    public static final long SYNC_INTERVAL = G.SYNC_INTERVAL; // cada 60 segundos

    private Sincronizacion sincronizacion;


    // Instance fields
    Account mAccount;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        resolvedor = getContentResolver();

        activarSincronizacion();
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

    private void activarSincronizacion() {
        // Create the dummy account
        mAccount = CreateSyncAccount(this);
        //Se lanza la sincronización siempre que hay conexión de INTERNET

        //resolvedor.setIsSyncable(mAccount, AUTHORITY, 1); //Creo que no hace falta ponerlo porque ya está en el proveedor en el manifest android:syncable="true"
        resolvedor.setSyncAutomatically(mAccount, AUTHORITY, true);
        resolvedor.setMasterSyncAutomatically(true);
        resolvedor.addPeriodicSync(mAccount, AUTHORITY, Bundle.EMPTY, SYNC_INTERVAL);
    }

    public Sincronizacion getSincronizacion() {
        return sincronizacion;
    }

    public void setSincronizacion(Sincronizacion sincronizacion) {
        this.sincronizacion = sincronizacion;
    }
}
