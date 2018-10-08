package com.example.mdtk.citasapp.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

import com.example.mdtk.citasapp.aplicacion.AppController;

/**
 * Handle the transfer of data between a server and an
 * app, using the Android sync adapter framework.
 */
//@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class SyncAdapter extends AbstractThreadedSyncAdapter {
    // Global variables
    // Define a variable to contain a content resolver instance
    //ContentResolver mContentResolver;
    //Sincronizacion sincronizacion;
    /**
     * Set up the sync adapter
     */
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        //mContentResolver = context.getContentResolver();
        //sincronizacion = new Sincronizacion(context);
        AppController.getInstance().setSincronizacion(new Sincronizacion(context));
    }
    
    /**
     * Set up the sync adapter. This form of the
     * constructor maintains compatibility with Android 3.0
     * and later platform versions
     */
    public SyncAdapter(
            Context context,
            boolean autoInitialize,
            boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        //mContentResolver = context.getContentResolver();
        //sincronizacion = new Sincronizacion(context);
        AppController.getInstance().setSincronizacion(new Sincronizacion(context));
    }

	@Override
	public void onPerformSync(Account arg0, Bundle arg1, String arg2,
                              ContentProviderClient arg3, SyncResult arg4) {
		// TODO Auto-generated method stub

		AppController.getInstance().getSincronizacion().sincronizar();
	}
}