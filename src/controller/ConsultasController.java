package controller;

import modelo.*;
import modelo.enums.EstadoDocumentoComercial;
import modelo.enums.EstadoOrdenCompra;
import modelo.enums.TipoImpuesto;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ConsultasController {
    private OrdenCompraController ordenCompraController;
    private DocumentoComercialController documentoController;
    private OrdenPagoController ordenPagoController;

    public ConsultasController(
            OrdenCompraController ordenCompraController,
            DocumentoComercialController documentoController,
            OrdenPagoController ordenPagoController
    ) {
        this.ordenCompraController = ordenCompraController;
        this.documentoController = documentoController;
        this.ordenPagoController = ordenPagoController;
    }

    public double consultarDeudaActualProveedor(Proveedor proveedor) {
        return documentoController.calcularDeudaActualProveedor(proveedor);
    }

    public ArrayList<DocumentoComercial> consultarDocumentosPendientes() {
        return documentoController.obtenerDocumentosPendientes();
    }

    public ArrayList<DocumentoComercial> consultarDocumentosPorProveedor(Proveedor proveedor) {
        ArrayList<DocumentoComercial> resultado = new ArrayList<>();

        for (DocumentoComercial documento : documentoController.getDocumentosComerciales()) {
            if (documento.getProveedor().getCuit() == proveedor.getCuit()) {
                resultado.add(documento);
            }
        }

        return resultado;
    }

    public ArrayList<OrdenCompra> consultarOrdenesCompraPorProveedor(Proveedor proveedor) {
        return ordenCompraController.consultarOrdenesCompraPorProveedor(proveedor);
    }

    public ArrayList<OrdenCompra> consultarOrdenesCompraPorEstado(EstadoOrdenCompra estado) {
        return ordenCompraController.consultarOrdenesCompraPorEstado(estado);
    }

    public ArrayList<OrdenCompra> consultarOrdenesCompraPorRubro(Rubro rubro) {
        return ordenCompraController.consultarOrdenesCompraPorRubro(rubro);
    }

    public ArrayList<OrdenPago> consultarPagosPorProveedor(Proveedor proveedor) {
        return ordenPagoController.consultarPagosPorProveedor(proveedor);
    }

    public ArrayList<OrdenPago> consultarPagosPorPeriodo(Date desde, Date hasta) {
        ArrayList<OrdenPago> resultado = new ArrayList<>();

        for (OrdenPago ordenPago : ordenPagoController.getOrdenesPago()) {
            if (fechaEntre(ordenPago.getFechaEmision(), desde, hasta)) {
                resultado.add(ordenPago);
            }
        }

        return resultado;
    }

    public ArrayList<OrdenPago> consultarPagosPorMedioPago(String medioBuscado) {
        ArrayList<OrdenPago> resultado = new ArrayList<>();

        for (OrdenPago ordenPago : ordenPagoController.getOrdenesPago()) {
            for (MedioPago medioPago : ordenPago.getMediosPago()) {
                if (medioPago.getDescripcion().toLowerCase().contains(medioBuscado.toLowerCase())) {
                    resultado.add(ordenPago);
                    break;
                }
            }
        }

        return resultado;
    }

    public double consultarTotalRetenidoPorImpuesto(TipoImpuesto tipoImpuesto) {
        double total = 0;

        for (OrdenPago ordenPago : ordenPagoController.getOrdenesPago()) {
            for (Retencion retencion : ordenPago.getRetenciones()) {
                if (retencion.getTipoImpuesto() == tipoImpuesto) {
                    total = total + retencion.getImporteRetenido();
                }
            }
        }

        return total;
    }

    public double consultarTotalRetenidoPorImpuestoYPeriodo(TipoImpuesto tipoImpuesto, Date desde, Date hasta) {
        double total = 0;

        for (OrdenPago ordenPago : ordenPagoController.getOrdenesPago()) {
            if (fechaEntre(ordenPago.getFechaEmision(), desde, hasta)) {
                for (Retencion retencion : ordenPago.getRetenciones()) {
                    if (retencion.getTipoImpuesto() == tipoImpuesto) {
                        total = total + retencion.getImporteRetenido();
                    }
                }
            }
        }

        return total;
    }

    public int consultarCantidadDocumentosPendientes() {
        int cantidad = 0;

        for (DocumentoComercial documento : documentoController.getDocumentosComerciales()) {
            if (documento.getEstado() == EstadoDocumentoComercial.PENDIENTE ||
                    documento.getEstado() == EstadoDocumentoComercial.PARCIALMENTE_CANCELADO) {
                cantidad++;
            }
        }

        return cantidad;
    }

    public int consultarTotalDocumentosPorDia(Date fecha) {
        int cantidad = 0;

        for (DocumentoComercial documento : documentoController.getDocumentosComerciales()) {
            if (mismaFecha(documento.getFechaEmision(), fecha)) {
                cantidad++;
            }
        }

        return cantidad;
    }

    public int consultarTotalDocumentosPorPeriodo(Date desde, Date hasta) {
        int cantidad = 0;

        for (DocumentoComercial documento : documentoController.getDocumentosComerciales()) {
            if (fechaEntre(documento.getFechaEmision(), desde, hasta)) {
                cantidad++;
            }
        }

        return cantidad;
    }

    public ArrayList<DocumentoComercial> consultarDocumentosPendientesPorAntiguedad(int diasMinimos) {
        ArrayList<DocumentoComercial> resultado = new ArrayList<>();
        Date hoy = new Date();

        for (DocumentoComercial documento : documentoController.getDocumentosComerciales()) {
            if (documento.getEstado() == EstadoDocumentoComercial.PENDIENTE ||
                    documento.getEstado() == EstadoDocumentoComercial.PARCIALMENTE_CANCELADO) {

                long diferencia = hoy.getTime() - documento.getFechaEmision().getTime();
                long dias = diferencia / (1000 * 60 * 60 * 24);

                if (dias >= diasMinimos) {
                    resultado.add(documento);
                }
            }
        }

        return resultado;
    }

    public String generarReporteDocumentosPorProveedor(Proveedor proveedor) {
        String reporte = "";
        double total = 0;
        int cantidad = 0;

        reporte = reporte + "Detalle de documentos del proveedor: " + proveedor.getRazonSocial() + "\n";

        for (DocumentoComercial documento : documentoController.getDocumentosComerciales()) {
            if (documento.getProveedor().getCuit() == proveedor.getCuit()) {
                cantidad++;
                total = total + documento.impactoEnCuentaCorriente();

                reporte = reporte + documento.getClass().getSimpleName()
                        + " Nro: " + documento.getNroDocumento()
                        + " | Fecha: " + formatearFecha(documento.getFechaEmision())
                        + " | Importe: " + documento.getImporteTotal()
                        + " | Estado: " + documento.getEstado()
                        + "\n";
            }
        }

        reporte = reporte + "Cantidad de documentos: " + cantidad + "\n";
        reporte = reporte + "Total documentos: " + total + "\n";

        return reporte;
    }

    public String generarCuentaCorrienteProveedor(Proveedor proveedor) {
        String reporte = "";
        double saldo = 0;

        reporte = reporte + "Cuenta corriente de proveedor: " + proveedor.getRazonSocial() + "\n";

        for (DocumentoComercial documento : documentoController.getDocumentosComerciales()) {
            if (documento.getProveedor().getCuit() == proveedor.getCuit()) {
                saldo = saldo + documento.impactoEnCuentaCorriente();

                reporte = reporte + formatearFecha(documento.getFechaEmision())
                        + " | " + documento.getClass().getSimpleName()
                        + " Nro " + documento.getNroDocumento()
                        + " | Movimiento: " + documento.impactoEnCuentaCorriente()
                        + " | Saldo: " + saldo
                        + "\n";
            }
        }

        for (OrdenPago ordenPago : ordenPagoController.getOrdenesPago()) {
            if (ordenPago.getProveedor().getCuit() == proveedor.getCuit()) {
                for (DetallePago detallePago : ordenPago.getDetallesPago()) {
                    saldo = saldo - detallePago.getMontoAplicado();

                    reporte = reporte + formatearFecha(ordenPago.getFechaEmision())
                            + " | Orden de Pago Nro " + ordenPago.getNumero()
                            + " | Movimiento: -" + detallePago.getMontoAplicado()
                            + " | Saldo: " + saldo
                            + "\n";
                }
            }
        }

        return reporte;
    }

    public String generarReporteDeudaDetalladaProveedor(Proveedor proveedor) {
        String reporte = "";
        double total = 0;

        reporte = reporte + "Deuda actual detallada del proveedor: " + proveedor.getRazonSocial() + "\n";

        for (DocumentoComercial documento : documentoController.getDocumentosComerciales()) {
            if (documento.getProveedor().getCuit() == proveedor.getCuit()) {
                if (documento.getEstado() == EstadoDocumentoComercial.PENDIENTE ||
                        documento.getEstado() == EstadoDocumentoComercial.PARCIALMENTE_CANCELADO) {

                    double saldoDocumento = documento.getImporteTotal() - documento.getMontoPagado();
                    total = total + saldoDocumento;

                    reporte = reporte + documento.getClass().getSimpleName()
                            + " Nro " + documento.getNroDocumento()
                            + " | Importe: " + documento.getImporteTotal()
                            + " | Pagado: " + documento.getMontoPagado()
                            + " | Saldo pendiente: " + saldoDocumento
                            + "\n";
                }
            }
        }

        reporte = reporte + "Total deuda actual: " + total + "\n";
        return reporte;
    }

    public String generarReporteComparacionPreciosItem(Item item) {
        String reporte = "";

        reporte = reporte + "Comparacion de precios para item: " + item.getDescripcion() + "\n";

        for (OrdenCompra ordenCompra : ordenCompraController.getOrdenesCompra()) {
            for (LineaOrdenCompra linea : ordenCompra.getLineas()) {
                if (linea.getItem().getCodigo() == item.getCodigo()) {
                    reporte = reporte + "Proveedor: " + ordenCompra.getProveedor().getRazonSocial()
                            + " | OC: " + ordenCompra.getNumero()
                            + " | Precio acordado: " + linea.getPrecioUnitarioAcordado()
                            + "\n";
                }
            }
        }

        return reporte;
    }

    public String generarLibroIVACompras() {
        String reporte = "";

        reporte = reporte + "CUIT | Proveedor | Fecha | Tipo Documento | Nro | IVA | Total\n";

        for (DocumentoComercial documento : documentoController.getDocumentosComerciales()) {
            double ivaTotal = 0;

            for (LineaOrdenCompra linea : documento.getDetalleItems()) {
                double subtotal = linea.calcularSubtotal();
                double ivaLinea = subtotal * linea.getItem().getAlicuotaIVA() / 100;
                ivaTotal = ivaTotal + ivaLinea;
            }

            reporte = reporte + documento.getProveedor().getCuit()
                    + " | " + documento.getProveedor().getRazonSocial()
                    + " | " + formatearFecha(documento.getFechaEmision())
                    + " | " + documento.getClass().getSimpleName()
                    + " | " + documento.getNroDocumento()
                    + " | IVA: " + ivaTotal
                    + " | Total: " + documento.getImporteTotal()
                    + "\n";
        }

        return reporte;
    }

    private boolean mismaFecha(Date fecha1, Date fecha2) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        c1.setTime(fecha1);
        c2.setTime(fecha2);

        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                && c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR);
    }

    private boolean fechaEntre(Date fecha, Date desde, Date hasta) {
        return !fecha.before(desde) && !fecha.after(hasta);
    }

    private String formatearFecha(Date fecha) {
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        return formato.format(fecha);
    }
}