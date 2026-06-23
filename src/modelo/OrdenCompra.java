package modelo;

import modelo.enums.EstadoOrdenCompra;

import java.util.ArrayList;
import java.util.Date;

public class OrdenCompra {
    private int numero;
    private Date fecha;
    private Proveedor proveedor;
    private ArrayList<LineaOrdenCompra> lineas;
    private EstadoOrdenCompra estado;

    public OrdenCompra(int numero, Date fecha, Proveedor proveedor) {
        this.numero = numero;
        this.fecha = fecha;
        this.proveedor = proveedor;
        this.lineas = new ArrayList<>();
        this.estado = EstadoOrdenCompra.BORRADOR;
    }

    public void agregarLinea(Item item, int cantidad, double precioUnitarioAcordado) {
        LineaOrdenCompra linea = new LineaOrdenCompra(item, cantidad, precioUnitarioAcordado);
        lineas.add(linea);
    }

    public double calcularTotalBruto() {
        double total = 0;

        for (LineaOrdenCompra linea : lineas) {
            total = total + linea.calcularSubtotal();
        }

        return total;
    }

    public void confirmar(double deudaActualProveedor) {
        double montoComprometido = deudaActualProveedor + calcularTotalBruto();

        if (montoComprometido <= proveedor.getLimiteDeudaAutorizado()) {
            estado = EstadoOrdenCompra.EMITIDA;
        } else {
            estado = EstadoOrdenCompra.PENDIENTE_APROBACION;
        }
    }

    public void aprobarPorSupervisor(Usuario usuario) {
        if (usuario.isSupervisor()) {
            estado = EstadoOrdenCompra.EMITIDA;
        }
    }

    public int getNumero() {
        return numero;
    }

    public Date getFecha() {
        return fecha;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public ArrayList<LineaOrdenCompra> getLineas() {
        return lineas;
    }

    public EstadoOrdenCompra getEstado() {
        return estado;
    }
}