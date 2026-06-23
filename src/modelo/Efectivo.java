package modelo;

public class Efectivo extends MedioPago {

    public Efectivo(double importe) {
        super(importe);
    }

    @Override
    public String getDescripcion() {
        return "Efectivo";
    }
}