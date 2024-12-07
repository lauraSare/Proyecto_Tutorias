package com.example;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyecto_tutorias.R;

import java.sql.ResultSet;

import ConexionBD.conexion_bd_tutorias;

public class ActivityBajas extends AppCompatActivity {

    private EditText numControlInput;
    private Button eliminarBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bajas);

        numControlInput = findViewById(R.id.input_num_control);
        eliminarBtn = findViewById(R.id.btn_eliminar);

        eliminarBtn.setOnClickListener(v -> eliminarAlumno());
    }

    private void eliminarAlumno() {
        String numControl = numControlInput.getText().toString().trim();

        if (numControl.isEmpty()) {
            Toast.makeText(this, "Por favor, ingresa el número de control.", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            conexion_bd_tutorias conexion = conexion_bd_tutorias.getInstance();
            try {
                String queryExiste = "SELECT COUNT(*) AS count FROM alumnos WHERE numControl = ?";
                boolean alumnoExiste = false;

                // Verificar si el alumno existe
                ResultSet rs = conexion.ejecutarConsultaSQL(queryExiste, numControl);
                if (rs.next() && rs.getInt("count") > 0) {
                    alumnoExiste = true;
                }

                if (alumnoExiste) {
                    // Eliminar el alumno
                    String queryBaja = "DELETE FROM alumnos WHERE numControl = ?";
                    boolean bajaExitoso = conexion.ejecutarInstruccionDML(queryBaja, numControl);

                    runOnUiThread(() -> {
                        if (bajaExitoso) {
                            Toast.makeText(this, "Alumno eliminado con éxito.", Toast.LENGTH_LONG).show();
                            numControlInput.setText(""); // Limpiar campo
                        } else {
                            Toast.makeText(this, "Error al eliminar el alumno.", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    runOnUiThread(() -> Toast.makeText(this, "El número de control no existe.", Toast.LENGTH_SHORT).show());
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "Error en la conexión con la base de datos.", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
}
