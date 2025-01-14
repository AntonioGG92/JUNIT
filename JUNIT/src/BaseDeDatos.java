import java.sql.*;

public class BaseDeDatos {

    private Connection conn;

    // Constructor: Establece la conexión con la base de datos
    public BaseDeDatos(String dbPath) {
        try {
            this.conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            System.out.println("Conexión establecida con la base de datos.");
        } catch (SQLException e) {
            System.err.println("Error al conectar con la base de datos: " + e.getMessage());
        }
    }

    // Crear tablas si no existen
    public void crearTablas() {
        String crearUsuarios = """
            CREATE TABLE IF NOT EXISTS usuarios (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                dni TEXT UNIQUE NOT NULL
            );
        """;

        String crearGastos = """
            CREATE TABLE IF NOT EXISTS gastos (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                usuario_id INTEGER NOT NULL,
                cantidad REAL NOT NULL,
                descripcion TEXT,
                FOREIGN KEY (usuario_id) REFERENCES usuarios (id)
            );
        """;

        String crearIngresos = """
            CREATE TABLE IF NOT EXISTS ingresos (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                usuario_id INTEGER NOT NULL,
                cantidad REAL NOT NULL,
                descripcion TEXT,
                FOREIGN KEY (usuario_id) REFERENCES usuarios (id)
            );
        """;

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(crearUsuarios);
            stmt.execute(crearGastos);
            stmt.execute(crearIngresos);
            System.out.println("Tablas creadas o ya existían.");
        } catch (SQLException e) {
            System.err.println("Error al crear tablas: " + e.getMessage());
        }
    }

    // Insertar un usuario
    public void insertarUsuario(String dni) {
        String query = "INSERT INTO usuarios (dni) VALUES (?)";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, dni);
            ps.executeUpdate();
            System.out.println("Usuario insertado: " + dni);
        } catch (SQLException e) {
            System.err.println("Error al insertar usuario: " + e.getMessage());
        }
    }

    // Insertar un gasto
    public void insertarGasto(int usuarioId, double cantidad, String descripcion) {
        String query = "INSERT INTO gastos (usuario_id, cantidad, descripcion) VALUES (?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, usuarioId);
            ps.setDouble(2, cantidad);
            ps.setString(3, descripcion);
            ps.executeUpdate();
            System.out.println("Gasto insertado para el usuario ID " + usuarioId);
        } catch (SQLException e) {
            System.err.println("Error al insertar gasto: " + e.getMessage());
        }
    }

    // Insertar un ingreso
    public void insertarIngreso(int usuarioId, double cantidad, String descripcion) {
        String query = "INSERT INTO ingresos (usuario_id, cantidad, descripcion) VALUES (?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, usuarioId);
            ps.setDouble(2, cantidad);
            ps.setString(3, descripcion);
            ps.executeUpdate();
            System.out.println("Ingreso insertado para el usuario ID " + usuarioId);
        } catch (SQLException e) {
            System.err.println("Error al insertar ingreso: " + e.getMessage());
        }
    }

    // Consultar todos los usuarios
    public void consultarUsuarios() {
        String query = "SELECT * FROM usuarios";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("Usuarios:");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") + ", DNI: " + rs.getString("dni"));
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar usuarios: " + e.getMessage());
        }
    }

    // Consultar gastos por usuario
    public void consultarGastos(int usuarioId) {
        String query = "SELECT * FROM gastos WHERE usuario_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, usuarioId);

            try (ResultSet rs = ps.executeQuery()) {
                System.out.println("Gastos para el usuario ID " + usuarioId + ":");
                while (rs.next()) {
                    System.out.println("Cantidad: " + rs.getDouble("cantidad") + ", Descripción: " + rs.getString("descripcion"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar gastos: " + e.getMessage());
        }
    }

    // Consultar ingresos por usuario
    public void consultarIngresos(int usuarioId) {
        String query = "SELECT * FROM ingresos WHERE usuario_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, usuarioId);

            try (ResultSet rs = ps.executeQuery()) {
                System.out.println("Ingresos para el usuario ID " + usuarioId + ":");
                while (rs.next()) {
                    System.out.println("Cantidad: " + rs.getDouble("cantidad") + ", Descripción: " + rs.getString("descripcion"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar ingresos: " + e.getMessage());
        }
    }

    // Cerrar la conexión
    public void cerrarConexion() {
        try {
            if (conn != null) {
                conn.close();
                System.out.println("Conexión cerrada.");
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }
}
