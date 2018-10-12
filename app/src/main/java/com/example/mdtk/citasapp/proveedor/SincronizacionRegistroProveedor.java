package com.example.mdtk.citasapp.proveedor;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.example.mdtk.citasapp.pojo.SincronizacionRegistro;

import java.util.ArrayList;
import java.util.List;

public class SincronizacionRegistroProveedor {
    public static void insertRecord(ContentResolver resolver, SincronizacionRegistro SincronizacionRegistro){
        Uri uri = Contrato.Sincronizacion.CONTENT_URI;
        ContentValues values = new ContentValues();
        values.put(Contrato.Sincronizacion.ID_CITA, SincronizacionRegistro.getId_cita());
        values.put(Contrato.Sincronizacion.ID_TRABAJADOR_REGISTRO, SincronizacionRegistro.getId_trabajador_registro());
        values.put(Contrato.Sincronizacion.OPERACION, SincronizacionRegistro.getOperacion());
        resolver.insert(uri, values);
    }

    public static void deleteRecord(ContentResolver resolver, int SincronizacionId){
        Uri uri = Uri.parse(Contrato.Sincronizacion.CONTENT_URI +"/"+ SincronizacionId);
        resolver.delete(uri, null,null);
    }

    public static void deleteAllRecord(ContentResolver resolver){
        Uri uri = Uri.parse(Contrato.Sincronizacion.CONTENT_URI +"");
        resolver.delete(uri, null,null);
    }

    public static void updateRecord(ContentResolver resolver, SincronizacionRegistro SincronizacionRegistro){
        Uri uri = Uri.parse(Contrato.Sincronizacion.CONTENT_URI +"/"+ SincronizacionRegistro.getID());
        ContentValues values = new ContentValues();
        values.put(Contrato.Sincronizacion.ID_CITA, SincronizacionRegistro.getId_cita());
        values.put(Contrato.Sincronizacion.ID_TRABAJADOR_REGISTRO, SincronizacionRegistro.getId_trabajador_registro());
        values.put(Contrato.Sincronizacion.OPERACION, SincronizacionRegistro.getOperacion());
        resolver.update(uri, values,null,null);
    }

    static public SincronizacionRegistro readRecord(ContentResolver resolver, int id_sincronizacion){
        Uri uri = Uri.parse(Contrato.Sincronizacion.CONTENT_URI+"/"+id_sincronizacion);

        String[] projection = {
                Contrato.Sincronizacion.ID_CITA, Contrato.Sincronizacion.ID_TRABAJADOR_REGISTRO, Contrato.Sincronizacion.OPERACION,
        };
        Cursor cursor = resolver.query(uri,projection,null,null,null);

        if(cursor.moveToFirst()){
            SincronizacionRegistro SincronizacionRegistro = new SincronizacionRegistro();
            SincronizacionRegistro.setID(id_sincronizacion);
            SincronizacionRegistro.setId_cita(cursor.getInt(cursor.getColumnIndex(Contrato.Sincronizacion.ID_CITA)));
            SincronizacionRegistro.setOperacion(cursor.getInt(cursor.getColumnIndex(Contrato.Sincronizacion.OPERACION)));
            SincronizacionRegistro.setId_trabajador_registro(cursor.getInt(cursor.getColumnIndex(Contrato.Sincronizacion.ID_TRABAJADOR_REGISTRO)));

            return SincronizacionRegistro;
        }
        return null;
    }

    static public ArrayList<SincronizacionRegistro> readAllRecord(ContentResolver resolver){
        Uri uri = Uri.parse(Contrato.Sincronizacion.CONTENT_URI+"");

        String[] projection = {
                Contrato.Sincronizacion.ID_CITA, Contrato.Sincronizacion.ID_TRABAJADOR_REGISTRO,
                Contrato.Sincronizacion.OPERACION,Contrato.Sincronizacion._ID
        };
        Cursor cursor = resolver.query(uri,projection,null,null,null);

        ArrayList<SincronizacionRegistro> sincronizacionRegistros = new ArrayList<>();
        SincronizacionRegistro sincronizacionRegistro;
        while(cursor.moveToNext()){
            sincronizacionRegistro = new SincronizacionRegistro();
            sincronizacionRegistro.setID(cursor.getInt(cursor.getColumnIndex(Contrato.Sincronizacion._ID)));
            sincronizacionRegistro.setId_cita(cursor.getInt(cursor.getColumnIndex(Contrato.Sincronizacion.ID_CITA)));
            sincronizacionRegistro.setOperacion(cursor.getInt(cursor.getColumnIndex(Contrato.Sincronizacion.OPERACION)));
            sincronizacionRegistro.setId_trabajador_registro(cursor.getInt(cursor.getColumnIndex(Contrato.Sincronizacion.ID_TRABAJADOR_REGISTRO)));

            sincronizacionRegistros.add(sincronizacionRegistro);
        }
        return sincronizacionRegistros;
    }
}
