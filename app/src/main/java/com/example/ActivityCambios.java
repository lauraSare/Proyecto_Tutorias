package com.example;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyecto_tutorias.R;

import ConexionBD.conexion_bd_tutorias;

public class ActivityCambios extends AppCompatActivity {

    private EditText numControlInput, nombreInput, apellidoPInput, apellidoMInput, fechaNacimientoInput, telefonoInput, emailInput;
    private Spinner carreraSpinner;
    private Button modificarBtn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambios);

        numControlInput = findViewById(R.id.input_num_control);
        nombreInput = findViewById(R.id.input_nombre);
        apellidoPInput = findViewById(R.id.input_apellido_p);
        apellidoMInput = findViewById(R.id.input_apellido_m);
        fechaNacimientoInput = findViewById(R.id.input_fecha_nacimiento);
        telefonoInput = findViewById(R.id.input_telefono);
        emailInput = findViewById(R.id.input_email);
        carreraSpinner = findViewById(R.id.spinner_carrera);
        modificarBtn = findViewById(R.id.btn_modificar);

        modificarBtn.setOnClickListener(v -> modificarAlumno());
    }

    private void modificarAlumno() {
        String numControl = numControlInput.getText().toString().trim();
        String nombre = nombreInput.getText().toString().trim();
        String apellidoP = apellidoPInput.getText().toString().trim();
        String apellidoM = apellidoMInput.getText().toString().trim();
        String fechaNacimiento = fechaNacimientoInput.getText().toString().trim();
        String telefono = telefonoInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String carrera = carreraSpinner.getSelectedItem().toString();

        if (numControl.isEmpty() || nombre.isEmpty() || apellidoP.isEmpty() || fechaNacimiento.isEmpty() || email.isEmpty() || carrera.isEmpty()) {
            Toast.makeText(this, "Por favor, llena todos los campos obligatorios.", Toast.LENGTH_LONG).show();
            return;
        }

        new Thread(() -> {
            conexion_bd_tutorias conexion = conexion_bd_tutorias.getInstance();
            try {
                String updateQuery = "UPDATE alumnos SET nombre = ?, apellidoP = ?, apellidoM = ?, fecha_nacimiento = ?, telefono = ?, email = ?, Carrera_carrera_id = ? WHERE numControl = ?";
                boolean isUpdated = conexion.ejecutarInstruccionDML(updateQuery, nombre, apellidoP, apellidoM, fechaNacimiento, telefono, email, carrera, numControl);

                runOnUiThread(() -> {
                    if (isUpdated) {
                        Toast.makeText(this, "¡Datos modificados correctamente!", Toast.LENGTH_LONG).show();
                        limpiarFormulario();
                    } else {
                        Toast.makeText(this, "Error al modificar los datos.", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                runOnUiThread(() -> Toast.makeText(this, "Error en la conexión con la base de datos.", Toast.LENGTH_SHORT).show());
                e.printStackTrace();
            }
        }).start();
    }

    private void limpiarFormulario() {
        numControlInput.setText("");
        nombreInput.setText("");
        apellidoPInput.setText("");
        apellidoMInput.setText("");
        fechaNacimientoInput.setText("");
        telefonoInput.setText("");
        emailInput.setText("");
        carreraSpinner.setSelection(0);
    }
}
