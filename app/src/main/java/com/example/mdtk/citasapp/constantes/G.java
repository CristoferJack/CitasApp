package com.example.mdtk.citasapp.constantes;

/**
 * Created by Tiburcio on 04/10/2016.
 */

public class G {
    //public static final String RUTA_SERVIDOR = "http://192.168.1.6:8080/TDEM_REST/Actividad"; // DESARROLLO
    public static final String RUTA_SERVIDOR = "http://74.207.250.150/TDEM_REST/Actividad"; // UAT
    //public static final String RUTA_SERVIDOR = "http://104.237.150.20/TDEM_REST/Actividad"; // PRODUCCION

    public static final int SYNC_INTERVAL = 1;//1; <- versiones menores a 24

    public static final int SIN_VALOR_INT = 0;
    public static final String SIN_VALOR_STRING = "";
    public static final int INSERTAR = 1;
    public static final int GUARDAR = 2;

    public static final int OPERACION_INSERTAR = 1;
    public static final int OPERACION_MODIFICAR = 2;
    public static final int OPERACION_ELIMINAR = 3;

    public static final int ESTADO_REGISTRADA = 186;
    public static final int ESTADO_ANULADA = 188;

}
