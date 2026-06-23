package modelo;

public abstract class MedioPago {
    private double importe;

    public MedioPago(double importe) {
        this.importe = importe;
    }

    public double getImporte() {
        return importe;
    }

    public abstract String getDescripcion();
}