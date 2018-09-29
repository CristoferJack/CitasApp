package com.example.mdtk.citasapp.proveedor;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.SparseArray;

public class ProveedorDeContenido extends ContentProvider {
    //private static final String LOGTAG = "Tiburcio - ProveedorDeContenido";

    private static final int CITA_ONE_REG = 1;
    private static final int CITA_ALL_REGS = 2;
    private static final int EMPLEADO_ONE_REG = 3;
    private static final int EMPLEADO_ALL_REGS = 4;
    private static final int LOGIN_ONE_REG = 5;
    private static final int LOGIN_ALL_REGS = 6;

    private SQLiteDatabase sqlDB;
    public DatabaseHelper dbHelper;
    private static final String DATABASE_NAME = "Programate.db";
    private static final int DATABASE_VERSION = 8;

    private static final String CITA_TABLE_NAME = "Cita";
    private static final String EMPLEADO_TABLE_NAME = "Empleado";
    private static final String LOGIN_TABLE_NAME = "Login";

    // Indicates an invalid content URI
    public static final int INVALID_URI = -1;

    // Defines a helper object that matches content URIs to table-specific parameters
    private static final UriMatcher sUriMatcher;

    // Stores the MIME types served by this provider
    private static final SparseArray<String> sMimeTypes;

    /*
     * Initializes meta-data used by the content provider:
     * - UriMatcher that maps content URIs to codes
     * - MimeType array that returns the custom MIME type of a table
     */
    static {

        // Creates an object that associates content URIs with numeric codes
        sUriMatcher = new UriMatcher(0);

        /*
         * Sets up an array that maps content URIs to MIME types, via a mapping between the
         * URIs and an integer code. These are custom MIME types that apply to tables and rows
         * in this particular provider.
         */
        sMimeTypes = new SparseArray<String>();

        // Adds a URI "match" entry that maps picture URL content URIs to a numeric code

        sUriMatcher.addURI(
                Contrato.AUTHORITY,
                CITA_TABLE_NAME,
                CITA_ALL_REGS);
        sUriMatcher.addURI(
                Contrato.AUTHORITY,
                CITA_TABLE_NAME + "/#",
                CITA_ONE_REG);

        sUriMatcher.addURI(
                Contrato.AUTHORITY,
                EMPLEADO_TABLE_NAME,
                EMPLEADO_ALL_REGS);
        sUriMatcher.addURI(
                Contrato.AUTHORITY,
                EMPLEADO_TABLE_NAME + "/#",
                EMPLEADO_ONE_REG);

        sUriMatcher.addURI(
                Contrato.AUTHORITY,
                LOGIN_TABLE_NAME,
                LOGIN_ALL_REGS);
        sUriMatcher.addURI(
                Contrato.AUTHORITY,
                LOGIN_TABLE_NAME + "/#",
                LOGIN_ONE_REG);

        // Specifies a custom MIME type for the picture URL table

        sMimeTypes.put(
                CITA_ALL_REGS,
                "vnd.android.cursor.dir/vnd." +
                        Contrato.AUTHORITY + "." + CITA_TABLE_NAME);
        sMimeTypes.put(
                CITA_ONE_REG,
                "vnd.android.cursor.item/vnd."+
                        Contrato.AUTHORITY + "." + CITA_TABLE_NAME);

        sMimeTypes.put(
                EMPLEADO_ALL_REGS,
                "vnd.android.cursor.dir/vnd." +
                        Contrato.AUTHORITY + "." + EMPLEADO_TABLE_NAME);
        sMimeTypes.put(
                EMPLEADO_ONE_REG,
                "vnd.android.cursor.item/vnd."+
                        Contrato.AUTHORITY + "." + EMPLEADO_TABLE_NAME);

        sMimeTypes.put(
                LOGIN_ALL_REGS,
                "vnd.android.cursor.dir/vnd." +
                        Contrato.AUTHORITY + "." + LOGIN_TABLE_NAME);
        sMimeTypes.put(
                LOGIN_ONE_REG,
                "vnd.android.cursor.item/vnd."+
                        Contrato.AUTHORITY + "." + LOGIN_TABLE_NAME);
    }

