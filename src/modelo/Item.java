package modelo;

public abstract class Item {
    private int codigo;
    private String descripcion;
    private Rubro rubro;
    private String unidadMedida;
    private double precioUnitario;
    private double alicuotaIVA;

    public Item(
            int codigo,
            String descripcion,
            Rubro rubro,
            String unidadMedida,
            double precioUnitario,
            double alicuotaIVA
    ) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.rubro = rubro;
        this.unidadMedida = unidadMedida;
        this.precioUnitario = precioUnitario;
        this.alicuotaIVA = alicuotaIVA;
    }

    public double calcularSubtotal(int cantidad) {
        return precioUnitario * cantidad;
    }

    public double getPrecioConIVA() {
        return precioUnitario + (precioUnitario * alicuotaIVA / 100);
    }

    public int getCodigo() {
        return codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Rubro getRubro() {
        return rubro;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public double getAlicuotaIVA() {
        return alicuotaIVA;
    }
}