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
import com.example.mdtk.citasapp.volley.TrabajadorVolley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Cristofer B. on 06/10/2018.
 */
public class SincronizacionTrabajador {
    private static final String LOGTAG = "Cristofer - Sincronizacion Trabajador";
    private static ContentResolver resolvedor;
    private static Context contexto;
    private static boolean esperandoRespuestaDeServidor = false;

    public SincronizacionTrabajador(Context contexto){
        this.resolvedor = contexto.getContentResolver();
        this.contexto = contexto;
        //recibirActualizacionesDelServidor(); //La primera vez se cargan los datos siempre
    }

    public synchronized static boolean isEsperandoRespuestaDeServidor() {
        return esperandoRespuestaDeServidor;
    }

    public synchronized static void setEsperandoRespuestaDeServidor(boolean esperandoRespuestaDeServidor) {
        SincronizacionTrabajador.esperandoRespuestaDeServidor = esperandoRespuestaDeServidor;
    }

    public synchronized boolean sincronizar(){
        Log.i("sincronizacion","SINCRONIZAR TRABAJADORES");

        if(isEsperandoRespuestaDeServidor()){
            return true;
        }

        recibirActualizacionesDelServidor();

        return true;
    }

    private static void recibirActualizacionesDelServidor(){
        TrabajadorVolley.getAllCicloByHistorial();
    }

    public static void realizarActualizacionesDelServidorUnaVezRecibidasTrabajador(JSONArray jsonArray){
        Log.i("sincTrabajador", "ActualizacionesTrabajadorDelServidor");

        try {
            //ArrayList<String> identificadoresDeRegistrosActualizados = new ArrayList<String>();
            ArrayList<Trabajador> registrosNuevos = new ArrayList<>();
            ArrayList<Trabajador> registrosViejos = TrabajadorProveedor.readAllRecord(resolvedor);//1,2,3,4
            ArrayList<Integer> identificadoresDeRegistrosViejos = new ArrayList<>();//
            for(Trabajador i : registrosViejos) identificadoresDeRegistrosViejos.add(i.getID());

            JSONObject obj = null;
            for (int i = 0; i < jsonArray.length(); i++ ){
                obj = jsonArray.getJSONObject(i);
                registrosNuevos.add(new Trabajador(
                        obj.getInt("id_trabajador"),
                        obj.getString("nombres"),
                        obj.getString("telefono")
                ));
            }
            for(Trabajador trabajador: registrosNuevos) {//
                try {
                    if(identificadoresDeRegistrosViejos.contains(trabajador.getID())) {
                        TrabajadorProveedor.updateRecord(resolvedor, trabajador);
                    } else {
                        TrabajadorProveedor.insertRecord(resolvedor, trabajador);
                    }
                    Log.i("Actualizacion", "registro: "+ trabajador.getID());
                    //identificadoresDeRegistrosActualizados.add(trabajador.getID());
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
