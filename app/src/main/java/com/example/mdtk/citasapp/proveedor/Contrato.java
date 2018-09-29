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
        public static final String EMPLEADO_ID = "empleadoId";
    }

    public static final class Empleado implements BaseColumns {

        public static final Uri CONTENT_URI = Uri
                .parse("content://"+AUTHORITY+"/Empleado");

        // Table column
        public static final String NOMBRES = "nombres";
        public static final String TELEFONO = "telefono";
    }

    public static final class Login implements BaseColumns {

        public static final Uri CONTENT_URI = Uri
                .parse("content://"+AUTHORITY+"/Login");

        // Table column
        public static final String EMPLEADO_ID = "empleadoID";
        public static final String ESTADO = "estado";
    }
}
