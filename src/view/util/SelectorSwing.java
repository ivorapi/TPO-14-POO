package view.util;

import controller.*;
import modelo.*;
import modelo.enums.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class SelectorSwing {

    public static CondicionImpositiva seleccionarCondicionImpositiva(Component padre) {
        return (CondicionImpositiva) JOptionPane.showInputDialog(
                padre,
                "Seleccione condicion impositiva:",
                "Condicion impositiva",
                JOptionPane.QUESTION_MESSAGE,
                null,
                CondicionImpositiva.values(),
                CondicionImpositiva.RESPONSABLE_INSCRIPTO
        );
    }

    public static String seleccionarTipoItem(Component padre) {
        String[] opciones = {"Producto", "Servicio"};

        return (String) JOptionPane.showInputDialog(
                padre,
                "Seleccione tipo de item:",
                "Tipo item",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );
    }

    public static Rubro seleccionarRubro(Component padre, RubroController rubroController, String mensaje) {
        ArrayList<Rubro> rubros = rubroController.getRubros();

        if (rubros.isEmpty()) {
            DialogoSwing.mostrarError(padre, "No hay rubros cargados.");
            return null;
        }

        String[] opciones = new String[rubros.size()];

        for (int i = 0; i < rubros.size(); i++) {
            opciones[i] = rubros.get(i).getNombre();
        }

        String seleccionado = (String) JOptionPane.showInputDialog(
                padre,
                mensaje,
                "Seleccionar rubro",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        if (seleccionado == null) {
            return null;
        }

        for (int i = 0; i < opciones.length; i++) {
            if (opciones[i].equals(seleccionado)) {
                return rubros.get(i);
            }
        }

        return null;
    }

    public static Proveedor seleccionarProveedor(Component padre, ProveedorController proveedorController, String mensaje) {
        ArrayList<Proveedor> proveedores = proveedorController.getProveedores();

        if (proveedores.isEmpty()) {
            DialogoSwing.mostrarError(padre, "No hay proveedores cargados.");
            return null;
        }

        String[] opciones = new String[proveedores.size()];

        for (int i = 0; i < proveedores.size(); i++) {
            opciones[i] = proveedores.get(i).getCuit()
                    + " - " + proveedores.get(i).getRazonSocial();
        }

        String seleccionado = (String) JOptionPane.showInputDialog(
                padre,
                mensaje,
                "Seleccionar proveedor",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        if (seleccionado == null) {
            return null;
        }

        for (int i = 0; i < opciones.length; i++) {
            if (opciones[i].equals(seleccionado)) {
                return proveedores.get(i);
            }
        }

        return null;
    }

    public static Item seleccionarItem(Component padre, ItemController itemController, String mensaje) {
        ArrayList<Item> items = itemController.getItems();

        if (items.isEmpty()) {
            DialogoSwing.mostrarError(padre, "No hay items cargados.");
            return null;
        }

        String[] opciones = new String[items.size()];

        for (int i = 0; i < items.size(); i++) {
            opciones[i] = items.get(i).getCodigo()
                    + " - " + items.get(i).getDescripcion();
        }

        String seleccionado = (String) JOptionPane.showInputDialog(
                padre,
                mensaje,
                "Seleccionar item",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        if (seleccionado == null) {
            return null;
        }

        for (int i = 0; i < opciones.length; i++) {
            if (opciones[i].equals(seleccionado)) {
                return items.get(i);
            }
        }

        return null;
    }

    public static OrdenCompra seleccionarOrdenCompra(Component padre, OrdenCompraController ordenCompraController, String mensaje) {
        ArrayList<OrdenCompra> ordenes = ordenCompraController.getOrdenesCompra();

        if (ordenes.isEmpty()) {
            DialogoSwing.mostrarError(padre, "No hay ordenes de compra cargadas.");
            return null;
        }

        String[] opciones = new String[ordenes.size()];

        for (int i = 0; i < ordenes.size(); i++) {
            opciones[i] = "OC " + ordenes.get(i).getNumero()
                    + " - " + ordenes.get(i).getProveedor().getRazonSocial()
                    + " - Estado: " + ordenes.get(i).getEstado();
        }

        String seleccionado = (String) JOptionPane.showInputDialog(
                padre,
                mensaje,
                "Seleccionar Orden de Compra",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        if (seleccionado == null) {
            return null;
        }

        for (int i = 0; i < opciones.length; i++) {
            if (opciones[i].equals(seleccionado)) {
                return ordenes.get(i);
            }
        }

        return null;
    }

    public static DocumentoComercial seleccionarDocumentoPendienteParaPago(
            Component padre,
            DocumentoComercialController documentoController
    ) {
        ArrayList<DocumentoComercial> documentos = documentoController.getDocumentosComerciales();
        ArrayList<DocumentoComercial> documentosPagables = new ArrayList<>();

        for (DocumentoComercial documento : documentos) {
            boolean estadoPagable = documento.getEstado() == EstadoDocumentoComercial.PENDIENTE
                    || documento.getEstado() == EstadoDocumentoComercial.PARCIALMENTE_CANCELADO;

            double saldo = documento.getImporteTotal() - documento.getMontoPagado();

            if (estadoPagable && saldo > 0) {
                documentosPagables.add(documento);
            }
        }

        if (documentosPagables.isEmpty()) {
            DialogoSwing.mostrarError(padre, "No hay documentos pendientes para pagar.");
            return null;
        }

        String[] opciones = new String[documentosPagables.size()];

        for (int i = 0; i < documentosPagables.size(); i++) {
            DocumentoComercial documento = documentosPagables.get(i);

            opciones[i] = documento.getClass().getSimpleName()
                    + " Nro " + documento.getNroDocumento()
                    + " - " + documento.getProveedor().getRazonSocial()
                    + " - Saldo: " + (documento.getImporteTotal() - documento.getMontoPagado());
        }

        String seleccionado = (String) JOptionPane.showInputDialog(
                padre,
                "Seleccione documento a pagar:",
                "Documento a pagar",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        if (seleccionado == null) {
            return null;
        }

        for (int i = 0; i < opciones.length; i++) {
            if (opciones[i].equals(seleccionado)) {
                return documentosPagables.get(i);
            }
        }

        return null;
    }
}