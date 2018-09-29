package com.example.mdtk.citasapp.proveedor;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.ParseException;
import android.net.Uri;

import com.example.mdtk.citasapp.pojo.Empleado;

import java.util.ArrayList;
import java.util.List;

public class EmpleadoProveedor {
    static public List<Empleado> getList (ContentResolver contentResolver){
        Uri uri = Contrato.Empleado.CONTENT_URI;
        String[] projection = {Contrato.Empleado._ID,Contrato.Empleado.NOMBRES, Contrato.Empleado.TELEFONO};
        Cursor cursor = contentResolver.query(uri,projection,null,null,null);
        List<Empleado> empleados= new ArrayList<>();

        while (cursor.moveToNext()) {
            Empleado empleado = new Empleado();
            empleado.setID(cursor.getInt(cursor.getColumnIndex(Contrato.Empleado._ID)));
            empleado.setNombres(cursor.getString(cursor.getColumnIndex(Contrato.Empleado.NOMBRES)));
            empleado.setTelefono(cursor.getString(cursor.getColumnIndex(Contrato.Empleado.TELEFONO)));
            empleados.add(empleado);
        }
        return empleados;
    }

    static public Empleado validarLogin(ContentResolver resolver, String telefono){
        Uri uri = Contrato.Empleado.CONTENT_URI;
        String[] projection = {Contrato.Empleado._ID,Contrato.Empleado.NOMBRES};
        String selection= Contrato.Empleado.TELEFONO + " = '"+telefono+"'";
        Cursor cursor = resolver.query(uri,projection,selection,null,null);
        Empleado empleado= new Empleado();

        if(cursor.moveToFirst()){
            empleado.setID(cursor.getInt(cursor.getColumnIndex(Contrato.Empleado._ID)));
            empleado.setNombres(cursor.getString(cursor.getColumnIndex(Contrato.Empleado.NOMBRES)));
            return empleado;
        }
        return null;
    }
}
