package controller;

import modelo.*;
import modelo.enums.EstadoOrdenCompra;

import java.util.ArrayList;
import java.util.Date;

public class OrdenCompraController {
    private ArrayList<OrdenCompra> ordenesCompra;
    private DocumentoComercialController documentoController;

    public OrdenCompraController(DocumentoComercialController documentoController) {
        this.ordenesCompra = new ArrayList<>();
        this.documentoController = documentoController;
    }

    public OrdenCompra crearOrdenCompra(int numero, Proveedor proveedor) {
        OrdenCompra ordenCompra = new OrdenCompra(numero, new Date(), proveedor);
        ordenesCompra.add(ordenCompra);
        return ordenCompra;
    }

    public void agregarLinea(OrdenCompra ordenCompra, Item item, int cantidad, double precioUnitarioAcordado) {
        ordenCompra.agregarLinea(item, cantidad, precioUnitarioAcordado);
    }

    public void confirmarOrdenCompra(OrdenCompra ordenCompra) {
        double deudaActual = documentoController.calcularDeudaActualProveedor(
                ordenCompra.getProveedor()
        );

        ordenCompra.confirmar(deudaActual);
    }

    public void aprobarOrdenCompra(OrdenCompra ordenCompra, Usuario usuario) {
        ordenCompra.aprobarPorSupervisor(usuario);
    }

    public OrdenCompra buscarOrdenCompraPorNumero(int numero) {
        for (OrdenCompra ordenCompra : ordenesCompra) {
            if (ordenCompra.getNumero() == numero) {
                return ordenCompra;
            }
        }

        return null;
    }

    public ArrayList<OrdenCompra> consultarOrdenesCompraPorProveedor(Proveedor proveedor) {
        ArrayList<OrdenCompra> resultado = new ArrayList<>();

        for (OrdenCompra ordenCompra : ordenesCompra) {
            if (ordenCompra.getProveedor().getCuit() == proveedor.getCuit()) {
                resultado.add(ordenCompra);
            }
        }

        return resultado;
    }

    public ArrayList<OrdenCompra> consultarOrdenesCompraPorEstado(EstadoOrdenCompra estado) {
        ArrayList<OrdenCompra> resultado = new ArrayList<>();

        for (OrdenCompra ordenCompra : ordenesCompra) {
            if (ordenCompra.getEstado() == estado) {
                resultado.add(ordenCompra);
            }
        }

        return resultado;
    }

    public ArrayList<OrdenCompra> consultarOrdenesCompraPorRubro(Rubro rubro) {
        ArrayList<OrdenCompra> resultado = new ArrayList<>();

        for (OrdenCompra ordenCompra : ordenesCompra) {
            for (LineaOrdenCompra linea : ordenCompra.getLineas()) {
                if (linea.getItem().getRubro().getNombre().equals(rubro.getNombre())) {
                    resultado.add(ordenCompra);
                    break;
                }
            }
        }

        return resultado;
    }

    public ArrayList<OrdenCompra> getOrdenesCompra() {
        return ordenesCompra;
    }
}