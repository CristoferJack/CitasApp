package com.example.mdtk.citasapp.proveedor;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

public class TrabajadorProveedor {
    static public List<com.example.mdtk.citasapp.pojo.Trabajador> getList (ContentResolver contentResolver){
        Uri uri = Contrato.Trabajador.CONTENT_URI;
        String[] projection = {Contrato.Trabajador._ID, Contrato.Trabajador.NOMBRES, Contrato.Trabajador.TELEFONO};
        Cursor cursor = contentResolver.query(uri,projection,null,null,null);
        List<com.example.mdtk.citasapp.pojo.Trabajador> trabajadors = new ArrayList<>();

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
}
