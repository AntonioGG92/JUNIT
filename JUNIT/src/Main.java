import java.util.Scanner;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Conexion conexion = null;

        try {
            // Conectar a la base de datos
            conexion = new Conexion("usuarios.db");

            // Pedir DNI del usuario
            System.out.print("Usuario, introduzca DNI: ");
            String dni = sc.nextLine();

            // Verificar si el usuario existe o crearlo
            if (!conexion.verificarDni(dni)) {
                conexion.agregarUsuario(dni);
                System.out.println("Nuevo usuario creado.");
            } else {
                System.out.println("Usuario existente.");
            }

            // Crear instancia del usuario
            Usuarios usuarios = new Usuarios(dni);

            // Mostrar menú principal
            mostrarMenu(sc, usuarios);

        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            if (conexion != null) {
                conexion.cerrarConexion();
            }
        }
    }

    // Método para mostrar el menú principal
    private static void mostrarMenu(Scanner sc, Usuarios usuario) {
        while (true) {
            System.out.println("\n¿Qué desea realizar?");
            System.out.println("1. Ingreso");
            System.out.println("2. Gasto");
            System.out.println("3. Ver saldo final");
            System.out.println("4. Salir");
            System.out.print("Seleccione opción: ");

            int opcion = leerEntero(sc);

            switch (opcion) {
                case 1:
                    gestionarGasto(sc, usuario);
                    break;
                case 2:
                    gestionarIngreso(sc, usuario);
                    break;
                case 3:
                    System.out.println("Su saldo final es: " + usuario.calcularSaldo());
                    break;
                case 4:
                    System.out.println("Gracias por usar el sistema.");
                    return;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }
        }
    }

    // Método para gestionar gastos
    private static void gestionarGasto(Scanner sc, Usuarios usuario) {
        System.out.println("¿Qué tipo de gasto?");
        System.out.println("1. Vacaciones");
        System.out.println("2. Alquiler");
        System.out.println("3. IRPF");
        System.out.println("4. Vicios");
        System.out.print("Seleccione tipo: ");

        int tipo = leerEntero(sc);

        System.out.print("Cantidad: ");
        double cantidad = leerDouble(sc);

        Gasto gasto = null;
        switch (tipo) {
            case 1 -> gasto = new Gasto("Vacaciones", cantidad);
            case 2 -> gasto = new Gasto("Alquiler", cantidad);
            case 3 -> gasto = new Gasto("IRPF", cantidad * 0.15); // IRPF es el 15% de la cantidad
            case 4 -> gasto = new Gasto("Vicios", cantidad);
            default -> System.out.println("Tipo de gasto no válido.");
        }

        if (gasto != null) {
            usuario.agregarGasto(gasto);
            System.out.println("Gasto registrado. Su saldo final es: " + usuario.calcularSaldo());
        }
    }

    // Método para gestionar ingresos
    private static void gestionarIngreso(Scanner sc, Usuarios usuario) {
        System.out.println("¿Qué tipo de ingreso?");
        System.out.println("1. Nómina");
        System.out.println("2. Venta en páginas de segunda mano");
        System.out.print("Seleccione tipo: ");

        int tipo = leerEntero(sc);

        System.out.print("Cantidad: ");
        double cantidad = leerDouble(sc);

        Ingreso ingreso = null;
        switch (tipo) {
            case 1 -> ingreso = new Ingreso("Nómina", cantidad);
            case 2 -> ingreso = new Ingreso("Venta en páginas", cantidad);
            default -> System.out.println("Tipo de ingreso no válido.");
        }

        if (ingreso != null) {
            usuario.agregarIngreso(ingreso);
            System.out.println("Ingreso registrado. Su saldo final es: " + usuario.calcularSaldo());
        }
    }

    // Método para leer enteros con manejo de excepciones
    private static int leerEntero(Scanner sc) {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Entrada inválida. Ingrese un número: ");
            }
        }
    }

    // Método para leer dobles con manejo de excepciones
    private static double leerDouble(Scanner sc) {
        while (true) {
            try {
                return Double.parseDouble(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Entrada inválida. Ingrese un número válido: ");
            }
        }
    }
}

