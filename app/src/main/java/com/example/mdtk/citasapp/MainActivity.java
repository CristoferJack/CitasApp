package com.example.mdtk.citasapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mdtk.citasapp.cita.CitaActivity;
import com.example.mdtk.citasapp.pojo.Empleado;
import com.example.mdtk.citasapp.pojo.Login;

import static com.example.mdtk.citasapp.proveedor.EmpleadoProveedor.validarLogin;
import static com.example.mdtk.citasapp.proveedor.LoginProveedor.getDefault;
import static com.example.mdtk.citasapp.proveedor.LoginProveedor.updateRecord;

public class MainActivity extends AppCompatActivity {
    Button _loginButton;
    TextView txtLoginTelefono;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int logueado= getDefault(getContentResolver());
        _loginButton = (Button) findViewById(R.id.buttonIrALasCitas);
        txtLoginTelefono = (TextView) findViewById(R.id.txtLoginTelefono);

        if(logueado!=0){
            Intent intent = new Intent(MainActivity.this, CitaActivity.class);
            startActivity(intent);
        }


        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    public void login() {
        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this,
                R.style.MyAlertDialogStyle);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Autenticando...");
        progressDialog.show();

        String telefono = txtLoginTelefono.getText().toString();
        //String password = _passwordText.getText().toString();

        Empleado empleado = validarLogin(getContentResolver(), telefono);
        if(empleado!=null){
            Login login =  new Login(1,empleado.getID(),1);
            updateRecord(getContentResolver(),login );

            Intent intent = new Intent(MainActivity.this, CitaActivity.class);
            startActivity(intent);
        }else{
            Toast.makeText(getBaseContext(), "Telefono inválido", Toast.LENGTH_LONG).show();
            onLoginFailed();
            progressDialog.dismiss();
            return;
        }

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Número de telefono incorrecto", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = txtLoginTelefono.getText().toString();

        if (email.isEmpty() ) {//|| !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
            txtLoginTelefono.setError("Ingrese un telefono válido");
            valid = false;
        } else {
            txtLoginTelefono.setError(null);
        }


        return valid;
    }
}
