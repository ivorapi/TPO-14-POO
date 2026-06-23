package modelo;

import modelo.enums.TipoImpuesto;

public class Retencion {
    private TipoImpuesto tipoImpuesto;
    private double montoBase;
    private double porcentaje;
    private double minimoNoImponible;
    private double importeRetenido;

    public Retencion(TipoImpuesto tipoImpuesto, double montoBase, double porcentaje, double minimoNoImponible) {
        this.tipoImpuesto = tipoImpuesto;
        this.montoBase = montoBase;
        this.porcentaje = porcentaje;
        this.minimoNoImponible = minimoNoImponible;
        this.importeRetenido = 0;
    }

    public void calcular(Proveedor proveedor) {
        if (proveedor.tieneCertificadoVigente(tipoImpuesto)) {
            importeRetenido = 0;
            return;
        }

        if (montoBase <= minimoNoImponible) {
            importeRetenido = 0;
            return;
        }

        double montoImponible = montoBase - minimoNoImponible;
        importeRetenido = montoImponible * porcentaje / 100;
    }

    public TipoImpuesto getTipoImpuesto() {
        return tipoImpuesto;
    }

    public double getImporteRetenido() {
        return importeRetenido;
    }

    public double getPorcentaje() {
        return porcentaje;
    }
}