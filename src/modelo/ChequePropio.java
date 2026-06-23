package modelo;

import java.util.Date;

public class ChequePropio extends MedioPago {
    private int nroCheque;
    private String banco;
    private Date fechaEmision;
    private Date fechaVencimiento;
    private String firmante;

    public ChequePropio(
            double importe,
            int nroCheque,
            String banco,
            Date fechaEmision,
            Date fechaVencimiento,
            String firmante
    ) {
        super(importe);
        this.nroCheque = nroCheque;
        this.banco = banco;
        this.fechaEmision = fechaEmision;
        this.fechaVencimiento = fechaVencimiento;
        this.firmante = firmante;
    }

    @Override
    public String getDescripcion() {
        return "Cheque propio N° " + nroCheque + " - Banco: " + banco;
    }
}