package com.example.mdtk.citasapp.volley;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.mdtk.citasapp.aplicacion.AppController;
import com.example.mdtk.citasapp.constantes.G;
import com.example.mdtk.citasapp.sync.Sincronizacion;
import com.example.mdtk.citasapp.sync.SincronizacionTrabajador;

import org.json.JSONArray;

public class TrabajadorVolley {
    final static String ruta = G.RUTA_SERVIDOR ;

    public static void getAllCicloByHistorial(){
        String tag_json_obj = "getDatosUsuarios";
        String url = ruta + "/ActualizarDatosUsuarios";
        // prepare the Request

        AppController.getInstance().getSincronizacionTrabajador().setEsperandoRespuestaDeServidor(true);

        JsonArrayRequest getRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>(){
                    @Override
                    public void onResponse(JSONArray response) {
                        // display response
                        //Log.d("Response", response.toString());
                        SincronizacionTrabajador.realizarActualizacionesDelServidorUnaVezRecibidasTrabajador(response);
                        AppController.getInstance().getSincronizacionTrabajador().setEsperandoRespuestaDeServidor(false);
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Log.d("Error.Response", error.getMessage());
                        AppController.getInstance().getSincronizacionTrabajador().setEsperandoRespuestaDeServidor(false);
                    }
                }
        );
        // add it to the RequestQueue
        //queue.add(getRequest);
        AppController.getInstance().addToRequestQueue(getRequest, tag_json_obj);
    }
}
