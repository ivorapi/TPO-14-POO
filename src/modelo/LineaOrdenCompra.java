package modelo;

public class LineaOrdenCompra {
    private Item item;
    private int cantidad;
    private double precioUnitarioAcordado;

    public LineaOrdenCompra(Item item, int cantidad, double precioUnitarioAcordado) {
        this.item = item;
        this.cantidad = cantidad;
        this.precioUnitarioAcordado = precioUnitarioAcordado;
    }

    public double calcularSubtotal() {
        return cantidad * precioUnitarioAcordado;
    }

    public Item getItem() {
        return item;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getPrecioUnitarioAcordado() {
        return precioUnitarioAcordado;
    }
}