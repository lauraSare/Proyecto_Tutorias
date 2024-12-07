package com.example;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyecto_tutorias.R;

import ConexionBD.conexion_bd_tutorias;

public class ActivityAltas extends AppCompatActivity {

    private EditText numControl, nombre, primerApellido, segundoApellido, fechaNacimiento, telefono, email, carrera;
    private Button btnSubmit;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_altas);

        numControl = findViewById(R.id.input_num_control);
        nombre = findViewById(R.id.input_nombre);
        primerApellido = findViewById(R.id.input_primer_apellido);
        segundoApellido = findViewById(R.id.input_segundo_apellido);
        fechaNacimiento = findViewById(R.id.input_fecha_nacimiento);
        telefono = findViewById(R.id.input_telefono);
        email = findViewById(R.id.input_email);
        carrera = findViewById(R.id.input_carrera);
        btnSubmit = findViewById(R.id.btn_agregar);

        btnSubmit.setOnClickListener(v -> agregarAlumno());
    }

    private void agregarAlumno() {
        String nc = numControl.getText().toString();
        String nombreStr = nombre.getText().toString();
        String primerAp = primerApellido.getText().toString();
        String segundoAp = segundoApellido.getText().toString();
        String fechaNac = fechaNacimiento.getText().toString();
        String tel = telefono.getText().toString();
        String correo = email.getText().toString();
        String carreraStr = carrera.getText().toString();

        if (TextUtils.isEmpty(nc) || TextUtils.isEmpty(nombreStr) || TextUtils.isEmpty(primerAp) || TextUtils.isEmpty(fechaNac) || TextUtils.isEmpty(carreraStr)) {
            Toast.makeText(this, "Por favor completa todos los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            conexion_bd_tutorias conexion = conexion_bd_tutorias.getInstance();
            String query = "INSERT INTO alumnos (numControl, nombre, apellidoP, apellidoM, fecha_nacimiento, telefono, email, Carrera_carrera_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            boolean exito = conexion.ejecutarInstruccionDML(query, nc, nombreStr, primerAp, segundoAp, fechaNac, tel, correo, carreraStr);

            runOnUiThread(() -> {
                if (exito) {
                    Toast.makeText(this, "Alumno agregado correctamente", Toast.LENGTH_LONG).show();
                    limpiarFormulario();
                } else {
                    Toast.makeText(this, "Error al agregar al alumno", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

    private void limpiarFormulario() {
        numControl.setText("");
        nombre.setText("");
        primerApellido.setText("");
        segundoApellido.setText("");
        fechaNacimiento.setText("");
        telefono.setText("");
        email.setText("");
        carrera.setText("");
    }
}
