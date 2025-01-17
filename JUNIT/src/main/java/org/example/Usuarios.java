package org.example;

import java.util.ArrayList;
import java.util.List;

public class Usuarios {
    private final String dni;
    private final List<Ingreso> ingresos;
    private final List<Gasto> gastos;

    public Usuarios(String dni) {
        if (dni == null || !esDniValido(dni)) {
            throw new IllegalArgumentException("DNI no válido.");
        }
        this.dni = dni;
        this.ingresos = new ArrayList<>();
        this.gastos = new ArrayList<>();
    }

    public String getDni() {
        return dni;
    }

    public void agregarIngreso(Ingreso ingreso) {
        if (ingreso == null) {
            throw new IllegalArgumentException("El ingreso no puede ser nulo.");
        }
        ingresos.add(ingreso);
    }

    public void agregarGasto(Gasto gasto) {
        if (gasto == null) {
            throw new IllegalArgumentException("El gasto no puede ser nulo.");
        }
        gastos.add(gasto);
    }

    public double calcularSaldo() {
        double totalIngresos = ingresos.stream().mapToDouble(Ingreso::getCantidad).sum();
        double totalGastos = gastos.stream().mapToDouble(Gasto::getCantidad).sum();
        return totalIngresos - totalGastos;
    }

    private boolean esDniValido(String dni) {
        return dni.matches("\\d{8}[A-Z]");
    }

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

