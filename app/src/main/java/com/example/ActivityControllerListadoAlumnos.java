package com.example;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.proyecto_tutorias.R;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import ConexionBD.conexion_bd_tutorias;

public class ActivityControllerListadoAlumnos extends AppCompatActivity {

    private LinearLayout alumnosContainer;
    private Button loadButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_alumnos_carreras);

        // Referencias al layout
        alumnosContainer = findViewById(R.id.alumnos_container);
        loadButton = findViewById(R.id.btn_load);

        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarAlumnosCarreras();
            }
        });
    }

    private void cargarAlumnosCarreras() {
        new Thread(() -> {
            conexion_bd_tutorias conexion = conexion_bd_tutorias.getInstance();
            ArrayList<HashMap<String, String>> alumnosCarreras = new ArrayList<>();

            try {
                String query = "SELECT * FROM listado_alumnos_carreras";
                ResultSet rs = conexion.ejecutarConsultaSQL(query);

                while (rs.next()) {
                    HashMap<String, String> alumno = new HashMap<>();
                    alumno.put("numControl", rs.getString("numControl"));
                    alumno.put("nombre", rs.getString("nombre"));
                    alumno.put("carrera", rs.getString("carrera"));
                    alumnosCarreras.add(alumno);
                }

                runOnUiThread(() -> mostrarAlumnosCarreras(alumnosCarreras));
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "Error en la conexión con la base de datos.", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void mostrarAlumnosCarreras(ArrayList<HashMap<String, String>> alumnosCarreras) {
        alumnosContainer.removeAllViews();

        if (alumnosCarreras.isEmpty()) {
            TextView noResults = new TextView(this);
            noResults.setText("No se encontraron registros.");
            noResults.setPadding(16, 16, 16, 16);
            alumnosContainer.addView(noResults);
            return;
        }

        for (HashMap<String, String> alumno : alumnosCarreras) {
            TextView alumnoView = new TextView(this);
            String info = String.format("NumControl: %s\nNombre: %s\nCarrera: %s",
                    alumno.get("numControl"), alumno.get("nombre"), alumno.get("carrera"));
            alumnoView.setText(info);
            alumnoView.setPadding(16, 16, 16, 16);
            alumnoView.setBackgroundResource(R.drawable.item_background);
            alumnosContainer.addView(alumnoView);
        }
    }
}
