package modelo;

import java.util.Date;

public class Producto extends Item {
    private String lote;
    private Date vencimiento;
    private int stock;

    public Producto(
            int codigo,
            String descripcion,
            Rubro rubro,
            String unidadMedida,
            double precioUnitario,
            double alicuotaIVA,
            String lote,
            Date vencimiento,
            int stock
    ) {
        super(codigo, descripcion, rubro, unidadMedida, precioUnitario, alicuotaIVA);
        this.lote = lote;
        this.vencimiento = vencimiento;
        this.stock = stock;
    }

    public boolean tieneStock(int cantidad) {
        return stock >= cantidad;
    }

    public void actualizarStock(int cantidad) {
        stock = stock + cantidad;
    }

    public int getStock() {
        return stock;
    }

    public String getLote() {
        return lote;
    }

    public Date getVencimiento() {
        return vencimiento;
    }
}