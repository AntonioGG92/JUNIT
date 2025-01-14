public class Gasto {
    private final String categoria;  // La categoría del gasto (vacaciones, alquiler, etc.)
    private final double cantidad;   // La cantidad del gasto

    // Constructor
    public Gasto(String categoria, double cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad del gasto debe ser mayor que cero.");
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

    // Método toString para mostrar información del gasto
    @Override
    public String toString() {
        return "Gasto{" +
                "categoria='" + categoria + '\'' +
                ", cantidad=" + cantidad +
                '}';
    }
}
