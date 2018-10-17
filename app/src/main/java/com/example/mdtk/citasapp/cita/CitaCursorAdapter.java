package com.example.mdtk.citasapp.cita;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.example.mdtk.citasapp.R;
import com.example.mdtk.citasapp.proveedor.Contrato;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CitaCursorAdapter extends CursorAdapter {
    private SimpleDateFormat simpleDateTime =  new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private SimpleDateFormat simpleTime = new SimpleDateFormat("HH:mm");

    public CitaCursorAdapter(Context context) {
        super(context, null, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String ID = cursor.getString(cursor.getColumnIndex(Contrato.Cita._ID));
        String servicio = cursor.getString(cursor.getColumnIndex(Contrato.Cita.SERVICIO));
        String cliente = cursor.getString(cursor.getColumnIndex(Contrato.Cita.CLIENTE));
        String empleado = cursor.getString(cursor.getColumnIndex(Contrato.Cita.ID_TRABAJADOR));

        TextView textviewServicio = (TextView) view.findViewById(R.id.textview_ciclo_list_item_servicio);
        textviewServicio.setText(servicio);

        TextView textviewCliente = (TextView) view.findViewById(R.id.textview_ciclo_list_item_cliente);
        textviewCliente.setText(cliente);

        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        int color = generator.getColor(empleado); //Genera un color según el nombre
        String hora ="";
        try {
            String d = cursor.getString(cursor.getColumnIndex(Contrato.Cita.FECHA_HORA));
            Date dat = simpleDateTime.parse(d);
            hora = simpleTime.format(dat);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                .fontSize(60) // size of text in pixels
                .endConfig()
                .buildRoundRect(hora.substring(0,5), color,20);

        ImageView image = (ImageView) view.findViewById(R.id.image_view);
        image.setImageDrawable(drawable);

        view.setTag(ID);

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.cita_list_item, parent, false);
        bindView(v, context, cursor);
        return v;
    }
}
