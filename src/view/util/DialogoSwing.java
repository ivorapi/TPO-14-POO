package view.util;

import javax.swing.*;
import java.awt.*;

public class DialogoSwing {

    public static String pedirTextoObligatorio(Component padre, String mensaje) {
        while (true) {
            String texto = JOptionPane.showInputDialog(padre, mensaje);

            if (texto == null) {
                return null;
            }

            texto = texto.trim();

            if (!texto.isEmpty()) {
                return texto;
            }

            mostrarError(padre, "El campo no puede estar vacio.");
        }
    }

    public static Integer pedirEntero(Component padre, String mensaje) {
        while (true) {
            String texto = JOptionPane.showInputDialog(padre, mensaje);

            if (texto == null) {
                return null;
            }

            try {
                return Integer.parseInt(texto.trim());
            } catch (NumberFormatException e) {
                mostrarError(padre, "Debe ingresar un numero entero valido.");
            }
        }
    }

    public static Double pedirDouble(Component padre, String mensaje) {
        while (true) {
            String texto = JOptionPane.showInputDialog(padre, mensaje);

            if (texto == null) {
                return null;
            }

            try {
                return Double.parseDouble(texto.trim());
            } catch (NumberFormatException e) {
                mostrarError(padre, "Debe ingresar un numero valido.");
            }
        }
    }

    public static void mostrarError(Component padre, String mensaje) {
        JOptionPane.showMessageDialog(
                padre,
                mensaje,
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}