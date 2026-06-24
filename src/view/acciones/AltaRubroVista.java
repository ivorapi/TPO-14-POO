package view.acciones;

import controller.RubroController;
import modelo.Rubro;
import view.util.DialogoSwing;

import javax.swing.*;
import java.awt.*;

public class AltaRubroVista {
    private final Component padre;
    private final JTextArea areaResultado;
    private final RubroController rubroController;

    public AltaRubroVista(Component padre, JTextArea areaResultado, RubroController rubroController) {
        this.padre = padre;
        this.areaResultado = areaResultado;
        this.rubroController = rubroController;
    }

    public void ejecutar() {
        String nombre = DialogoSwing.pedirTextoObligatorio(padre, "Ingrese nombre del rubro:");
        if (nombre == null) {
            return;
        }

        Rubro rubroExistente = rubroController.buscarRubroPorNombre(nombre);
        if (rubroExistente != null) {
            DialogoSwing.mostrarError(padre, "Ya existe un rubro con ese nombre.");
            return;
        }

        String descripcion = DialogoSwing.pedirTextoObligatorio(padre, "Ingrese descripcion del rubro:");
        if (descripcion == null) {
            return;
        }

        Rubro rubro = new Rubro(nombre, descripcion);
        rubroController.agregarRubro(rubro);

        areaResultado.append("===== RUBRO CARGADO =====\n");
        areaResultado.append("Nombre: " + rubro.getNombre() + "\n");
        areaResultado.append("Descripcion: " + rubro.getDescripcion() + "\n\n");
    }
}