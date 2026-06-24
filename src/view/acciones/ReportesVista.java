package view.acciones;

import controller.*;
import modelo.Item;
import modelo.Proveedor;
import modelo.Rubro;
import modelo.enums.EstadoOrdenCompra;
import modelo.enums.TipoImpuesto;
import view.util.DialogoSwing;
import view.util.SelectorSwing;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class ReportesVista {
    private final Component padre;
    private final JTextArea areaResultado;
    private final ProveedorController proveedorController;
    private final RubroController rubroController;
    private final ItemController itemController;
    private final ConsultasController consultasController;

    public ReportesVista(
            Component padre,
            JTextArea areaResultado,
            ProveedorController proveedorController,
            RubroController rubroController,
            ItemController itemController,
            ConsultasController consultasController
    ) {
        this.padre = padre;
        this.areaResultado = areaResultado;
        this.proveedorController = proveedorController;
        this.rubroController = rubroController;
        this.itemController = itemController;
        this.consultasController = consultasController;
    }

    public void ejecutar() {
        if (proveedorController.getProveedores().isEmpty()) {
            DialogoSwing.mostrarError(padre, "Debe cargar al menos un proveedor para generar reportes.");
            return;
        }

        Date desde = new Date(System.currentTimeMillis() - (24L * 60 * 60 * 1000));
        Date hasta = new Date();

        Proveedor proveedor = SelectorSwing.seleccionarProveedor(
                padre,
                proveedorController,
                "Seleccione proveedor para reportes:"
        );

        if (proveedor == null) {
            return;
        }

        Item item = null;

        if (!itemController.getItems().isEmpty()) {
            item = SelectorSwing.seleccionarItem(
                    padre,
                    itemController,
                    "Seleccione item para comparacion de precios:"
            );
        }

        areaResultado.append("===== REPORTES OBLIGATORIOS =====\n\n");

        areaResultado.append("1. TRAZABILIDAD DOCUMENTAL\n");
        areaResultado.append("Total documentos recibidos hoy: "
                + consultasController.consultarTotalDocumentosPorDia(new Date()) + "\n");
        areaResultado.append("Total documentos recibidos en periodo: "
                + consultasController.consultarTotalDocumentosPorPeriodo(desde, hasta) + "\n");
        areaResultado.append(consultasController.generarReporteDocumentosPorProveedor(proveedor));

        areaResultado.append("\n2. GESTION DE DEUDA / CUENTA CORRIENTE\n");
        areaResultado.append(consultasController.generarCuentaCorrienteProveedor(proveedor));
        areaResultado.append(consultasController.generarReporteDeudaDetalladaProveedor(proveedor));

        areaResultado.append("Documentos pendientes con antiguedad mayor o igual a 0 dias: "
                + consultasController.consultarDocumentosPendientesPorAntiguedad(0).size() + "\n");

        areaResultado.append("\n3. SEGUIMIENTO DE COMPRAS Y PAGOS\n");
        areaResultado.append("OC emitidas: "
                + consultasController.consultarOrdenesCompraPorEstado(EstadoOrdenCompra.EMITIDA).size() + "\n");

        if (!rubroController.getRubros().isEmpty()) {
            Rubro rubro = SelectorSwing.seleccionarRubro(
                    padre,
                    rubroController,
                    "Seleccione rubro para reporte de OC por rubro:"
            );

            if (rubro != null) {
                areaResultado.append("OC por rubro " + rubro.getNombre() + ": "
                        + consultasController.consultarOrdenesCompraPorRubro(rubro).size() + "\n");
            }
        }

        areaResultado.append("OC por proveedor: "
                + consultasController.consultarOrdenesCompraPorProveedor(proveedor).size() + "\n");
        areaResultado.append("Pagos por proveedor: "
                + consultasController.consultarPagosPorProveedor(proveedor).size() + "\n");

        areaResultado.append("Pagos por periodo: "
                + consultasController.consultarPagosPorPeriodo(desde, hasta).size() + "\n");
        areaResultado.append("Pagos por medio Efectivo: "
                + consultasController.consultarPagosPorMedioPago("Efectivo").size() + "\n");

        areaResultado.append("\n4. ANALISIS DE COSTOS Y PRECIOS\n");
        if (item != null) {
            areaResultado.append(consultasController.generarReporteComparacionPreciosItem(item));
        } else {
            areaResultado.append("No hay items cargados para comparar precios.\n");
        }

        areaResultado.append("\n5. REPORTES FISCALES\n");
        areaResultado.append("Total retenido IVA en periodo: "
                + consultasController.consultarTotalRetenidoPorImpuestoYPeriodo(TipoImpuesto.IVA, desde, hasta) + "\n");
        areaResultado.append("Total retenido Ganancias en periodo: "
                + consultasController.consultarTotalRetenidoPorImpuestoYPeriodo(TipoImpuesto.GANANCIAS, desde, hasta) + "\n");
        areaResultado.append("Total retenido IIBB en periodo: "
                + consultasController.consultarTotalRetenidoPorImpuestoYPeriodo(TipoImpuesto.IIBB, desde, hasta) + "\n");

        areaResultado.append("\nLibro IVA Compras:\n");
        areaResultado.append(consultasController.generarLibroIVACompras());

        areaResultado.append("\n");
    }
}