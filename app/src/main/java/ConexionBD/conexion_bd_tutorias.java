package ConexionBD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class conexion_bd_tutorias {
    private static conexion_bd_tutorias instance; // Patrón Singleton
    private Connection conexion; // Conexión a la base de datos
    private PreparedStatement pstm; // Preparar declaraciones SQL
    private ResultSet rs; // Resultados de consultas

    // Constructor privado
    private conexion_bd_tutorias() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Cargar el driver MySQL
            String URL = "jdbc:mysql://localhost:3306/proyecto_tutorias";
            conexion = DriverManager.getConnection(URL, "laura", "laura");
            System.out.println("¡Conexión establecida correctamente!");
        } catch (ClassNotFoundException e) {
            System.out.println("Error en el controlador de conexión a MySQL: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Error en la conexión a la base de datos: " + e.getMessage());
        }
    }

    // Obtener la única instancia de la clase (Singleton)
    public static conexion_bd_tutorias getInstance() {
        if (instance == null) {
            instance = new conexion_bd_tutorias();
        }
        return instance;
    }

    // Obtener la conexión
    public Connection getConnection() {
        return conexion;
    }

    // Ejecutar instrucciones DML (INSERT, UPDATE, DELETE)
    public boolean ejecutarInstruccionDML(String instruccionDML, Object... parametros) {
        boolean result = false;
        try {
            pstm = conexion.prepareStatement(instruccionDML);
            for (int i = 0; i < parametros.length; i++) {
                pstm.setObject(i + 1, parametros[i]);
            }
            if (pstm.executeUpdate() >= 1) {
                result = true;
            }
        } catch (SQLException e) {
            System.out.println("Error al ejecutar instrucción DML: " + e.getMessage());
        }
        return result;
    }

    // Ejecutar consultas SQL
    public ResultSet ejecutarConsultaSQL(String consultaSQL, Object... parametros) {
        rs = null;
        try {
            pstm = conexion.prepareStatement(consultaSQL);
            for (int i = 0; i < parametros.length; i++) {
                pstm.setObject(i + 1, parametros[i]);
            }
            rs = pstm.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error al ejecutar consulta SQL: " + e.getMessage());
        }
        return rs;
    }

    // Cerrar conexión
    public void cerrarConexion() {
        try {
            if (rs != null) rs.close();
            if (pstm != null) pstm.close();
            if (conexion != null) conexion.close();
        } catch (SQLException e) {
            System.out.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }
}