package modelo;

public class TransferenciaBancaria extends MedioPago {
    private int nroReferencia;
    private String cuentaOrigen;

    public TransferenciaBancaria(double importe, int nroReferencia, String cuentaOrigen) {
        super(importe);
        this.nroReferencia = nroReferencia;
        this.cuentaOrigen = cuentaOrigen;
    }

    @Override
    public String getDescripcion() {
        return "Transferencia bancaria - Ref: " + nroReferencia;
    }

    public int getNroReferencia() {
        return nroReferencia;
    }

    public String getCuentaOrigen() {
        return cuentaOrigen;
    }
}