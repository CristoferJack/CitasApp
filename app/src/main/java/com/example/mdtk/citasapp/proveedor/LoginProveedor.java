package com.example.mdtk.citasapp.proveedor;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.example.mdtk.citasapp.pojo.Login;

public class LoginProveedor {
    static public int getDefault(ContentResolver resolver){
        Uri uri = Uri.parse(Contrato.Login.CONTENT_URI+"/"+1);
        String[] projection = {Contrato.Login.ID_TRABAJADOR_REGISTRO, Contrato.Login.ESTADO};
        Cursor cursor = resolver.query(uri,projection,null,null,null);

        if(cursor.moveToFirst()){
            Login login = new Login();
            login.setID(1);
            login.setId_trabajador_registro(cursor.getInt(cursor.getColumnIndex(Contrato.Login.ID_TRABAJADOR_REGISTRO)));
            login.setEstado(cursor.getInt(cursor.getColumnIndex(Contrato.Login.ESTADO)));

            if(cursor.getInt(cursor.getColumnIndex(Contrato.Login.ESTADO))==1){
                return cursor.getInt(cursor.getColumnIndex(Contrato.Login.ID_TRABAJADOR_REGISTRO));
            }else{
                return 0;
            }
        }
        return 0;
    }

    public static void updateRecord(ContentResolver resolver, Login login){
        Uri uri = Uri.parse(Contrato.Login.CONTENT_URI +"/"+ 1);
        ContentValues values = new ContentValues();
        values.put(Contrato.Login.ID_TRABAJADOR_REGISTRO, login.getId_trabajador_registro());
        values.put(Contrato.Login.ESTADO, login.getEstado());
        resolver.update(uri, values,null,null);
    }
}
