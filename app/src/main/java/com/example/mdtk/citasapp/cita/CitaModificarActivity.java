package com.example.mdtk.citasapp.cita;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.mdtk.citasapp.R;
import com.example.mdtk.citasapp.constantes.G;
import com.example.mdtk.citasapp.dialog.DateDialog;
import com.example.mdtk.citasapp.dialog.TimeDialog;
import com.example.mdtk.citasapp.pojo.Cita;
import com.example.mdtk.citasapp.pojo.Trabajador;
import com.example.mdtk.citasapp.proveedor.CitaProveedor;
import com.example.mdtk.citasapp.proveedor.Contrato;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.example.mdtk.citasapp.proveedor.TrabajadorProveedor.getList;
import static com.example.mdtk.citasapp.proveedor.LoginProveedor.getDefault;

public class CitaModificarActivity extends AppCompatActivity {
    int citaId;
    String fecha;
    EditText editTextCitaServicio;
    EditText editTextCitaCliente;
    EditText editTextCitaNota;
    EditText editTextCitaFecha;
    EditText editTextCitaHora;
    SimpleDateFormat simpleDate =  new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat simpleDateTime =  new SimpleDateFormat("yyyy-MM-dd HH:mm");
    SimpleDateFormat simpleTime = new SimpleDateFormat("HH:mm");
    SimpleDateFormat simpleDateBase =  new SimpleDateFormat("yyyy-MM-dd");
    Spinner spnEmpleado;
    int id_trabajador = 0;
    int id_trabajador_registro = 0;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cita_detalle);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_detalle_activity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editTextCitaServicio =(EditText) findViewById(R.id.editTextCitaServicio);
        editTextCitaCliente =(EditText) findViewById(R.id.editTextCitaCliente);
        editTextCitaNota =(EditText) findViewById(R.id.editTextCitaNota);
        editTextCitaFecha =(EditText) findViewById(R.id.editTextCitaFecha);
        editTextCitaHora =(EditText) findViewById(R.id.editTextCitaHora);
        spnEmpleado = (Spinner)findViewById(R.id.spnEmpleadoD);
        List<Trabajador> trabajadorList = getList(getContentResolver());
        ArrayAdapter<Trabajador> adapter = new ArrayAdapter<Trabajador>(this, android.R.layout.simple_spinner_dropdown_item, trabajadorList);
        spnEmpleado.setAdapter(adapter);
        spnEmpleado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Trabajador trabajador = (Trabajador) parent.getSelectedItem();
                id_trabajador = trabajador.getID();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        citaId = this.getIntent().getExtras().getInt(Contrato.Cita._ID);
        Cita cita = CitaProveedor.readRecord(getContentResolver(), citaId);

        int posicionEmpleado = 0;
        for(Trabajador e : trabajadorList){
            if(e.getID() == cita.getId_trabajador()) {
                spnEmpleado.setSelection(posicionEmpleado);
                break;
            }
            posicionEmpleado++;
        }


        editTextCitaServicio.setText(cita.getServicio());
        editTextCitaCliente.setText(cita.getCliente());
        editTextCitaNota.setText(cita.getNota());
        try {
            String fec = cita.getFechaHora();
            Date ff = simpleDateTime.parse(fec);
            editTextCitaFecha.setText( simpleDate.format(ff));
            editTextCitaHora.setText(simpleTime.format(simpleDateTime.parse(cita.getFechaHora())));

            //fecha= simpleDateBase.format(simpleDateTime.parse(cita.getFechaHora()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        id_trabajador_registro = getDefault(getContentResolver());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.add(Menu.NONE, G.GUARDAR,Menu.NONE,"Guardar");
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menuItem.setIcon(R.drawable.ic_action_guardar);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case G.GUARDAR:
                try {
                    attemptGuardar();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    void attemptGuardar() throws ParseException {
        EditText editTextServicio = (EditText) findViewById(R.id.editTextCitaServicio);
        EditText editTextCliente = (EditText) findViewById(R.id.editTextCitaCliente);
        EditText editTextHora = (EditText) findViewById(R.id.editTextCitaHora);
        EditText editTextNota = (EditText) findViewById(R.id.editTextCitaNota);

        editTextServicio.setError(null);
        editTextCliente.setError(null);
        editTextHora.setError(null);

        String servicio = String.valueOf(editTextServicio.getText());
        String cliente = String.valueOf(editTextCliente.getText());
        String horaStr = String.valueOf(editTextHora.getText());
        String nota = String.valueOf(editTextNota.getText());

        String fec = String.valueOf(editTextCitaFecha.getText());
        Date ff = simpleDate.parse(fec);
        String fechaStr = simpleDateBase.format(ff);

        if(TextUtils.isEmpty(servicio)){
            editTextServicio.setError(getString(R.string.campo_requerido));
            editTextServicio.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(cliente)){
            editTextCliente.setError(getString(R.string.campo_requerido));
            editTextCliente.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(horaStr)){
            editTextHora.setError(getString(R.string.campo_requerido));
            editTextHora.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(fechaStr)){
            editTextCitaFecha.setError(getString(R.string.campo_requerido));
            editTextCitaFecha.requestFocus();
            return;
        }

        if(id_trabajador ==0){
            editTextHora.setError(getString(R.string.campo_requerido));
            editTextHora.requestFocus();
            return;
        }

        String fechaHora = fechaStr+ " " +horaStr;

        Cita cita = new Cita(citaId, servicio ,cliente,nota,fechaHora, id_trabajador,id_trabajador_registro,G.ESTADO_REGISTRADA);
        CitaProveedor.updateRecordSincronizacion(getContentResolver(), cita);

        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EditText txtTime=(EditText)findViewById(R.id.editTextCitaHora);
        txtTime.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            public void onFocusChange(View view, boolean hasfocus){
                if(hasfocus){
                    TimeDialog dialog = TimeDialog.newInstance(view);
                    dialog.show(getFragmentManager(), "DatePicker");
                }
            }
        });

        editTextCitaFecha.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            public void onFocusChange(View view, boolean hasfocus){
                if(hasfocus){
                    DateDialog dialog = DateDialog.newInstance(view);
                    dialog.show(getFragmentManager(), "DatePicker");
                }
            }
        });
    }
}
