package com.example.mdtk.citasapp.proveedor;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.example.mdtk.citasapp.pojo.Cita;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CitaProveedor {
    public static void insertRecord(ContentResolver resolver, Cita cita){
        Uri uri = Contrato.Cita.CONTENT_URI;
        ContentValues values = new ContentValues();
        values.put(Contrato.Cita.SERVICIO, cita.getServicio());
        values.put(Contrato.Cita.CLIENTE, cita.getCliente());
        values.put(Contrato.Cita.NOTA, cita.getNota());
        values.put(Contrato.Cita.FECHA_HORA, cita.getFechaHora());
        values.put(Contrato.Cita.EMPLEADO_ID, cita.getEmpleadoID());
        resolver.insert(uri, values);
    }

    public static void deleteRecord(ContentResolver resolver, int citaId){
        Uri uri = Uri.parse(Contrato.Cita.CONTENT_URI +"/"+ citaId);
        resolver.delete(uri, null,null);
    }

    public static void updateRecord(ContentResolver resolver, Cita cita){
        Uri uri = Uri.parse(Contrato.Cita.CONTENT_URI +"/"+ cita.getID());
        ContentValues values = new ContentValues();
        values.put(Contrato.Cita.SERVICIO, cita.getServicio());
        values.put(Contrato.Cita.CLIENTE, cita.getCliente());
        values.put(Contrato.Cita.NOTA, cita.getNota());
        values.put(Contrato.Cita.FECHA_HORA, cita.getFechaHora());
        values.put(Contrato.Cita.EMPLEADO_ID, cita.getEmpleadoID());
        resolver.update(uri, values,null,null);
    }

    static public Cita readRecord(ContentResolver resolver, int cicloId){
        Uri uri = Uri.parse(Contrato.Cita.CONTENT_URI+"/"+cicloId);

        String[] projection = {Contrato.Cita.SERVICIO, Contrato.Cita.CLIENTE, Contrato.Cita.NOTA, Contrato.Cita.FECHA_HORA, Contrato.Cita.EMPLEADO_ID};
        Cursor cursor = resolver.query(uri,projection,null,null,null);

        if(cursor.moveToFirst()){
            Cita cita = new Cita();
            cita.setID(cicloId);
            cita.setServicio(cursor.getString(cursor.getColumnIndex(Contrato.Cita.SERVICIO)));
            cita.setCliente(cursor.getString(cursor.getColumnIndex(Contrato.Cita.CLIENTE)));
            cita.setNota(cursor.getString(cursor.getColumnIndex(Contrato.Cita.NOTA)));
            cita.setFechaHora(cursor.getString(cursor.getColumnIndex(Contrato.Cita.FECHA_HORA)));
            cita.setEmpleadoID(cursor.getInt(cursor.getColumnIndex(Contrato.Cita.EMPLEADO_ID)));

            return cita;
        }
        return null;
    }

    static public Map<String,List<Cita>> disponibilidadByEmpleado(ContentResolver contentResolver, int empleadoId){
        Uri uri = Contrato.Cita.CONTENT_URI;
        String[] projection = {Contrato.Cita.SERVICIO, Contrato.Cita.CLIENTE, Contrato.Cita.NOTA, Contrato.Cita.FECHA_HORA, Contrato.Cita.EMPLEADO_ID};
        String selection = empleadoId==0?"":Contrato.Cita.EMPLEADO_ID+" = '"+empleadoId+"'";
        Cursor cursor = contentResolver.query(uri,projection,selection,null,null);
        List<Cita> citas= new ArrayList<>();

        while (cursor.moveToNext()) {
            Cita cita = new Cita();
            cita.setCliente(cursor.getString(cursor.getColumnIndex(Contrato.Cita.CLIENTE)));
            cita.setNota(cursor.getString(cursor.getColumnIndex(Contrato.Cita.NOTA)));
            cita.setFechaHora(cursor.getString(cursor.getColumnIndex(Contrato.Cita.FECHA_HORA)));
            cita.setEmpleadoID(cursor.getInt(cursor.getColumnIndex(Contrato.Cita.EMPLEADO_ID)));

            citas.add(cita);
        }

        Map<String,List<Cita>> disponibilidadMap = new HashMap<>();
        for(Cita c: citas){
            String fecha = c.getFechaHora().substring(0,10);
            if(disponibilidadMap.get(fecha)!=null){
                disponibilidadMap.get(fecha).add(c);
            }else{
                List<Cita> citaList = new ArrayList<>();
                citaList.add(c);
                disponibilidadMap.put(fecha,citaList);
            }
        }

        return disponibilidadMap;
    }
}
