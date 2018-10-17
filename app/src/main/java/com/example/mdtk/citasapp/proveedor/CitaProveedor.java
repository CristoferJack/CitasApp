package com.example.mdtk.citasapp.proveedor;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.example.mdtk.citasapp.constantes.G;
import com.example.mdtk.citasapp.pojo.Cita;
import com.example.mdtk.citasapp.pojo.SincronizacionRegistro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.example.mdtk.citasapp.proveedor.LoginProveedor.getDefault;

public class CitaProveedor {
    public static Uri insertRecord(ContentResolver resolver, Cita cita){
        Uri uri = Contrato.Cita.CONTENT_URI;
        ContentValues values = new ContentValues();
        if(!cita.getID().equals(G.SIN_VALOR_STRING)){
            values.put(Contrato.Cita._ID, cita.getID());
        }else{
            values.put(Contrato.Cita._ID, getUUID());
        }
        values.put(Contrato.Cita.SERVICIO, cita.getServicio());
        values.put(Contrato.Cita.CLIENTE, cita.getCliente());
        values.put(Contrato.Cita.NOTA, cita.getNota());
        values.put(Contrato.Cita.FECHA_HORA, cita.getFechaHora());
        values.put(Contrato.Cita.ID_TRABAJADOR, cita.getId_trabajador());
        values.put(Contrato.Cita.ID_TRABAJADOR_REGISTRO, cita.getId_trabajador_registro());
        values.put(Contrato.Cita.ESTADO, cita.getEstado());
        return resolver.insert(uri, values);
    }

    public static void insertRecordSincronizacion(ContentResolver resolver, Cita cita){
        Uri uri = insertRecord(resolver,cita);
        cita.setID(uri.getLastPathSegment());

        SincronizacionRegistro sincronizacionRegistro = new SincronizacionRegistro();
        sincronizacionRegistro.setId_cita(cita.getID());
        sincronizacionRegistro.setOperacion(G.OPERACION_INSERTAR);
        sincronizacionRegistro.setId_trabajador_registro(cita.getId_trabajador_registro());

        SincronizacionRegistroProveedor.insertRecord(resolver, sincronizacionRegistro);
    }

    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

    public static void deleteRecord(ContentResolver resolver, String citaId,int id_trabajador_registro){
        //Uri uri = Uri.parse(Contrato.Cita.CONTENT_URI +"/"+ citaId);
        //resolver.delete(uri, null,null);
        Cita cita = readRecord(resolver,citaId);
        cita.setEstado(G.ESTADO_ANULADA);
        cita.setId_trabajador_registro(id_trabajador_registro);
        updateRecord(resolver,cita);
    }

    public static void deleteRecordSincronizacion(ContentResolver resolver, String citaId,int id_trabajador_registro){
        deleteRecord(resolver,citaId,id_trabajador_registro);

        SincronizacionRegistro sincronizacionRegistro = new SincronizacionRegistro();
        sincronizacionRegistro.setId_cita(citaId);
        sincronizacionRegistro.setOperacion(G.OPERACION_ELIMINAR);
        sincronizacionRegistro.setId_trabajador_registro(id_trabajador_registro);
        SincronizacionRegistroProveedor.insertRecord(resolver, sincronizacionRegistro);
    }

    public static void updateRecord(ContentResolver resolver, Cita cita){
        Uri uri = Uri.parse(Contrato.Cita.CONTENT_URI +"");
        ContentValues values = new ContentValues();
        values.put(Contrato.Cita.SERVICIO, cita.getServicio());
        values.put(Contrato.Cita.CLIENTE, cita.getCliente());
        values.put(Contrato.Cita.NOTA, cita.getNota());
        values.put(Contrato.Cita.FECHA_HORA, cita.getFechaHora());
        values.put(Contrato.Cita.ID_TRABAJADOR, cita.getId_trabajador());
        values.put(Contrato.Cita.ID_TRABAJADOR_REGISTRO, cita.getId_trabajador_registro());
        values.put(Contrato.Cita.ESTADO, cita.getEstado());
        String where = "_id = '" +cita.getID()+"'";
        resolver.update(uri, values,where,null);
    }

    public static void updateRecordSincronizacion(ContentResolver resolver, Cita cita){
        updateRecord(resolver,cita);

        SincronizacionRegistro sincronizacionRegistro = new SincronizacionRegistro();
        sincronizacionRegistro.setId_cita(cita.getID());
        sincronizacionRegistro.setOperacion(G.OPERACION_MODIFICAR);
        sincronizacionRegistro.setId_trabajador_registro(cita.getId_trabajador_registro());
        SincronizacionRegistroProveedor.insertRecord(resolver, sincronizacionRegistro);
    }

