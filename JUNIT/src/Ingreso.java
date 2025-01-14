public class Ingreso {
    private final String categoria;  // La categoría del ingreso (nómina, venta, etc.)
    private final double cantidad;   // La cantidad del ingreso

    // Constructor
    public Ingreso(String categoria, double cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad del ingreso debe ser mayor que cero.");
        }
        if (categoria == null || categoria.trim().isEmpty()) {
            throw new IllegalArgumentException("La categoría no puede ser nula o vacía.");
        }
        this.categoria = categoria;
        this.cantidad = cantidad;
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

