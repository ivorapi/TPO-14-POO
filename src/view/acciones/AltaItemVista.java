package view.acciones;

import controller.ItemController;
import controller.RubroController;
import modelo.Item;
import modelo.Producto;
import modelo.Rubro;
import modelo.Servicio;
import view.util.DialogoSwing;
import view.util.SelectorSwing;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class AltaItemVista {
    private final Component padre;
    private final JTextArea areaResultado;
    private final ItemController itemController;
    private final RubroController rubroController;

    public AltaItemVista(
            Component padre,
            JTextArea areaResultado,
            ItemController itemController,
            RubroController rubroController
    ) {
        this.padre = padre;
        this.areaResultado = areaResultado;
        this.itemController = itemController;
        this.rubroController = rubroController;
    }

    public void ejecutar() {
        if (rubroController.getRubros().isEmpty()) {
            DialogoSwing.mostrarError(padre, "Primero debe cargar al menos un rubro.");
            return;
        }

        String tipoItem = SelectorSwing.seleccionarTipoItem(padre);
        if (tipoItem == null) {
            return;
        }

        Integer codigo = DialogoSwing.pedirEntero(padre, "Ingrese codigo del item:");
        if (codigo == null) {
            return;
        }

        if (codigo <= 0) {
            DialogoSwing.mostrarError(padre, "El codigo debe ser mayor a cero.");
            return;
        }

        if (itemController.buscarItemPorCodigo(codigo) != null) {
            DialogoSwing.mostrarError(padre, "Ya existe un item con ese codigo.");
            return;
        }

        String descripcion = DialogoSwing.pedirTextoObligatorio(padre, "Ingrese descripcion del item:");
        if (descripcion == null) {
            return;
        }

        Rubro rubro = SelectorSwing.seleccionarRubro(padre, rubroController, "Seleccione rubro del item:");
        if (rubro == null) {
            return;
        }

        String unidadMedida = DialogoSwing.pedirTextoObligatorio(padre, "Ingrese unidad de medida:");
        if (unidadMedida == null) {
            return;
        }

        Double precioUnitario = DialogoSwing.pedirDouble(padre, "Ingrese precio unitario:");
        if (precioUnitario == null) {
            return;
        }

        if (precioUnitario <= 0) {
            DialogoSwing.mostrarError(padre, "El precio unitario debe ser mayor a cero.");
            return;
        }

        Double alicuotaIVA = DialogoSwing.pedirDouble(padre, "Ingrese alicuota IVA:");
        if (alicuotaIVA == null) {
            return;
        }

        if (alicuotaIVA < 0) {
            DialogoSwing.mostrarError(padre, "La alicuota de IVA no puede ser negativa.");
            return;
        }

        if (tipoItem.equals("Producto")) {
            crearProducto(codigo, descripcion, rubro, unidadMedida, precioUnitario, alicuotaIVA);
        } else {
            crearServicio(codigo, descripcion, rubro, unidadMedida, precioUnitario, alicuotaIVA);
        }
    }

    private void crearProducto(
            int codigo,
            String descripcion,
            Rubro rubro,
            String unidadMedida,
            double precioUnitario,
            double alicuotaIVA
    ) {
        String lote = DialogoSwing.pedirTextoObligatorio(padre, "Ingrese lote:");
        if (lote == null) {
            return;
        }

        Integer stock = DialogoSwing.pedirEntero(padre, "Ingrese stock:");
        if (stock == null) {
            return;
        }

        if (stock < 0) {
            DialogoSwing.mostrarError(padre, "El stock no puede ser negativo.");
            return;
        }

        Producto producto = new Producto(
                codigo,
                descripcion,
                rubro,
                unidadMedida,
                precioUnitario,
                alicuotaIVA,
                lote,
                new Date(),
                stock
        );

        itemController.agregarItem(producto);
        imprimirItem(producto, "PRODUCTO CARGADO");
        areaResultado.append("Stock: " + producto.getStock() + "\n\n");
    }

    private void crearServicio(
            int codigo,
            String descripcion,
            Rubro rubro,
            String unidadMedida,
            double precioUnitario,
            double alicuotaIVA
    ) {
        Integer duracion = DialogoSwing.pedirEntero(padre, "Ingrese duracion estimada del servicio:");
        if (duracion == null) {
            return;
        }

        if (duracion <= 0) {
            DialogoSwing.mostrarError(padre, "La duracion estimada debe ser mayor a cero.");
            return;
        }

        Servicio servicio = new Servicio(
                codigo,
                descripcion,
                rubro,
                unidadMedida,
                precioUnitario,
                alicuotaIVA,
                duracion
        );

        itemController.agregarItem(servicio);
        imprimirItem(servicio, "SERVICIO CARGADO");
        areaResultado.append("Duracion estimada: " + servicio.getDuracionEstimada() + "\n\n");
    }

    private void imprimirItem(Item item, String titulo) {
        areaResultado.append("===== " + titulo + " =====\n");
        areaResultado.append("Codigo: " + item.getCodigo() + "\n");
        areaResultado.append("Descripcion: " + item.getDescripcion() + "\n");
        areaResultado.append("Rubro: " + item.getRubro().getNombre() + "\n");
        areaResultado.append("Precio unitario: " + item.getPrecioUnitario() + "\n");
        areaResultado.append("IVA: " + item.getAlicuotaIVA() + "\n");
    }
}