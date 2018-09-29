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
import com.example.mdtk.citasapp.pojo.Empleado;
import com.example.mdtk.citasapp.proveedor.CitaProveedor;
import com.example.mdtk.citasapp.proveedor.Contrato;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

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
    int empleadoId = 0;
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
        ArrayList<Empleado> empleadoList = new ArrayList<>();
        empleadoList.add(new Empleado(0, "Seleccione"));
        empleadoList.add(new Empleado(1, "Celeste"));
        empleadoList.add(new Empleado(2, "Richard"));
        empleadoList.add(new Empleado(3, "Victoria"));
        empleadoList.add(new Empleado(4, "Eloy"));
        ArrayAdapter<Empleado> adapter = new ArrayAdapter<Empleado>(this, android.R.layout.simple_spinner_dropdown_item, empleadoList);
        spnEmpleado.setAdapter(adapter);
        spnEmpleado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Empleado empleado = (Empleado) parent.getSelectedItem();
                empleadoId = empleado.getID();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        citaId = this.getIntent().getExtras().getInt(Contrato.Cita._ID);
        Cita cita = CitaProveedor.readRecord(getContentResolver(), citaId);

        int posicionEmpleado = 0;
        for(Empleado e : empleadoList){
            if(e.getID() == cita.getEmpleadoID()) {
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
            //editTextCitaFecha.setEnabled(false);
            editTextCitaHora.setText(simpleTime.format(simpleDateTime.parse(cita.getFechaHora())));

            fecha= simpleDateBase.format(simpleDateTime.parse(cita.getFechaHora()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
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
                attemptGuardar();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    void attemptGuardar(){
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
        if(empleadoId==0){
            editTextHora.setError(getString(R.string.campo_requerido));
            editTextHora.requestFocus();
            return;
        }

        String fechaHora = fecha+ " " +horaStr;

        Cita cita = new Cita(citaId, servicio ,cliente,nota,fechaHora,empleadoId);
        CitaProveedor.updateRecord(getContentResolver(), cita);

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
