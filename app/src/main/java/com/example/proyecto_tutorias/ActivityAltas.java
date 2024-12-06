package com.example.proyecto_tutorias;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import Controllers.AlumnoDAO;

public class ActivityAltas extends AppCompatActivity {

    private EditText numControl, nombre, primerApellido, segundoApellido, fechaNacimiento, telefono, email;
    private Spinner carrera;
    private Button btnRegistrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_altas);

        // Inicializar componentes
        numControl = findViewById(R.id.inputNumControl);
        nombre = findViewById(R.id.inputNombre);
        primerApellido = findViewById(R.id.inputPrimerApellido);
        segundoApellido = findViewById(R.id.inputSegundoApellido);
        fechaNacimiento = findViewById(R.id.inputFechaNacimiento);
        telefono = findViewById(R.id.inputTelefono);
        email = findViewById(R.id.inputEmail);
        carrera = findViewById(R.id.inputCarrera);

        btnRegistrar = findViewById(R.id.btnRegistrar);

        // Asignar evento al botón
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarAlumno();
            }
        });
    }

    private void registrarAlumno() {
        // Validar entradas
        String numControlValue = numControl.getText().toString().trim();
        String nombreValue = nombre.getText().toString().trim();
        String primerApellidoValue = primerApellido.getText().toString().trim();
        String segundoApellidoValue = segundoApellido.getText().toString().trim();
        String fechaNacimientoValue = fechaNacimiento.getText().toString().trim();
        String telefonoValue = telefono.getText().toString().trim();
        String emailValue = email.getText().toString().trim();
        String carreraValue = carrera.getSelectedItem().toString();

        if (!numControlValue.matches("^[a-zA-Z]\\d{8}$")) {
            numControl.setError("Número de control debe ser 1 letra seguida de 8 números");
            return;
        }
        if (!nombreValue.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$")) {
            nombre.setError("El nombre solo puede contener letras");
            return;
        }
        if (!primerApellidoValue.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$")) {
            primerApellido.setError("El primer apellido solo puede contener letras");
            return;
        }
        if (!segundoApellidoValue.isEmpty() && !segundoApellidoValue.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$")) {
            segundoApellido.setError("El segundo apellido solo puede contener letras");
            return;
        }
        if (!telefonoValue.matches("^\\d{10}$")) {
            telefono.setError("El teléfono debe contener 10 dígitos");
            return;
        }
        if (!emailValue.matches("^[a-zA-Z]+@[a-zA-Z]+\\.[a-zA-Z]{2,}$")) {
            email.setError("Correo no tiene formato válido");
            return;
        }

        if (carreraValue.equals("Selecciona tu carrera")) {
            Toast.makeText(this, "Por favor selecciona una carrera", Toast.LENGTH_SHORT).show();
            return;
        }

        // Enviar datos al servidor o BD local
        AlumnoDAO alumnoDAO = new AlumnoDAO(this);
        boolean result = alumnoDAO.agregarAlumno(
                numControlValue, nombreValue, primerApellidoValue, segundoApellidoValue,
                fechaNacimientoValue, telefonoValue, emailValue, carreraValue
        );

        if (result) {
            Toast.makeText(this, "Alumno registrado con éxito", Toast.LENGTH_SHORT).show();
            limpiarCampos();
        } else {
            Toast.makeText(this, "Error al registrar alumno", Toast.LENGTH_SHORT).show();
        }
    }

    private void limpiarCampos() {
        numControl.setText("");
        nombre.setText("");
        primerApellido.setText("");
        segundoApellido.setText("");
        fechaNacimiento.setText("");
        telefono.setText("");
        email.setText("");
        carrera.setSelection(0); // Reiniciar Spinner al primer valor
    }
}
