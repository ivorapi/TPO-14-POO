package view.acciones;

import controller.ItemController;
import controller.OrdenCompraController;
import controller.ProveedorController;
import modelo.Item;
import modelo.OrdenCompra;
import modelo.Proveedor;
import view.EstadoVista;
import view.util.DialogoSwing;
import view.util.SelectorSwing;

import javax.swing.*;
import java.awt.*;

public class CrearOrdenCompraVista {
    private final Component padre;
    private final JTextArea areaResultado;
    private final ProveedorController proveedorController;
    private final ItemController itemController;
    private final OrdenCompraController ordenCompraController;
    private final EstadoVista estadoVista;

    public CrearOrdenCompraVista(
            Component padre,
            JTextArea areaResultado,
            ProveedorController proveedorController,
            ItemController itemController,
            OrdenCompraController ordenCompraController,
            EstadoVista estadoVista
    ) {
        this.padre = padre;
        this.areaResultado = areaResultado;
        this.proveedorController = proveedorController;
        this.itemController = itemController;
        this.ordenCompraController = ordenCompraController;
        this.estadoVista = estadoVista;
    }

    public void ejecutar() {
        if (proveedorController.getProveedores().isEmpty()) {
            DialogoSwing.mostrarError(padre, "Primero debe cargar al menos un proveedor.");
            return;
        }

        if (itemController.getItems().isEmpty()) {
            DialogoSwing.mostrarError(padre, "Primero debe cargar al menos un item.");
            return;
        }

        Proveedor proveedor = SelectorSwing.seleccionarProveedor(
                padre,
                proveedorController,
                "Seleccione proveedor para la OC:"
        );

        if (proveedor == null) {
            return;
        }

        Item item = SelectorSwing.seleccionarItem(
                padre,
                itemController,
                "Seleccione item para la OC:"
        );

        if (item == null) {
            return;
        }

        Integer cantidad = DialogoSwing.pedirEntero(padre, "Ingrese cantidad:");
        if (cantidad == null) {
            return;
        }

        if (cantidad <= 0) {
            DialogoSwing.mostrarError(padre, "La cantidad debe ser mayor a cero.");
            return;
        }

        Double precioAcordado = DialogoSwing.pedirDouble(padre, "Ingrese precio unitario acordado:");
        if (precioAcordado == null) {
            return;
        }

        if (precioAcordado <= 0) {
            DialogoSwing.mostrarError(padre, "El precio acordado debe ser mayor a cero.");
            return;
        }

        OrdenCompra ordenCompra = ordenCompraController.crearOrdenCompra(
                estadoVista.obtenerYAvanzarNumeroOC(),
                proveedor
        );

        ordenCompraController.agregarLinea(
                ordenCompra,
                item,
                cantidad,
                precioAcordado
        );

        ordenCompraController.confirmarOrdenCompra(ordenCompra);

        areaResultado.append("===== ORDEN DE COMPRA CREADA =====\n");
        areaResultado.append("Numero OC: " + ordenCompra.getNumero() + "\n");
        areaResultado.append("Proveedor: " + proveedor.getRazonSocial() + "\n");
        areaResultado.append("Item: " + item.getDescripcion() + "\n");
        areaResultado.append("Cantidad: " + cantidad + "\n");
        areaResultado.append("Precio acordado: " + precioAcordado + "\n");
        areaResultado.append("Total bruto: " + ordenCompra.calcularTotalBruto() + "\n");
        areaResultado.append("Limite deuda proveedor: " + proveedor.getLimiteDeudaAutorizado() + "\n");
        areaResultado.append("Estado OC: " + ordenCompra.getEstado() + "\n\n");
    }
}