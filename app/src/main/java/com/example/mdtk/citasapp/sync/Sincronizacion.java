package com.example.mdtk.citasapp.sync;

import android.content.ContentResolver;
import android.content.Context;
import android.util.Log;

import com.example.mdtk.citasapp.constantes.G;
import com.example.mdtk.citasapp.pojo.Cita;
import com.example.mdtk.citasapp.pojo.SincronizacionRegistro;
import com.example.mdtk.citasapp.pojo.Trabajador;
import com.example.mdtk.citasapp.proveedor.CitaProveedor;
import com.example.mdtk.citasapp.proveedor.SincronizacionRegistroProveedor;
import com.example.mdtk.citasapp.proveedor.TrabajadorProveedor;
import com.example.mdtk.citasapp.volley.CitaVolley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Cristofer B. on 06/10/2018.
 */
public class Sincronizacion {
    private static final String LOGTAG = "Cristofer - Sincronizacion";
    private static ContentResolver resolvedor;
    private static Context contexto;
    private static boolean esperandoRespuestaDeServidor = false;

    public Sincronizacion(Context contexto){
        this.resolvedor = contexto.getContentResolver();
        this.contexto = contexto;
        //recibirActualizacionesDelServidor(); //La primera vez se cargan los datos siempre
    }

    public synchronized static boolean isEsperandoRespuestaDeServidor() {
        return esperandoRespuestaDeServidor;
    }

    public synchronized static void setEsperandoRespuestaDeServidor(boolean esperandoRespuestaDeServidor) {
        Sincronizacion.esperandoRespuestaDeServidor = esperandoRespuestaDeServidor;
    }

    public synchronized boolean sincronizar(){
        Log.i("sincronizacion","SINCRONIZAR");

        if(isEsperandoRespuestaDeServidor()){
            return true;
        }

        recibirActualizacionesDelServidor();
        enviarActualizacionesAlServidor();

        return true;
    }



    private static void enviarActualizacionesAlServidor(){
        ArrayList<SincronizacionRegistro> registrosBitacora = SincronizacionRegistroProveedor.readAllRecord(resolvedor);
        for(SincronizacionRegistro sincronizacionRegistro : registrosBitacora){

            switch(sincronizacionRegistro.getOperacion()){
                case G.OPERACION_INSERTAR:
                    Cita cita = null;
                    try {
                        Log.e("dsdsf","id_Cita sicncronizacion "+sincronizacionRegistro.getId_cita());
                        cita = CitaProveedor.readRecord(resolvedor, sincronizacionRegistro.getId_cita());
                        CitaVolley.addCita(cita, true, sincronizacionRegistro.getID());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case G.OPERACION_MODIFICAR:
                    try {
                        cita = CitaProveedor.readRecord(resolvedor, sincronizacionRegistro.getId_cita());
                        CitaVolley.updateCita(cita, true, sincronizacionRegistro.getID());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case G.OPERACION_ELIMINAR:
                    CitaVolley.delCiclo(sincronizacionRegistro.getId_cita(), true, sincronizacionRegistro.getID());
                    break;
            }

            Log.i("sincronizacion", "acabo de enviar");
        }

        //eliminar todos los registro de bitacora
        //SincronizacionRegistroProveedor.deleteRecord(resolvedor,sincronizacionRegistro.getID());
    }

    private static void recibirActualizacionesDelServidor(){
        CitaVolley.getAllCicloByHistorial();
    }

    public static void realizarActualizacionesDelServidorUnaVezRecibidasCita(JSONArray jsonArray){
        Log.i("sincronizacion", "recibirActualizacionesDelServidor");

        try {
            ArrayList<String> identificadoresDeRegistrosActualizados = new ArrayList<String>();
            ArrayList<Cita> registrosNuevos = new ArrayList<>();
            ArrayList<Cita> registrosViejos = CitaProveedor.readAllRecord(resolvedor);//1,2,3,4
            ArrayList<String> identificadoresDeRegistrosViejos = new ArrayList<String>();//
            for(Cita i : registrosViejos) identificadoresDeRegistrosViejos.add(i.getID());

            JSONObject obj = null;
            for (int i = 0; i < jsonArray.length(); i++ ){
                obj = jsonArray.getJSONObject(i);
                registrosNuevos.add(new Cita(
                        obj.getString("id_actividad"),
                        obj.getString("servicio"),
                        obj.getString("cliente"),
                        obj.getString("nota"),
                        obj.getString("fechaHora"),
                        obj.getInt("id_trabajador"),
                        obj.getInt("id_trabajador_registro"),
                        obj.getInt("estado"))
                );
            }
            for(Cita cita: registrosNuevos) {//
                try {
                    if(identificadoresDeRegistrosViejos.contains(cita.getID())) {
                        CitaProveedor.updateRecord(resolvedor, cita);
                    } else {
                        CitaProveedor.insertRecord(resolvedor, cita);
                    }
                    Log.i("Actualizacion", "registro: "+ cita.getID());
                    identificadoresDeRegistrosActualizados.add(cita.getID());
                } catch (Exception e){
                    Log.i("sincronizacion",
                            "Probablemente el registro ya existía en la BD."+"" +
                                    " Esto se podría controlar mejor con una Bitácora.");
                }
            }

/*            for(Cita cita: registrosViejos){//1,2.3,4
                if(!identificadoresDeRegistrosActualizados.contains(cita.getID())){
                    try {
                        CitaProveedor.deleteRecord(resolvedor, cita.getID());
                    }catch(Exception e){
                        Log.i("sincronizacion", "Error al borrar el registro con id:" + cita.getID());
                    }
                }
            }*/

            //CitaVolley.getAllCiclo(); //Los baja y los guarda en SQLite
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
