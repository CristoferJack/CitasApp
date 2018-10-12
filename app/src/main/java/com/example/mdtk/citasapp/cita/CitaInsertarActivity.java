package com.example.mdtk.citasapp.cita;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.mdtk.citasapp.R;
import com.example.mdtk.citasapp.constantes.G;
import com.example.mdtk.citasapp.dialog.DateDialog;
import com.example.mdtk.citasapp.dialog.TimeDialog;
import com.example.mdtk.citasapp.pojo.Cita;
import com.example.mdtk.citasapp.pojo.Trabajador;
import com.example.mdtk.citasapp.proveedor.CitaProveedor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import static com.example.mdtk.citasapp.proveedor.TrabajadorProveedor.getList;
import static com.example.mdtk.citasapp.proveedor.LoginProveedor.getDefault;

public class CitaInsertarActivity extends AppCompatActivity{

    String fecha;
    EditText editTextCitaFecha;
    SimpleDateFormat simpleDate =  new SimpleDateFormat("dd/MM/yyyy");
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

        Bundle bundle = getIntent().getExtras();
        Calendar calFecha = Calendar.getInstance();
        calFecha.clear();
        calFecha.set(Calendar.YEAR, bundle.getInt("Year"));
        calFecha.set(Calendar.MONTH, bundle.getInt("Month"));
        calFecha.set(Calendar.DATE, bundle.getInt("Day"));
        String fechaStr = simpleDate.format(calFecha.getTime());
        fecha = simpleDateBase.format(calFecha.getTime());

        editTextCitaFecha =(EditText) findViewById(R.id.editTextCitaFecha);
        editTextCitaFecha.setText(fechaStr);
        //editTextCitaFecha.setEnabled(false);

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

        int id_trabajador_seleccionado = bundle.getInt("id_trabajador_seleccionado");
        id_trabajador_registro = id_trabajador_seleccionado==0? getDefault(getContentResolver()):id_trabajador_seleccionado;

        int posicionEmpleado = 0;
        for(Trabajador e : trabajadorList){
            if(e.getID() == id_trabajador_registro) {
                spnEmpleado.setSelection(posicionEmpleado);
                break;
            }
            posicionEmpleado++;
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
        if(id_trabajador ==0){
            ((TextView)spnEmpleado.getChildAt(0)).setError(getString(R.string.campo_requerido));
            return;
        }

        String fechaHora = fecha+ " " +horaStr;

        Cita cita = new Cita(G.SIN_VALOR_INT, servicio,cliente,nota, fechaHora, id_trabajador,id_trabajador_registro,G.ESTADO_REGISTRADA);

        CitaProveedor.insertRecordSincronizacion(getContentResolver(), cita);

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