    static public Cita readRecord(ContentResolver resolver, String cicloId){
        Uri uri = Uri.parse(Contrato.Cita.CONTENT_URI+"");

        String[] projection = {Contrato.Cita._ID , Contrato.Cita.SERVICIO, Contrato.Cita.CLIENTE, Contrato.Cita.NOTA,
                Contrato.Cita.FECHA_HORA, Contrato.Cita.ID_TRABAJADOR, Contrato.Cita.ID_TRABAJADOR_REGISTRO, Contrato.Cita.ESTADO};

        String selection = "_id = '"+ cicloId +"'";

        Cursor cursor = resolver.query(uri,projection,selection,null,null);

        if(cursor.moveToFirst()){
            Cita cita = new Cita();
            cita.setID(cicloId);
            cita.setServicio(cursor.getString(cursor.getColumnIndex(Contrato.Cita.SERVICIO)));
            cita.setCliente(cursor.getString(cursor.getColumnIndex(Contrato.Cita.CLIENTE)));
            cita.setNota(cursor.getString(cursor.getColumnIndex(Contrato.Cita.NOTA)));
            cita.setFechaHora(cursor.getString(cursor.getColumnIndex(Contrato.Cita.FECHA_HORA)));
            cita.setId_trabajador(cursor.getInt(cursor.getColumnIndex(Contrato.Cita.ID_TRABAJADOR)));
            cita.setId_trabajador_registro(cursor.getInt(cursor.getColumnIndex(Contrato.Cita.ID_TRABAJADOR_REGISTRO)));

            return cita;
        }
        return null;
    }

    static public ArrayList<Cita> readAllRecord(ContentResolver resolver){
        Uri uri = Uri.parse(Contrato.Cita.CONTENT_URI+"");

        String[] projection = {
                Contrato.Cita._ID,
                Contrato.Cita.FECHA_HORA,
                Contrato.Cita.SERVICIO,
                Contrato.Cita.ID_TRABAJADOR,
                Contrato.Cita.CLIENTE,
                Contrato.Cita.NOTA,
                Contrato.Cita.ESTADO,
                Contrato.Cita.ID_TRABAJADOR_REGISTRO,
        };
        Cursor cursor = resolver.query(uri,projection,null,null,null);

        ArrayList<Cita> citas = new ArrayList<>();
        Cita cita;
        while(cursor.moveToNext()){
            cita = new Cita();
            cita.setID(cursor.getString(cursor.getColumnIndex(Contrato.Cita._ID)));
            cita.setFechaHora(cursor.getString(cursor.getColumnIndex(Contrato.Cita.FECHA_HORA)));
            cita.setServicio(cursor.getString(cursor.getColumnIndex(Contrato.Cita.SERVICIO)));
            cita.setId_trabajador(cursor.getInt(cursor.getColumnIndex(Contrato.Cita.ID_TRABAJADOR)));
            cita.setCliente(cursor.getString(cursor.getColumnIndex(Contrato.Cita.CLIENTE)));
            cita.setNota(cursor.getString(cursor.getColumnIndex(Contrato.Cita.NOTA)));
            cita.setEstado(cursor.getInt(cursor.getColumnIndex(Contrato.Cita.ESTADO)));
            cita.setId_trabajador_registro(cursor.getInt(cursor.getColumnIndex(Contrato.Cita.ID_TRABAJADOR_REGISTRO)));

            citas.add(cita);
        }
        return citas;
    }

    static public Map<String,List<Cita>> disponibilidadByEmpleado(ContentResolver contentResolver, int empleadoId){
        Uri uri = Contrato.Cita.CONTENT_URI;
        String[] projection = {Contrato.Cita.SERVICIO, Contrato.Cita.CLIENTE, Contrato.Cita.NOTA,
                Contrato.Cita.FECHA_HORA, Contrato.Cita.ID_TRABAJADOR, Contrato.Cita.ESTADO};
        String selection = empleadoId==0?"":Contrato.Cita.ID_TRABAJADOR +" = '"+empleadoId+"'";
        Cursor cursor = contentResolver.query(uri,projection,selection,null,null);
        List<Cita> citas= new ArrayList<>();

        while (cursor.moveToNext()) {
            if(cursor.getInt(cursor.getColumnIndex(Contrato.Cita.ESTADO)) == G.ESTADO_REGISTRADA){
                Cita cita = new Cita();
                cita.setCliente(cursor.getString(cursor.getColumnIndex(Contrato.Cita.CLIENTE)));
                cita.setNota(cursor.getString(cursor.getColumnIndex(Contrato.Cita.NOTA)));
                cita.setFechaHora(cursor.getString(cursor.getColumnIndex(Contrato.Cita.FECHA_HORA)));
                cita.setId_trabajador(cursor.getInt(cursor.getColumnIndex(Contrato.Cita.ID_TRABAJADOR)));

                citas.add(cita);
            }
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

    static public int cantidadCitasPendientesDia(ContentResolver contentResolver,int id_trabajador_registrado){
        Uri uri = Contrato.Cita.CONTENT_URI;
        String[] projection = {Contrato.Cita.SERVICIO, Contrato.Cita.CLIENTE, Contrato.Cita.NOTA,
                Contrato.Cita.FECHA_HORA, Contrato.Cita.ID_TRABAJADOR, Contrato.Cita.ESTADO};

        String empleadoSelection = " AND "+Contrato.Cita.ID_TRABAJADOR +" = '"+ id_trabajador_registrado +"'";
        String selection = Contrato.Cita.FECHA_HORA+" > strftime('%Y-%m-%d %H:%M', datetime('now','localtime')) AND "+ Contrato.Cita.FECHA_HORA+" < strftime('%Y-%m-%d 23:59', datetime('now','localtime')) AND " +
                "estado ='" + G.ESTADO_REGISTRADA +"' "+ empleadoSelection;

        Cursor cursor = contentResolver.query(uri,projection,selection,null,null);

        return cursor.getCount();
    }
}
