package modelo;

import modelo.enums.TipoImpuesto;

import java.util.ArrayList;
import java.util.Date;

public class OrdenPago {
    private int numero;
    private Proveedor proveedor;
    private Date fechaEmision;

    private ArrayList<DetallePago> detallesPago;
    private ArrayList<Retencion> retenciones;
    private ArrayList<MedioPago> mediosPago;

    public OrdenPago(int numero, Proveedor proveedor, Date fechaEmision) {
        this.numero = numero;
        this.proveedor = proveedor;
        this.fechaEmision = fechaEmision;
        this.detallesPago = new ArrayList<>();
        this.retenciones = new ArrayList<>();
        this.mediosPago = new ArrayList<>();
    }

    public void agregarDetallePago(DocumentoComercial documento, double montoAplicado) {
        if (documento.getEstado() == modelo.enums.EstadoDocumentoComercial.CANCELADO) {
            throw new IllegalArgumentException("No se puede pagar un documento cancelado");
        }

        if (montoAplicado <= 0) {
            throw new IllegalArgumentException("El monto aplicado debe ser mayor a cero");
        }

        double saldoPendiente = documento.getImporteTotal() - documento.getMontoPagado();

        if (montoAplicado > saldoPendiente) {
            throw new IllegalArgumentException("El monto aplicado no puede superar el saldo pendiente del documento");
        }

        DetallePago detallePago = new DetallePago(documento, montoAplicado);
        detallesPago.add(detallePago);
    }

    public void agregarMedioPago(MedioPago medioPago) {
        mediosPago.add(medioPago);
    }

    public double calcularTotalBrutoPagado() {
        double total = 0;

        for (DetallePago detalle : detallesPago) {
            total = total + detalle.getMontoAplicado();
        }

        return total;
    }

    public void calcularRetenciones() {
        retenciones.clear();

        double montoBase = calcularTotalBrutoPagado();

        Retencion retencionIVA = new Retencion(TipoImpuesto.IVA, montoBase, 3, 0);
        retencionIVA.calcular(proveedor);
        retenciones.add(retencionIVA);

        Retencion retencionGanancias = new Retencion(TipoImpuesto.GANANCIAS, montoBase, 2, 10000);
        retencionGanancias.calcular(proveedor);
        retenciones.add(retencionGanancias);

        Retencion retencionIIBB = new Retencion(TipoImpuesto.IIBB, montoBase, 1.5, 10000);
        retencionIIBB.calcular(proveedor);
        retenciones.add(retencionIIBB);
    }

    public double calcularTotalRetenido() {
        double total = 0;

        for (Retencion retencion : retenciones) {
            total = total + retencion.getImporteRetenido();
        }

        return total;
    }

    public double calcularTotalNetoAPagar() {
        return calcularTotalBrutoPagado() - calcularTotalRetenido();
    }

    public double calcularTotalMediosPago() {
        double total = 0;

        for (MedioPago medioPago : mediosPago) {
            total = total + medioPago.getImporte();
        }

        return total;
    }

    public boolean emitir() {
        calcularRetenciones();

        if (calcularTotalMediosPago() < calcularTotalNetoAPagar()) {
            return false;
        }

        for (DetallePago detalle : detallesPago) {
            detalle.aplicarPago();
        }

        generarAsientosContablesRetenciones();

        return true;
    }

    public void generarAsientosContablesRetenciones() {
        for (Retencion retencion : retenciones) {
            if (retencion.getImporteRetenido() > 0) {
                System.out.println(
                        "Asiento contable generado - " +
                                retencion.getTipoImpuesto() +
                                ": " +
                                retencion.getImporteRetenido()
                );
            }
        }
    }

    public int getNumero() {
        return numero;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public ArrayList<DetallePago> getDetallesPago() {
        return detallesPago;
    }

    public ArrayList<Retencion> getRetenciones() {
        return retenciones;
    }

    public ArrayList<MedioPago> getMediosPago() {
        return mediosPago;
    }
}