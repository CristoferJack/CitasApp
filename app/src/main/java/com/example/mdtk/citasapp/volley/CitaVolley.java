package com.example.mdtk.citasapp.volley;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mdtk.citasapp.constantes.G;
import com.example.mdtk.citasapp.aplicacion.AppController;
import com.example.mdtk.citasapp.constantes.G;
import com.example.mdtk.citasapp.pojo.Cita;
import com.example.mdtk.citasapp.proveedor.SincronizacionRegistroProveedor;
import com.example.mdtk.citasapp.proveedor.CitaProveedor;
import com.example.mdtk.citasapp.sync.Sincronizacion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.mdtk.citasapp.proveedor.LoginProveedor.getDefault;


public class CitaVolley {
    final static String ruta = G.RUTA_SERVIDOR ;

    public static void getAllCicloByHistorial(){
        int id_trabajador_registrado = getDefault(AppController.getInstance().getContentResolver());
        String predicado= "/ListarActividadByHistorial/"+ id_trabajador_registrado;
        if(id_trabajador_registrado==0){
            predicado ="/ListarActividad";
        }
        String tag_json_obj = "getAllCicloByHistorial"; //En realidad debería ser un identificar único para luego poder cancelar la petición.
        String url = ruta + predicado;
        // prepare the Request

        AppController.getInstance().getSincronizacion().setEsperandoRespuestaDeServidor(true);

        JsonArrayRequest getRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>(){
                    @Override
                    public void onResponse(JSONArray response) {
                        // display response
                        //Log.d("Response", response.toString());
                        Sincronizacion.realizarActualizacionesDelServidorUnaVezRecibidas(response);
                        AppController.getInstance().getSincronizacion().setEsperandoRespuestaDeServidor(false);
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Log.d("Error.Response", error.getMessage());
                        AppController.getInstance().getSincronizacion().setEsperandoRespuestaDeServidor(false);
                    }
                }
        );
        // add it to the RequestQueue
        //queue.add(getRequest);
        AppController.getInstance().addToRequestQueue(getRequest, tag_json_obj);
    }

    public static void addCita(Cita cita, final boolean conSincronizacionRegistro, final int idSincronizacionRegistro){
        String tag_json_obj = "addCita"; //En realidad debería ser un identificar único para luego poder cancelar la petición.
        String url = ruta +"/Crear";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id_actividad", cita.getID());
            jsonObject.put("servicio", cita.getServicio());
            jsonObject.put("cliente", cita.getCliente());
            jsonObject.put("nota", cita.getNota());
            jsonObject.put("fechaHora", cita.getFechaHora());
            jsonObject.put("estado", cita.getEstado());
            jsonObject.put("id_trabajador", cita.getId_trabajador());
            jsonObject.put("id_trabajador_registro", cita.getId_trabajador_registro());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AppController.getInstance().getSincronizacion().setEsperandoRespuestaDeServidor(true);

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // response
                        //Log.d("Response", "respponse server "+response.toString());
                        if(conSincronizacionRegistro) SincronizacionRegistroProveedor.deleteRecord(AppController.getResolvedor(), idSincronizacionRegistro);
                        AppController.getInstance().getSincronizacion().setEsperandoRespuestaDeServidor(false);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        //Log.d("Error.Response", error.getMessage());
                        AppController.getInstance().getSincronizacion().setEsperandoRespuestaDeServidor(false);
                    }
                }
        );
        //queue.add(postRequest);
        AppController.getInstance().addToRequestQueue(postRequest, tag_json_obj);
    }

    public static void updateCita(Cita cita, final boolean conBitacora, final int idBitacora){
        String tag_json_obj = "updateCita"; //En realidad debería ser un identificar único para luego poder cancelar la petición.
        String url = ruta + "/Editar";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id_actividad", cita.getID());
            jsonObject.put("servicio", cita.getServicio());
            jsonObject.put("cliente", cita.getCliente());
            jsonObject.put("nota", cita.getNota());
            jsonObject.put("fechaHora", cita.getFechaHora());
            jsonObject.put("estado", cita.getEstado());
            jsonObject.put("id_trabajador", cita.getId_trabajador());
            jsonObject.put("id_trabajador_registro", cita.getId_trabajador_registro());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AppController.getInstance().getSincronizacion().setEsperandoRespuestaDeServidor(true);

        JsonObjectRequest putRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        // response
                        Log.d("Response", response.toString());
                        if(conBitacora) SincronizacionRegistroProveedor.deleteRecord(AppController.getResolvedor(), idBitacora);
                        AppController.getInstance().getSincronizacion().setEsperandoRespuestaDeServidor(false);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        //Log.d("Error.Response", error.getMessage());
                        AppController.getInstance().getSincronizacion().setEsperandoRespuestaDeServidor(false);
                    }
                }
        );
        //queue.add(putRequest);
        AppController.getInstance().addToRequestQueue(putRequest, tag_json_obj);
    }

    public static void delCiclo(String id, final boolean conBitacora, final int idBitacora){
        String tag_json_obj = "updateCiclo"; //En realidad debería ser un identificar único para luego poder cancelar la petición.
        String url = ruta + "/Eliminar/" + String.valueOf(id);

        AppController.getInstance().getSincronizacion().setEsperandoRespuestaDeServidor(true);

        StringRequest delRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        if(conBitacora) SincronizacionRegistroProveedor.deleteRecord(AppController.getResolvedor(), idBitacora);
                        AppController.getInstance().getSincronizacion().setEsperandoRespuestaDeServidor(false);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error.
                        AppController.getInstance().getSincronizacion().setEsperandoRespuestaDeServidor(false);

                    }
                }
        );
        //queue.add(delRequest);
        AppController.getInstance().addToRequestQueue(delRequest, tag_json_obj);
    }
}
