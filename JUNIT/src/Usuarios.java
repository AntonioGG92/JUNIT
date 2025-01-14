import java.util.ArrayList;
import java.util.List;

public class Usuarios {
    private final String dni;                   // Identificador único del usuario
    private final List<Ingreso> ingresos;      // Lista de ingresos del usuario
    private final List<Gasto> gastos;          // Lista de gastos del usuario

    // Constructor
    public Usuarios(String dni) {
        if (dni == null || !esDniValido(dni)) {
            throw new IllegalArgumentException("DNI no válido.");
        }
        this.dni = dni;
        this.ingresos = new ArrayList<>();
        this.gastos = new ArrayList<>();
    }

    // Getter para el DNI
    public String getDni() {
        return dni;
    }

    // Método para agregar un gasto
    public void agregarGasto(Gasto gasto) {
        if (gasto == null) {
            throw new IllegalArgumentException("El gasto no puede ser nulo.");
        }
        gastos.add(gasto);
    }

    // Método para agregar un ingreso
    public void agregarIngreso(Ingreso ingreso) {
        if (ingreso == null) {
            throw new IllegalArgumentException("El ingreso no puede ser nulo.");
        }
        ingresos.add(ingreso);
    }

    // Método para calcular el saldo del usuario
    public double calcularSaldo() {
        double totalIngresos = ingresos.stream().mapToDouble(Ingreso::getCantidad).sum();
        double totalGastos = gastos.stream().mapToDouble(Gasto::getCantidad).sum();
        return totalIngresos - totalGastos;
    }

    // Validación completa del DNI español
    private boolean esDniValido(String dni) {
        // Verificar que el formato básico sea correcto (8 dígitos + 1 letra)
        if (!dni.matches("\\d{8}[A-Z]")) {
            return false;
        }

        // Extraer el número y la letra
        String numeros = dni.substring(0, 8);
        char letra = dni.charAt(8);

        // Tabla de letras válidas según el cálculo del módulo 23
        String letrasValidas = "TRWAGMYFPDXBNJZSQVHLCKE";

        // Calcular la letra esperada
        int numero = Integer.parseInt(numeros);
        char letraEsperada = letrasValidas.charAt(numero % 23);

        // Comparar la letra esperada con la letra proporcionada
        return letra == letraEsperada;
    }

    // Método para mostrar la información del usuario
    @Override
    public String toString() {
        return "Usuario{" +
                "dni='" + dni + '\'' +
                ", ingresos=" + ingresos +
                ", gastos=" + gastos +
                ", saldo=" + calcularSaldo() +
                '}';
    }
}