    public static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onOpen(SQLiteDatabase db) {
            super.onOpen(db);

            //if (!db.isReadOnly()){
            //Habilitamos la integridad referencial
            db.execSQL("PRAGMA foreign_keys=ON;");
            //}
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // create table to store

            db.execSQL("Create table "
                            + CITA_TABLE_NAME
                            + "( _id INTEGER PRIMARY KEY ON CONFLICT ROLLBACK AUTOINCREMENT, "
                            + Contrato.Cita.SERVICIO + " TEXT , "
                            + Contrato.Cita.CLIENTE + " TEXT , "
                            + Contrato.Cita.NOTA + " TEXT , "
                            + Contrato.Cita.FECHA_HORA + " TEXT , "
                            + Contrato.Cita.EMPLEADO_ID + " TEXT ); "
            );

            db.execSQL("Create table "
                    + EMPLEADO_TABLE_NAME
                    + "( _id INTEGER PRIMARY KEY ON CONFLICT ROLLBACK AUTOINCREMENT, "
                    + Contrato.Empleado.NOMBRES + " TEXT , "
                    + Contrato.Empleado.TELEFONO + " TEXT ); "
            );

            db.execSQL("Create table "
                    + LOGIN_TABLE_NAME
                    + "( _id INTEGER PRIMARY KEY ON CONFLICT ROLLBACK AUTOINCREMENT, "
                    + Contrato.Login.EMPLEADO_ID + " TEXT , "
                    + Contrato.Login.ESTADO + " TEXT ); "
            );

            inicializarDatos(db);

        }

        void inicializarDatos(SQLiteDatabase db){

            /*db.execSQL("INSERT INTO " + CITA_TABLE_NAME + " (" +  Contrato.Cita._ID + "," + Contrato.Cita.SERVICIO + "," + Contrato.Cita.CLIENTE + "," + Contrato.Cita.NOTA + "," + Contrato.Cita.FECHA_HORA + "," + Contrato.Cita.EMPLEADO_ID + ") " +
                    "VALUES (1,'Reparacion de motor','Cristofer B. Goode','Reparaccion de motor del carro color verde marca AUDI..','2018-09-01 10:10',2)");
            db.execSQL("INSERT INTO " + CITA_TABLE_NAME + " (" +  Contrato.Cita._ID + "," + Contrato.Cita.SERVICIO + "," + Contrato.Cita.CLIENTE + "," + Contrato.Cita.NOTA + "," + Contrato.Cita.FECHA_HORA + "," + Contrato.Cita.EMPLEADO_ID + ") " +
                    "VALUES (2,'Cambio de aceite','Johnnie Walker','Cambio de aceite del carro azul Ford Mustang','2018-09-02 11:11',2)");*/

            db.execSQL("INSERT INTO " + EMPLEADO_TABLE_NAME + " (" +  Contrato.Empleado._ID + "," + Contrato.Empleado.NOMBRES + "," + Contrato.Empleado.TELEFONO + ") " +
                    "VALUES (1,'Naaminn','997271506')");
            db.execSQL("INSERT INTO " + EMPLEADO_TABLE_NAME + " (" +  Contrato.Empleado._ID + "," + Contrato.Empleado.NOMBRES + "," + Contrato.Empleado.TELEFONO + ") " +
                    "VALUES (2,'Celeste','991329096')");
            db.execSQL("INSERT INTO " + EMPLEADO_TABLE_NAME + " (" +  Contrato.Empleado._ID + "," + Contrato.Empleado.NOMBRES + "," + Contrato.Empleado.TELEFONO + ") " +
                    "VALUES (3,'Eloy','994172431')");
            db.execSQL("INSERT INTO " + EMPLEADO_TABLE_NAME + " (" +  Contrato.Empleado._ID + "," + Contrato.Empleado.NOMBRES + "," + Contrato.Empleado.TELEFONO + ") " +
                    "VALUES (4,'Richard','994143183')");
            db.execSQL("INSERT INTO " + EMPLEADO_TABLE_NAME + " (" +  Contrato.Empleado._ID + "," + Contrato.Empleado.NOMBRES + "," + Contrato.Empleado.TELEFONO + ") " +
                    "VALUES (5,'Victoria','977422500')");

            db.execSQL("INSERT INTO " + LOGIN_TABLE_NAME + " (" +  Contrato.Login._ID + "," + Contrato.Login.EMPLEADO_ID + "," + Contrato.Login.ESTADO + ") " +
                    "VALUES (1,0,0)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + CITA_TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + EMPLEADO_TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + LOGIN_TABLE_NAME);

            onCreate(db);
        }

    }

    public ProveedorDeContenido() {
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }


    @Override
    public boolean onCreate() {
        dbHelper = new DatabaseHelper(getContext());
        return (dbHelper == null) ? false : true;
    }

