package ConexionBD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class conexion_bd_usuarios {
    // Instancia única (Singleton)
    private static conexion_bd_usuarios instancia = null;

    private Connection conexion;
    private PreparedStatement pstm;
    private ResultSet rs;

    // Credenciales de la base de datos
    private final String host = "localhost";
    private final String usuario = "laura";
    private final String password = "laura";
    private final String bd = "usuario_bd_escuela";

    // Constructor privado
    private conexion_bd_usuarios() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Cargar el driver MySQL
            String url = "jdbc:mysql://" + host + ":3306/" + bd;
            conexion = DriverManager.getConnection(url, usuario, password);
            System.out.println("Conexión exitosa a la base de datos 'usuario_bd_escuela'");
        } catch (ClassNotFoundException e) {
            System.out.println("Error al cargar el driver MySQL: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos: " + e.getMessage());
        }
    }

    // Método para obtener la instancia única
    public static conexion_bd_usuarios getInstancia() {
        if (instancia == null) {
            instancia = new conexion_bd_usuarios();
        }
        return instancia;
    }

    // Obtener la conexión
    public Connection getConexion() {
        return conexion;
    }

    // Validar usuario
    public boolean validarUsuario(String usuario, String password) {
        boolean esValido = false;
        String query = "SELECT * FROM usuarios WHERE Nombre_Usuario = ? AND Password = ?";
        try {
            pstm = conexion.prepareStatement(query);
            pstm.setString(1, usuario);
            pstm.setString(2, password);
            rs = pstm.executeQuery();

            // Verificar si existe el usuario
            if (rs.next()) {
                esValido = true;
            }
        } catch (SQLException e) {
            System.out.println("Error al validar usuario: " + e.getMessage());
        }
        return esValido;
    }

    // Cerrar conexión
    public void cerrarConexion() {
        try {
            if (rs != null) rs.close();
            if (pstm != null) pstm.close();
            if (conexion != null) conexion.close();
            System.out.println("Conexión cerrada correctamente");
        } catch (SQLException e) {
            System.out.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }
}