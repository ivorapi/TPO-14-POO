package modelo;

import java.util.Date;

public class ChequeTercero extends MedioPago {
    private int nroCheque;
    private String banco;
    private Date fechaEmision;
    private Date fechaVencimiento;
    private String firmanteOriginal;

    public ChequeTercero(
            double importe,
            int nroCheque,
            String banco,
            Date fechaEmision,
            Date fechaVencimiento,
            String firmanteOriginal
    ) {
        super(importe);
        this.nroCheque = nroCheque;
        this.banco = banco;
        this.fechaEmision = fechaEmision;
        this.fechaVencimiento = fechaVencimiento;
        this.firmanteOriginal = firmanteOriginal;
    }

    @Override
    public String getDescripcion() {
        return "Cheque de tercero N° " + nroCheque + " - Banco: " + banco;
    }
}