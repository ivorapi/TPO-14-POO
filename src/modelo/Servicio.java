package modelo;

public class Servicio extends Item {
    private int duracionEstimada;

    public Servicio(
            int codigo,
            String descripcion,
            Rubro rubro,
            String unidadMedida,
            double precioUnitario,
            double alicuotaIVA,
            int duracionEstimada
    ) {
        super(codigo, descripcion, rubro, unidadMedida, precioUnitario, alicuotaIVA);
        this.duracionEstimada = duracionEstimada;
    }

    public int getDuracionEstimada() {
        return duracionEstimada;
    }
}