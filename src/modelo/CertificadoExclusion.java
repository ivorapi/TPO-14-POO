package modelo;

import modelo.enums.TipoImpuesto;
import java.util.Date;

public class CertificadoExclusion {
    private TipoImpuesto tipoImpuesto;
    private Date fechaDesde;
    private Date fechaHasta;
    private int nroCertificado;

    public CertificadoExclusion(TipoImpuesto tipoImpuesto, Date fechaDesde, Date fechaHasta, int nroCertificado) {
        this.tipoImpuesto = tipoImpuesto;
        this.fechaDesde = fechaDesde;
        this.fechaHasta = fechaHasta;
        this.nroCertificado = nroCertificado;
    }

    public boolean estaVigente(Date fechaActual) {
        return !fechaActual.before(fechaDesde) && !fechaActual.after(fechaHasta);
    }

    public TipoImpuesto getTipoImpuesto() {
        return tipoImpuesto;
    }

    public int getNroCertificado() {
        return nroCertificado;
    }
}