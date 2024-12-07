package com.example;

import android.annotation.SuppressLint;
import android.os.Bundle;
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

public class controller_tigger extends AppCompatActivity {

    private LinearLayout historialContainer;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_cambios);

        // Referencia al contenedor del historial
        historialContainer = findViewById(R.id.historial_container);

        // Cargar historial de cambios
        cargarHistorialCambios();
    }

    private void cargarHistorialCambios() {
        new Thread(() -> {
            conexion_bd_tutorias conexion = conexion_bd_tutorias.getInstance();
            ArrayList<HashMap<String, String>> historialCambios = new ArrayList<>();

            try {
                // Consulta a la base de datos
                String query = "SELECT * FROM historial_cambios_alumnos ORDER BY fecha DESC";
                ResultSet rs = conexion.ejecutarConsultaSQL(query);

                while (rs.next()) {
                    HashMap<String, String> cambio = new HashMap<>();
                    cambio.put("numControl", rs.getString("numControl"));
                    cambio.put("campo", rs.getString("campo"));
                    cambio.put("valorAnterior", rs.getString("valorAnterior"));
                    cambio.put("valorNuevo", rs.getString("valorNuevo"));
                    cambio.put("fecha", rs.getString("fecha"));
                    historialCambios.add(cambio);
                }

                runOnUiThread(() -> mostrarHistorialCambios(historialCambios));
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "Error al conectar con la base de datos.", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void mostrarHistorialCambios(ArrayList<HashMap<String, String>> historialCambios) {
        historialContainer.removeAllViews();

        if (historialCambios.isEmpty()) {
            TextView noResults = new TextView(this);
            noResults.setText("No se encontraron registros en el historial.");
            noResults.setPadding(16, 16, 16, 16);
            historialContainer.addView(noResults);
            return;
        }

        for (HashMap<String, String> cambio : historialCambios) {
            TextView cambioView = new TextView(this);
            String info = String.format("NumControl: %s\nCampo: %s\nValor Anterior: %s\nValor Nuevo: %s\nFecha: %s",
                    cambio.get("numControl"),
                    cambio.get("campo"),
                    cambio.get("valorAnterior"),
                    cambio.get("valorNuevo"),
                    cambio.get("fecha"));
            cambioView.setText(info);
            cambioView.setPadding(16, 16, 16, 16);
            cambioView.setBackgroundResource(R.drawable.item_background);
            historialContainer.addView(cambioView);
        }
    }
}
