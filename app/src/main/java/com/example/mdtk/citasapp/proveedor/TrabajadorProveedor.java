package com.example.mdtk.citasapp.proveedor;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.example.mdtk.citasapp.constantes.G;
import com.example.mdtk.citasapp.pojo.Trabajador;

import java.util.ArrayList;
import java.util.List;

public class TrabajadorProveedor {
    static public ArrayList<Trabajador> readAllRecord(ContentResolver contentResolver){
        Uri uri = Contrato.Trabajador.CONTENT_URI;
        String[] projection = {Contrato.Trabajador._ID, Contrato.Trabajador.NOMBRES, Contrato.Trabajador.TELEFONO};
        Cursor cursor = contentResolver.query(uri,projection,null,null,null);
        ArrayList<com.example.mdtk.citasapp.pojo.Trabajador> trabajadors = new ArrayList<>();

        while (cursor.moveToNext()) {
            com.example.mdtk.citasapp.pojo.Trabajador trabajador = new com.example.mdtk.citasapp.pojo.Trabajador();
            trabajador.setID(cursor.getInt(cursor.getColumnIndex(Contrato.Trabajador._ID)));
            trabajador.setNombres(cursor.getString(cursor.getColumnIndex(Contrato.Trabajador.NOMBRES)));
            trabajador.setTelefono(cursor.getString(cursor.getColumnIndex(Contrato.Trabajador.TELEFONO)));
            trabajadors.add(trabajador);
        }
        return trabajadors;
    }

    static public com.example.mdtk.citasapp.pojo.Trabajador validarLogin(ContentResolver resolver, String telefono){
        Uri uri = Contrato.Trabajador.CONTENT_URI;
        String[] projection = {Contrato.Trabajador._ID, Contrato.Trabajador.NOMBRES};
        String selection= Contrato.Trabajador.TELEFONO + " = '"+telefono+"'";
        Cursor cursor = resolver.query(uri,projection,selection,null,null);
        com.example.mdtk.citasapp.pojo.Trabajador trabajador = new com.example.mdtk.citasapp.pojo.Trabajador();

        if(cursor.moveToFirst()){
            trabajador.setID(cursor.getInt(cursor.getColumnIndex(Contrato.Trabajador._ID)));
            trabajador.setNombres(cursor.getString(cursor.getColumnIndex(Contrato.Trabajador.NOMBRES)));
            return trabajador;
        }
        return null;
    }

    public static Uri insertRecord(ContentResolver resolver, Trabajador trabajador){
        Uri uri = Contrato.Trabajador.CONTENT_URI;
        ContentValues values = new ContentValues();
        values.put(Contrato.Trabajador._ID, trabajador.getID());
        values.put(Contrato.Trabajador.NOMBRES, trabajador.getNombres());
        values.put(Contrato.Trabajador.TELEFONO, trabajador.getTelefono());
        return resolver.insert(uri, values);
    }

    public static void updateRecord(ContentResolver resolver, Trabajador trabajador){
        Uri uri = Uri.parse(Contrato.Trabajador.CONTENT_URI +"/"+trabajador.getID());
        ContentValues values = new ContentValues();
        values.put(Contrato.Trabajador.NOMBRES, trabajador.getNombres());
        values.put(Contrato.Trabajador.TELEFONO, trabajador.getTelefono());
        resolver.update(uri, values,null,null);
    }
}
