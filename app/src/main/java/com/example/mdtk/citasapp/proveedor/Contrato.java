package com.example.mdtk.citasapp.proveedor;

import android.net.Uri;
import android.provider.BaseColumns;

import java.util.Date;

public class Contrato {

    public static final String AUTHORITY = "com.example.mdtk.citasapp.proveedor.ProveedorDeContenido";

    public static final class Cita implements BaseColumns {

        public static final Uri CONTENT_URI = Uri
                .parse("content://"+AUTHORITY+"/Cita");//recurso dentro del porveedor de contenido,osea la tabla"Cita"

        // Table column
        public static final String SERVICIO = "servicio";
        public static final String CLIENTE = "cliente";
        public static final String NOTA = "nota";
        public static final String FECHA_HORA = "fechaHora";
        public static final String ID_TRABAJADOR = "id_trabajador";
        public static final String ID_TRABAJADOR_REGISTRO = "id_trabajador_registro";
        public static final String ESTADO = "estado";
    }

    public static final class Trabajador implements BaseColumns {

        public static final Uri CONTENT_URI = Uri
                .parse("content://"+AUTHORITY+"/Trabajador");

        // Table column
        public static final String NOMBRES = "nombres";
        public static final String TELEFONO = "telefono";
    }

    public static final class Login implements BaseColumns {

        public static final Uri CONTENT_URI = Uri
                .parse("content://"+AUTHORITY+"/Login");

        // Table column
        public static final String ID_TRABAJADOR_REGISTRO = "id_trabajador_registro";
        public static final String ESTADO = "estado";
    }

    public static final class Sincronizacion implements BaseColumns {

        public static final Uri CONTENT_URI = Uri
                .parse("content://"+AUTHORITY+"/SincronizacionRegistro");

        // Table column
        public static final String ID_CITA = "id_cita";
        public static final String OPERACION = "operacion";
        public static final String ID_TRABAJADOR_REGISTRO = "id_trabajador_registro";
    }
}
