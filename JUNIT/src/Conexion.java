import java.sql.*;

public class Conexion {

    private Connection conn;

    // Constructor para inicializar la conexión con la base de datos SQLite
    public Conexion(String dbFilePath) throws SQLException {
        try {
            // Cargar el controlador JDBC para SQLite
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:" + dbFilePath);
            System.out.println("Conexión establecida con la base de datos.");
        } catch (ClassNotFoundException e) {
            throw new SQLException("No se pudo cargar el controlador JDBC de SQLite.", e);
        }
    }

    // Método para verificar si el DNI ya está registrado
    public boolean verificarDni(String dni) throws SQLException {
        String query = "SELECT * FROM usuarios WHERE dni = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, dni);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // Retorna true si el DNI ya existe
            }
        }
    }

    // Método para agregar un nuevo usuario
    public void agregarUsuario(String dni) throws SQLException {
        if (verificarDni(dni)) {
            throw new SQLException("El DNI ya está registrado: " + dni);
        }
        String query = "INSERT INTO usuarios (dni) VALUES (?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, dni);
            stmt.executeUpdate();
            System.out.println("Usuario agregado con DNI: " + dni);
        }
    }

    // Método para ejecutar consultas genéricas (opcional)
    public ResultSet ejecutarConsulta(String sql) throws SQLException {
        Statement stmt = conn.createStatement();
        return stmt.executeQuery(sql);
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