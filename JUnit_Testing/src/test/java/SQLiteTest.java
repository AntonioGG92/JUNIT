import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class SQLiteTest {

    private static final String TEST_DB_URL = "jdbc:sqlite:test_finanzas.db";
    private Connection connection;

    @BeforeEach
    void setUp() {
        try {
            connection = DriverManager.getConnection(TEST_DB_URL);
            SQLite.url = TEST_DB_URL; // Apuntamos la URL al archivo de prueba
            SQLite.crearTablas();
        } catch (Exception e) {
            fail("Error al configurar la base de datos de prueba: " + e.getMessage());
        }
    }

    @AfterEach
    void tearDown() {
        try {
            if (connection != null) {
                connection.close();
            }
            java.nio.file.Files.deleteIfExists(java.nio.file.Paths.get("test_finanzas.db"));
        } catch (Exception e) {
            System.err.println("Error al limpiar la base de datos de prueba: " + e.getMessage());
        }
    }

    @Test
    void testCrearUsuario() {
        String dni = "12345678A";
        SQLite.crearUsuario(dni, 1000.0);

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM usuarios WHERE dni = '" + dni + "'")) {
            assertTrue(rs.next());
            assertEquals(dni, rs.getString("dni"));
            assertEquals(1000.0, rs.getDouble("saldo"));
        } catch (Exception e) {
            fail("Error al verificar la creación del usuario: " + e.getMessage());
        }
    }

    @Test
    void testIngresarDinero() {
        String dni = "12345678A";
        SQLite.crearUsuario(dni, 500.0);

        SQLite.ingresar(dni, "Nomina", 300.0);

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT saldo FROM usuarios WHERE dni = '" + dni + "'")) {
            assertTrue(rs.next());
            assertEquals(800.0, rs.getDouble("saldo")); // Verifica que el saldo se haya actualizado
        } catch (Exception e) {
            fail("Error al verificar el ingreso de dinero: " + e.getMessage());
        }
    }

    @Test
    void testGastarDinero() {
        String dni = "12345678A";
        SQLite.crearUsuario(dni, 1000.0);

        SQLite.gastar(dni, "Vacaciones", 200.0);

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT saldo FROM usuarios WHERE dni = '" + dni + "'")) {
            assertTrue(rs.next());
            assertEquals(800.0, rs.getDouble("saldo")); // Verifica que el saldo se haya actualizado
        } catch (Exception e) {
            fail("Error al verificar el gasto de dinero: " + e.getMessage());
        }
    }

    @Test
    void testIngresarYGastarDinero() {
        String dni = "12345678A";
        SQLite.crearUsuario(dni, 1000.0);

        SQLite.ingresar(dni, "Nomina", 500.0);
        SQLite.gastar(dni, "Alquiler", 300.0);

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT saldo FROM usuarios WHERE dni = '" + dni + "'")) {
            assertTrue(rs.next());
            assertEquals(1200.0, rs.getDouble("saldo")); // Verifica que el saldo se haya actualizado correctamente
        } catch (Exception e) {
            fail("Error al verificar ingresos y gastos combinados: " + e.getMessage());
        }
    }

    @Test
    void testTablasCreadasCorrectamente() {
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table'")) {
            boolean usuariosTable = false;
            boolean ingresosTable = false;
            boolean gastosTable = false;

            while (rs.next()) {
                String tableName = rs.getString("name");
                if (tableName.equals("usuarios")) usuariosTable = true;
                if (tableName.equals("ingresos")) ingresosTable = true;
                if (tableName.equals("gastos")) gastosTable = true;
            }

            assertTrue(usuariosTable, "La tabla 'usuarios' no fue creada.");
            assertTrue(ingresosTable, "La tabla 'ingresos' no fue creada.");
            assertTrue(gastosTable, "La tabla 'gastos' no fue creada.");
        } catch (Exception e) {
            fail("Error al verificar la creación de las tablas: " + e.getMessage());
        }
    }
}
