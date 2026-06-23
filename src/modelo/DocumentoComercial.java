package modelo;

import modelo.enums.EstadoDocumentoComercial;

import java.util.ArrayList;
import java.util.Date;

public abstract class DocumentoComercial {
    private Proveedor proveedor;
    private Date fechaEmision;
    private int nroDocumento;
    private double importeTotal;
    private ArrayList<LineaOrdenCompra> detalleItems;
    private EstadoDocumentoComercial estado;
    private double montoPagado;

    public DocumentoComercial(Proveedor proveedor, Date fechaEmision, int nroDocumento, double importeTotal) {
        this.proveedor = proveedor;
        this.fechaEmision = fechaEmision;
        this.nroDocumento = nroDocumento;
        this.importeTotal = importeTotal;
        this.detalleItems = new ArrayList<>();
        this.estado = EstadoDocumentoComercial.PENDIENTE;
        this.montoPagado = 0;
    }

    public void agregarLinea(Item item, int cantidad, double precioUnitario) {
        LineaOrdenCompra linea = new LineaOrdenCompra(item, cantidad, precioUnitario);
        detalleItems.add(linea);
        recalcularImporteTotal();
    }

    public void recalcularImporteTotal() {
        double total = 0;

        for (LineaOrdenCompra linea : detalleItems) {
            total = total + linea.calcularSubtotal();
        }

        if (!detalleItems.isEmpty()) {
            importeTotal = total;
        }
    }

    public void cambiarEstado(EstadoDocumentoComercial nuevoEstado) {
        this.estado = nuevoEstado;
    }

    public void actualizarEstadoCancelacion(double montoAplicado) {
        montoPagado = montoPagado + montoAplicado;

        if (montoPagado <= 0) {
            estado = EstadoDocumentoComercial.PENDIENTE;
        } else if (montoPagado >= importeTotal) {
            estado = EstadoDocumentoComercial.CANCELADO;
        } else {
            estado = EstadoDocumentoComercial.PARCIALMENTE_CANCELADO;
        }
    }

    public abstract double impactoEnCuentaCorriente();

    public Proveedor getProveedor() {
        return proveedor;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public int getNroDocumento() {
        return nroDocumento;
    }

    public double getImporteTotal() {
        return importeTotal;
    }

    protected void setImporteTotal(double importeTotal) {
        this.importeTotal = importeTotal;
    }

    public ArrayList<LineaOrdenCompra> getDetalleItems() {
        return detalleItems;
    }

    public EstadoDocumentoComercial getEstado() {
        return estado;
    }

    public double getMontoPagado() {
        return montoPagado;
    }
}