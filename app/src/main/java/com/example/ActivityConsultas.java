package com.example;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyecto_tutorias.R;

import java.sql.ResultSet;

import ConexionBD.conexion_bd_tutorias;

public class ActivityConsultas extends AppCompatActivity {

    private EditText numControlInput;
    private Button consultarBtn;
    private LinearLayout datosAlumnoContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultas);

        numControlInput = findViewById(R.id.input_num_control);
        consultarBtn = findViewById(R.id.btn_consultar);
        datosAlumnoContainer = findViewById(R.id.datos_alumno_container);

        consultarBtn.setOnClickListener(v -> consultarAlumno());
    }

    private void consultarAlumno() {
        String numControl = numControlInput.getText().toString().trim();

        if (numControl.isEmpty() || !numControl.matches("\\d+")) {
            Toast.makeText(this, "Por favor, ingresa un número de control válido.", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            conexion_bd_tutorias conexion = conexion_bd_tutorias.getInstance();
            try {
                String query = "SELECT * FROM alumnos WHERE numControl = ?";
                ResultSet rs = conexion.ejecutarConsultaSQL(query, numControl);

                if (rs.next()) {
                    runOnUiThread(() -> mostrarDatosAlumno(rs));
                } else {
                    runOnUiThread(() -> {
                        datosAlumnoContainer.removeAllViews();
                        Toast.makeText(this, "No se encontró el registro del alumno.", Toast.LENGTH_SHORT).show();
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "Error en la conexión con la base de datos.", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void mostrarDatosAlumno(ResultSet rs) {
        try {
            datosAlumnoContainer.removeAllViews();

            agregarTexto("Número de Control", rs.getString("numControl"));
            agregarTexto("Nombre", rs.getString("nombre"));
            agregarTexto("Apellido Paterno", rs.getString("apellidoP"));
            agregarTexto("Apellido Materno", rs.getString("apellidoM"));
            agregarTexto("Fecha de Nacimiento", rs.getString("fecha_nacimiento"));
            agregarTexto("Teléfono", rs.getString("telefono"));
            agregarTexto("Email", rs.getString("email"));
            agregarTexto("Carrera", rs.getString("Carrera_carrera_id"));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al procesar los datos del alumno.", Toast.LENGTH_SHORT).show();
        }
    }

    private void agregarTexto(String label, String value) {
        TextView textView = new TextView(this);
        textView.setText(String.format("%s: %s", label, value));
        textView.setPadding(8, 8, 8, 8);
        datosAlumnoContainer.addView(textView);
    }
}
