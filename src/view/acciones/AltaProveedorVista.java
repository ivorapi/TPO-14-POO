package view.acciones;

import controller.ProveedorController;
import controller.RubroController;
import modelo.Proveedor;
import modelo.Rubro;
import modelo.enums.CondicionImpositiva;
import view.util.DialogoSwing;
import view.util.SelectorSwing;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class AltaProveedorVista {
    private final Component padre;
    private final JTextArea areaResultado;
    private final ProveedorController proveedorController;
    private final RubroController rubroController;

    public AltaProveedorVista(
            Component padre,
            JTextArea areaResultado,
            ProveedorController proveedorController,
            RubroController rubroController
    ) {
        this.padre = padre;
        this.areaResultado = areaResultado;
        this.proveedorController = proveedorController;
        this.rubroController = rubroController;
    }

    public void ejecutar() {
        if (rubroController.getRubros().isEmpty()) {
            DialogoSwing.mostrarError(padre, "Primero debe cargar al menos un rubro.");
            return;
        }

        Integer cuit = DialogoSwing.pedirEntero(padre, "Ingrese CUIT del proveedor:");
        if (cuit == null) {
            return;
        }

        if (cuit <= 0) {
            DialogoSwing.mostrarError(padre, "El CUIT debe ser mayor a cero.");
            return;
        }

        if (proveedorController.buscarProveedorPorCuit(cuit) != null) {
            DialogoSwing.mostrarError(padre, "Ya existe un proveedor con ese CUIT.");
            return;
        }

        String razonSocial = DialogoSwing.pedirTextoObligatorio(padre, "Ingrese razon social:");
        if (razonSocial == null) {
            return;
        }

        String nombreComercial = DialogoSwing.pedirTextoObligatorio(padre, "Ingrese nombre comercial:");
        if (nombreComercial == null) {
            return;
        }

        String domicilio = DialogoSwing.pedirTextoObligatorio(padre, "Ingrese domicilio:");
        if (domicilio == null) {
            return;
        }

        Integer telefono = DialogoSwing.pedirEntero(padre, "Ingrese telefono:");
        if (telefono == null) {
            return;
        }

        if (telefono <= 0) {
            DialogoSwing.mostrarError(padre, "El telefono debe ser mayor a cero.");
            return;
        }

        String email = DialogoSwing.pedirTextoObligatorio(padre, "Ingrese email:");
        if (email == null) {
            return;
        }

        if (!email.contains("@") || !email.contains(".")) {
            DialogoSwing.mostrarError(padre, "El email ingresado no es valido.");
            return;
        }

        CondicionImpositiva condicion = SelectorSwing.seleccionarCondicionImpositiva(padre);
        if (condicion == null) {
            return;
        }

        Integer nroInscripcion = DialogoSwing.pedirEntero(padre, "Ingrese numero de inscripcion fiscal:");
        if (nroInscripcion == null) {
            return;
        }

        if (nroInscripcion <= 0) {
            DialogoSwing.mostrarError(padre, "El numero de inscripcion debe ser mayor a cero.");
            return;
        }

        Double limite = DialogoSwing.pedirDouble(padre, "Ingrese limite de deuda autorizado:");
        if (limite == null) {
            return;
        }

        if (limite <= 0) {
            DialogoSwing.mostrarError(padre, "El limite de deuda debe ser mayor a cero.");
            return;
        }

        Rubro rubro = SelectorSwing.seleccionarRubro(padre, rubroController, "Seleccione rubro del proveedor:");
        if (rubro == null) {
            return;
        }

        Proveedor proveedor = new Proveedor(
                cuit,
                razonSocial,
                nombreComercial,
                domicilio,
                telefono,
                email,
                condicion,
                nroInscripcion,
                new Date(),
                limite
        );

        proveedorController.agregarRubroAProveedor(proveedor, rubro);
        proveedorController.agregarProveedor(proveedor);

        areaResultado.append("===== PROVEEDOR CARGADO =====\n");
        areaResultado.append("CUIT: " + proveedor.getCuit() + "\n");
        areaResultado.append("Razon social: " + proveedor.getRazonSocial() + "\n");
        areaResultado.append("Condicion impositiva: " + proveedor.getCondicionImpositiva() + "\n");
        areaResultado.append("Limite deuda: " + proveedor.getLimiteDeudaAutorizado() + "\n");
        areaResultado.append("Rubro asociado: " + rubro.getNombre() + "\n\n");
    }
}