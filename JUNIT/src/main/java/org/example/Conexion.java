package org.example;

import java.sql.*;

public class Conexion {

    private Connection conn;

    // Constructor para inicializar la conexión con la base de datos SQLite
    public Conexion(String dbFilePath) {
        try {
            // Establecer conexión
            conn = DriverManager.getConnection("jdbc:sqlite:" + dbFilePath);
            System.out.println("Conexión establecida con la base de datos.");

            // Inicializar la base de datos (crear tablas si no existen)
            inicializarBaseDeDatos();

        } catch (SQLException e) {
            System.err.println("Error al conectar con la base de datos: " + e.getMessage());
        }
    }

    // Método para verificar si el DNI ya está registrado
    public boolean verificarDni(String dni) {
        String query = "SELECT COUNT(*) AS total FROM usuarios WHERE dni = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, dni);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total") > 0; // Retorna true si el DNI ya existe
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar el DNI: " + e.getMessage());
        }
        return false;
    }

    // Método para agregar un nuevo usuario
    public void agregarUsuario(String dni) {
        if (verificarDni(dni)) {
            System.err.println("El DNI ya está registrado: " + dni);
            return;
        }
        String query = "INSERT INTO usuarios (dni) VALUES (?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, dni);
            stmt.executeUpdate();
            System.out.println("Usuario agregado con DNI: " + dni);
        } catch (SQLException e) {
            System.err.println("Error al agregar el usuario: " + e.getMessage());
        }
    }

    // Método para inicializar la base de datos (crear tablas si no existen)
    private void inicializarBaseDeDatos() {
        String crearUsuarios = """
            CREATE TABLE IF NOT EXISTS usuarios (
                dni TEXT PRIMARY KEY,
                saldo REAL DEFAULT 0.0
            );
        """;

        String crearTransacciones = """
            CREATE TABLE IF NOT EXISTS transacciones (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                dni_usuario TEXT NOT NULL,
                tipo TEXT NOT NULL,
                concepto TEXT NOT NULL,
                cantidad REAL NOT NULL,
                FOREIGN KEY (dni_usuario) REFERENCES usuarios (dni)
            );
        """;

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(crearUsuarios);
            stmt.execute(crearTransacciones);
            System.out.println("Tablas inicializadas correctamente.");
        } catch (SQLException e) {
            System.err.println("Error al inicializar la base de datos: " + e.getMessage());
        }
    }

    // Método para cerrar la conexión
    public void cerrarConexion() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("Conexión cerrada.");
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }
}

