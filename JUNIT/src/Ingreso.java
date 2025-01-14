public class Ingreso {
    private final String categoria;  // La categoría del ingreso (nómina, venta, etc.)
    private final double cantidad;   // La cantidad final del ingreso después de aplicar el IRPF (si aplica)
    private static final double IRPF = 0.15;  // Porcentaje del IRPF (15%)

    // Constructor
    public Ingreso(String categoria, double cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad del ingreso debe ser mayor que cero.");
        }
        if (categoria == null || categoria.trim().isEmpty()) {
            throw new IllegalArgumentException("La categoría no puede ser nula o vacía.");
        }

        // Limpieza y validación de la categoría
        this.categoria = categoria.trim();

        // Depuración: Imprimir categoría para verificar
        System.out.println("Categoría del ingreso: " + this.categoria);

        // Si el ingreso es una nómina, aplicamos el IRPF automáticamente
        if (this.categoria.equalsIgnoreCase("Nómina")) {
            this.cantidad = cantidad * (1 - IRPF);  // Aplica el 15% de IRPF
            System.out.printf("IRPF aplicado a la nómina: %.2f (Ingreso final: %.2f)%n", cantidad * IRPF, this.cantidad);
        } else {
            this.cantidad = cantidad;  // Otros ingresos no tienen descuentos
        }
    }

    // Getter para la categoría
    public String getCategoria() {
        return categoria;
    }

    // Getter para la cantidad
    public double getCantidad() {
        return cantidad;
    }

    // Método toString para mostrar información del ingreso
    @Override
    public String toString() {
        return "Ingreso{" +
                "categoria='" + categoria + '\'' +
                ", cantidad=" + cantidad +
                '}';
    }
}