    public void resetDatabase() {
        dbHelper.close();
        dbHelper = new DatabaseHelper(getContext());
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        sqlDB = dbHelper.getWritableDatabase();

        String table = "";
        switch (sUriMatcher.match(uri)) {
            case CITA_ALL_REGS:
                table = CITA_TABLE_NAME;
                break;
            case EMPLEADO_ALL_REGS:
                table = EMPLEADO_TABLE_NAME;
                break;
            case LOGIN_ALL_REGS:
                table = LOGIN_TABLE_NAME;
                break;
        }

        long rowId = sqlDB.insert(table, "", values);

        if (rowId > 0) {
            Uri rowUri = ContentUris.appendId(
                    uri.buildUpon(), rowId).build();
            getContext().getContentResolver().notifyChange(rowUri, null);
            return rowUri;
        }
        throw new SQLException("Failed to insertRecord row into " + uri);
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        sqlDB = dbHelper.getWritableDatabase();
        // insertRecord record in user table and get the row number of recently inserted record

        String table = "";
        switch (sUriMatcher.match(uri)) {
            case CITA_ONE_REG:
                if (null == selection) selection = "";
                selection += Contrato.Cita._ID + " = "
                        + uri.getLastPathSegment();
                table = CITA_TABLE_NAME;
                break;
            case CITA_ALL_REGS:
                table = CITA_TABLE_NAME;
                break;

            case EMPLEADO_ONE_REG:
                if (null == selection) selection = "";
                selection += Contrato.Empleado._ID + " = "
                        + uri.getLastPathSegment();
                table = EMPLEADO_TABLE_NAME;
                break;
            case EMPLEADO_ALL_REGS:
                table = EMPLEADO_TABLE_NAME;
                break;

            case LOGIN_ONE_REG:
                if (null == selection) selection = "";
                selection += Contrato.Login._ID + " = "
                        + uri.getLastPathSegment();
                table = LOGIN_TABLE_NAME;
                break;
            case LOGIN_ALL_REGS:
                table = LOGIN_TABLE_NAME;
                break;
        }
        int rows = sqlDB.delete(table, selection, selectionArgs);
        if (rows > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
            return rows;
        }
        throw new SQLException("Failed to deleteRecord row into " + uri);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = null;

        switch (sUriMatcher.match(uri)) {
            case CITA_ONE_REG:
                if (null == selection) selection = "";
                selection += Contrato.Cita._ID + " = "
                        + uri.getLastPathSegment();
                qb.setTables(CITA_TABLE_NAME);
                break;
            case CITA_ALL_REGS:
                if (TextUtils.isEmpty(sortOrder)) sortOrder =
                        Contrato.Cita._ID + " ASC";
                qb.setTables(CITA_TABLE_NAME);
                break;

            case EMPLEADO_ONE_REG:
                if (null == selection) selection = "";
                selection += Contrato.Empleado._ID + " = "
                        + uri.getLastPathSegment();
                qb.setTables(EMPLEADO_TABLE_NAME);
                break;
            case EMPLEADO_ALL_REGS:
                if (TextUtils.isEmpty(sortOrder)) sortOrder =
                        Contrato.Empleado._ID + " ASC";
                qb.setTables(EMPLEADO_TABLE_NAME);
                break;

            case LOGIN_ONE_REG:
                if (null == selection) selection = "";
                selection += Contrato.Login._ID + " = "
                        + uri.getLastPathSegment();
                qb.setTables(LOGIN_TABLE_NAME);
                break;
            case LOGIN_ALL_REGS:
                if (TextUtils.isEmpty(sortOrder)) sortOrder =
                        Contrato.Login._ID + " ASC";
                qb.setTables(LOGIN_TABLE_NAME);
                break;
        }

        Cursor c;
        c = qb.query(db, projection, selection, selectionArgs, null, null,
                        sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);

        return c;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        sqlDB = dbHelper.getWritableDatabase();
        // insertRecord record in user table and get the row number of recently inserted record

        String table = "";
        switch (sUriMatcher.match(uri)) {
            case CITA_ONE_REG:
                if (null == selection) selection = "";
                selection += Contrato.Cita._ID + " = "
                        + uri.getLastPathSegment();
                table = CITA_TABLE_NAME;
                break;
            case CITA_ALL_REGS:
                table = CITA_TABLE_NAME;
                break;

            case LOGIN_ONE_REG:
                if (null == selection) selection = "";
                selection += Contrato.Login._ID + " = "
                        + uri.getLastPathSegment();
                table = LOGIN_TABLE_NAME;
                break;
            case LOGIN_ALL_REGS:
                table = LOGIN_TABLE_NAME;
                break;
        }

        int rows = sqlDB.update(table, values, selection, selectionArgs);
        if (rows > 0) {
            getContext().getContentResolver().notifyChange(uri, null);

            return rows;
        }
        throw new SQLException("Failed to updateRecord row into " + uri);
    }
}
