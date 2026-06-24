package view.acciones;

import controller.DocumentoComercialController;
import controller.OrdenCompraController;
import controller.UsuarioController;
import modelo.Factura;
import modelo.LineaOrdenCompra;
import modelo.OrdenCompra;
import modelo.Usuario;
import modelo.enums.RolUsuario;
import view.EstadoVista;
import view.util.DialogoSwing;
import view.util.SelectorSwing;

import javax.swing.*;
import java.awt.*;

public class RegistrarFacturaVista {
    private final Component padre;
    private final JTextArea areaResultado;
    private final OrdenCompraController ordenCompraController;
    private final DocumentoComercialController documentoController;
    private final UsuarioController usuarioController;
    private final EstadoVista estadoVista;

    public RegistrarFacturaVista(
            Component padre,
            JTextArea areaResultado,
            OrdenCompraController ordenCompraController,
            DocumentoComercialController documentoController,
            UsuarioController usuarioController,
            EstadoVista estadoVista
    ) {
        this.padre = padre;
        this.areaResultado = areaResultado;
        this.ordenCompraController = ordenCompraController;
        this.documentoController = documentoController;
        this.usuarioController = usuarioController;
        this.estadoVista = estadoVista;
    }

    public void ejecutar() {
        if (ordenCompraController.getOrdenesCompra().isEmpty()) {
            DialogoSwing.mostrarError(padre, "Primero debe crear una Orden de Compra.");
            return;
        }

        OrdenCompra ordenCompra = SelectorSwing.seleccionarOrdenCompra(
                padre,
                ordenCompraController,
                "Seleccione Orden de Compra para facturar:"
        );

        if (ordenCompra == null) {
            return;
        }

        if (ordenCompra.getLineas().isEmpty()) {
            DialogoSwing.mostrarError(padre, "La Orden de Compra seleccionada no tiene lineas.");
            return;
        }

        Integer nroFactura = DialogoSwing.pedirEntero(
                padre,
                "Ingrese numero de factura sugerido: " + estadoVista.getProximoNumeroFactura()
        );

        if (nroFactura == null) {
            return;
        }

        if (nroFactura <= 0) {
            DialogoSwing.mostrarError(padre, "El numero de factura debe ser mayor a cero.");
            return;
        }

        if (documentoController.buscarDocumentoPorNumero(nroFactura) != null) {
            DialogoSwing.mostrarError(padre, "Ya existe un documento con ese numero.");
            return;
        }

        Factura factura = documentoController.crearFactura(
                nroFactura,
                ordenCompra.getProveedor()
        );

        for (LineaOrdenCompra linea : ordenCompra.getLineas()) {
            Double precioFactura = DialogoSwing.pedirDouble(
                    padre,
                    "Precio facturado para item: " + linea.getItem().getDescripcion()
                            + "\nPrecio acordado en OC: " + linea.getPrecioUnitarioAcordado()
            );

            if (precioFactura == null) {
                return;
            }

            if (precioFactura <= 0) {
                DialogoSwing.mostrarError(padre, "El precio facturado debe ser mayor a cero.");
                return;
            }

            documentoController.agregarLineaFactura(
                    factura,
                    linea.getItem(),
                    linea.getCantidad(),
                    precioFactura
            );
        }

        Usuario usuarioSupervisor = null;

        if (!factura.validarPrecios(ordenCompra)) {
            int opcion = JOptionPane.showConfirmDialog(
                    padre,
                    "La factura tiene diferencias de precio con la OC.\n¿Desea aprobarla como supervisor?",
                    "Validacion de factura",
                    JOptionPane.YES_NO_OPTION
            );

            if (opcion == JOptionPane.YES_OPTION) {
                usuarioSupervisor = crearSupervisorTemporal();
            }
        }

        boolean facturaValida = documentoController.validarYRegistrarFactura(
                factura,
                ordenCompra,
                usuarioSupervisor
        );

        estadoVista.actualizarProximoNumeroFactura(nroFactura);

        areaResultado.append("===== FACTURA REGISTRADA =====\n");
        areaResultado.append("Numero factura: " + factura.getNroDocumento() + "\n");
        areaResultado.append("Proveedor: " + factura.getProveedor().getRazonSocial() + "\n");
        areaResultado.append("OC asociada: " + ordenCompra.getNumero() + "\n");
        areaResultado.append("Factura valida: " + facturaValida + "\n");
        areaResultado.append("Estado factura: " + factura.getEstado() + "\n");
        areaResultado.append("Importe factura: " + factura.getImporteTotal() + "\n");
        areaResultado.append("Deuda actual proveedor: "
                + documentoController.calcularDeudaActualProveedor(factura.getProveedor()) + "\n\n");
    }

    private Usuario crearSupervisorTemporal() {
        Usuario supervisor = new Usuario(
                "Supervisor",
                "supervisor@sanatorio.com",
                "1234",
                RolUsuario.SUPERVISOR
        );

        usuarioController.agregarUsuario(supervisor);

        return supervisor;
    }
}