package com.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import ConexionBD.conexion_bd_tutorias;

public class controller_alumno {

    private Connection conexion;

    public controller_alumno() {
        this.conexion = conexion_bd_tutorias.getInstance().getConnection();
    }

    // ======================== MÉTODOS ABCC (CRUD) ========================

    // ------------------ MÉTODO DE ALTAS ------------------
    public boolean agregarAlumno(String numControl, String nombre, String apellidoP, String apellidoM, String fecha_nacimiento, String telefono, String email, String Carrera_carrera_id) {
        String sql = "INSERT INTO alumnos (numControl, nombre, apellidoP, apellidoM, fecha_nacimiento, telefono, email, Carrera_carrera_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstm = conexion.prepareStatement(sql)) {
            pstm.setString(1, numControl);
            pstm.setString(2, nombre);
            pstm.setString(3, apellidoP);
            pstm.setString(4, apellidoM);
            pstm.setString(5, fecha_nacimiento);
            pstm.setString(6, telefono);
            pstm.setString(7, email);
            pstm.setString(8, Carrera_carrera_id);
            return pstm.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al ejecutar la consulta: " + e.getMessage());
            return false;
        }
    }

    public boolean existeAlumno(String numControl) {
        String sql = "SELECT COUNT(*) as total FROM alumnos WHERE numControl = ?";
        try (PreparedStatement pstm = conexion.prepareStatement(sql)) {
            pstm.setString(1, numControl);
            ResultSet res = pstm.executeQuery();
            if (res.next()) {
                return res.getInt("total") > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error al ejecutar la consulta: " + e.getMessage());
        }
        return false;
    }

    // ------------------ MÉTODO DE BAJAS ------------------
    public boolean registrarBaja(String numControl) {
        try {
            conexion.setAutoCommit(false);

            String selectSQL = "SELECT numControl, nombre, apellidoP, apellidoM, 'Baja solicitada' FROM listado_alumnos_carreras WHERE numControl = ?";
            try (PreparedStatement selectPstm = conexion.prepareStatement(selectSQL)) {
                selectPstm.setString(1, numControl);
                ResultSet res = selectPstm.executeQuery();
                if (!res.next()) {
                    throw new SQLException("No se encontraron datos para el numControl: " + numControl);
                }
            }

            String insertSQL = "INSERT INTO historial_bajas (numControl, nombre, apellidoP, apellidoM, motivo) " +
                    "SELECT numControl, nombre, apellidoP, apellidoM, 'Baja solicitada' FROM listado_alumnos_carreras WHERE numControl = ?";
            try (PreparedStatement insertPstm = conexion.prepareStatement(insertSQL)) {
                insertPstm.setString(1, numControl);
                if (insertPstm.executeUpdate() == 0) {
                    throw new SQLException("Error al insertar en historial_bajas");
                }
            }

            String deleteSQL = "DELETE FROM alumnos WHERE numControl = ?";
            try (PreparedStatement deletePstm = conexion.prepareStatement(deleteSQL)) {
                deletePstm.setString(1, numControl);
                if (deletePstm.executeUpdate() == 0) {
                    throw new SQLException("Error al eliminar el alumno");
                }
            }

            conexion.commit();
            return true;
        } catch (SQLException e) {
            try {
                conexion.rollback();
            } catch (SQLException rollbackEx) {
                System.out.println("Error al revertir los cambios: " + rollbackEx.getMessage());
            }
            System.out.println("Error al registrar baja: " + e.getMessage());
            return false;
        } finally {
            try {
                conexion.setAutoCommit(true);
            } catch (SQLException ex) {
                System.out.println("Error al restaurar el auto-commit: " + ex.getMessage());
            }
        }
    }

    public boolean eliminarAlumno(String numControl) {
        String sql = "DELETE FROM alumnos WHERE numControl = ?";
        try (PreparedStatement pstm = conexion.prepareStatement(sql)) {
            pstm.setString(1, numControl);
            return pstm.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al ejecutar la consulta: " + e.getMessage());
            return false;
        }
    }

    // ------------------ MÉTODO DE CAMBIOS ------------------
    public boolean modificarAlumno(String numControl, String nombre, String apellidoP, String apellidoM, String fecha_nacimiento, String telefono, String email, String Carrera_carrera_id) {
        String sql = "UPDATE alumnos SET nombre = ?, apellidoP = ?, apellidoM = ?, fecha_nacimiento = ?, telefono = ?, email = ?, Carrera_carrera_id = ? WHERE numControl = ?";
        try (PreparedStatement pstm = conexion.prepareStatement(sql)) {
            pstm.setString(1, nombre);
            pstm.setString(2, apellidoP);
            pstm.setString(3, apellidoM);
            pstm.setString(4, fecha_nacimiento);
            pstm.setString(5, telefono);
            pstm.setString(6, email);
            pstm.setString(7, Carrera_carrera_id);
            pstm.setString(8, numControl);
            return pstm.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al ejecutar la consulta: " + e.getMessage());
            return false;
        }
    }

    public ResultSet obtenerTodosLosAlumnos() {
        String sql = "SELECT * FROM alumnos";
        try {
            PreparedStatement pstm = conexion.prepareStatement(sql);
            return pstm.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error al ejecutar la consulta: " + e.getMessage());
            return null;
        }
    }

    // ------------------ MÉTODO DE CONSULTAS ------------------
    public ResultSet mostrarAlumnos() {
        String sql = "SELECT * FROM alumnos";
        try {
            PreparedStatement pstm = conexion.prepareStatement(sql);
            return pstm.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error al ejecutar la consulta: " + e.getMessage());
            return null;
        }
    }

    public ResultSet obtenerAlumnoPorNumControl(String numControl) {
        String sql = "SELECT * FROM alumnos WHERE numControl = ?";
        try (PreparedStatement pstm = conexion.prepareStatement(sql)) {
            pstm.setString(1, numControl);
            return pstm.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error al ejecutar la consulta: " + e.getMessage());
            return null;
        }
    }

    public ResultSet infoAlumnos() {
        String sql = "SELECT * FROM alumnos";
        try {
            PreparedStatement pstm = conexion.prepareStatement(sql);
            return pstm.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error en la consulta: " + e.getMessage());
            return null;
        }
    }

    // ------------------ MÉTODOS DE TRANSACTORES ------------------
    public void iniciarTransaccion() {
        try {
            conexion.setAutoCommit(false);
        } catch (SQLException e) {
            System.out.println("Error al iniciar la transacción: " + e.getMessage());
        }
    }

    public void confirmarTransaccion() {
        try {
            conexion.commit();
        } catch (SQLException e) {
            System.out.println("Error al confirmar la transacción: " + e.getMessage());
        }
    }
}