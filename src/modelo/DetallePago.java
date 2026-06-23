package modelo;

public class DetallePago {
    private DocumentoComercial documento;
    private double montoAplicado;

    public DetallePago(DocumentoComercial documento, double montoAplicado) {
        this.documento = documento;
        this.montoAplicado = montoAplicado;
    }

    public void aplicarPago() {
        documento.actualizarEstadoCancelacion(montoAplicado);
    }

    public DocumentoComercial getDocumento() {
        return documento;
    }

    public double getMontoAplicado() {
        return montoAplicado;
    }
}