package org.example;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        Conexion conexion = null;

        try {
            conexion = new Conexion("usuarios.db");

            String dni = JOptionPane.showInputDialog(null, "Usuario, introduzca DNI:", "DNI", JOptionPane.QUESTION_MESSAGE);

            if (dni == null || dni.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Debe ingresar un DNI válido.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!conexion.verificarDni(dni)) {
                conexion.agregarUsuario(dni);
                JOptionPane.showMessageDialog(null, "Nuevo usuario creado.", "Información", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Usuario existente.", "Información", JOptionPane.INFORMATION_MESSAGE);
            }

            Usuarios usuario = new Usuarios(dni);

            mostrarMenu(usuario, conexion);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (conexion != null) {
                conexion.cerrarConexion();
            }
        }
    }

    private static void mostrarMenu(Usuarios usuario, Conexion conexion) {
        while (true) {
            String[] opciones = {"Agregar ingreso", "Agregar gasto", "Ver saldo final", "Salir"};
            int opcion = JOptionPane.showOptionDialog(
                    null,
                    "¿Qué desea realizar?",
                    "Menú principal",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opciones,
                    opciones[0]
            );

            switch (opcion) {
                case 0:
                    gestionarIngreso(usuario, conexion);
                    break;
                case 1:
                    gestionarGasto(usuario, conexion);
                    break;
                case 2:
                    JOptionPane.showMessageDialog(
                            null,
                            "Su saldo final es: " + usuario.calcularSaldo(),
                            "Saldo final",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    break;
                case 3:
                case JOptionPane.CLOSED_OPTION:
                    JOptionPane.showMessageDialog(null, "Gracias por usar el sistema.", "Adiós", JOptionPane.INFORMATION_MESSAGE);
                    return;
                default:
                    JOptionPane.showMessageDialog(null, "Opción no válida.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static void gestionarIngreso(Usuarios usuario, Conexion conexion) {
        String[] tiposIngreso = {"Nómina", "Venta en páginas de segunda mano"};
        String tipo = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione el tipo de ingreso:",
                "Ingreso",
                JOptionPane.QUESTION_MESSAGE,
                null,
                tiposIngreso,
                tiposIngreso[0]
        );

        if (tipo == null) return;

        String cantidadStr = JOptionPane.showInputDialog(null, "Ingrese la cantidad del ingreso:", "Cantidad", JOptionPane.QUESTION_MESSAGE);
        if (cantidadStr == null || cantidadStr.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar una cantidad válida.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double cantidad = Double.parseDouble(cantidadStr);
            Ingreso ingreso = new Ingreso(tipo, cantidad);
            usuario.agregarIngreso(ingreso);

            if ("Nómina".equalsIgnoreCase(tipo)) {
                JOptionPane.showMessageDialog(
                        null,
                        String.format("Se aplicó un 15%% de IRPF a la nómina. Ingreso final: %.2f", ingreso.getCantidad()),
                        "Ingreso registrado",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                JOptionPane.showMessageDialog(null, "Ingreso registrado.", "Ingreso registrado", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "La cantidad debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void gestionarGasto(Usuarios usuario, Conexion conexion) {
        String[] tiposGasto = {"Vacaciones", "Alquiler", "Vicios"};
        String tipo = (String) JOptionPane.showInputDialog(
                null,
                "Seleccione el tipo de gasto:",
                "Gasto",
                JOptionPane.QUESTION_MESSAGE,
                null,
                tiposGasto,
                tiposGasto[0]
        );

        if (tipo == null) return;

        String cantidadStr = JOptionPane.showInputDialog(null, "Ingrese la cantidad del gasto:", "Cantidad", JOptionPane.QUESTION_MESSAGE);
        if (cantidadStr == null || cantidadStr.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar una cantidad válida.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double cantidad = Double.parseDouble(cantidadStr);
            Gasto gasto = new Gasto(tipo, cantidad);
            usuario.agregarGasto(gasto);

            JOptionPane.showMessageDialog(null, "Gasto registrado.", "Gasto registrado", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "La cantidad debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
