package modelo;

import modelo.enums.EstadoDocumentoComercial;

import java.util.ArrayList;

public class SistemaGestionCompras {
    private static SistemaGestionCompras instancia;

    private ArrayList<Proveedor> proveedores;
    private ArrayList<Rubro> rubros;
    private ArrayList<Item> items;
    private ArrayList<OrdenCompra> ordenesCompra;
    private ArrayList<DocumentoComercial> documentosComerciales;
    private ArrayList<OrdenPago> ordenesPago;
    private ArrayList<Usuario> usuarios;

    private SistemaGestionCompras() {
        this.proveedores = new ArrayList<>();
        this.rubros = new ArrayList<>();
        this.items = new ArrayList<>();
        this.ordenesCompra = new ArrayList<>();
        this.documentosComerciales = new ArrayList<>();
        this.ordenesPago = new ArrayList<>();
        this.usuarios = new ArrayList<>();
    }

    public static SistemaGestionCompras getInstancia() {
        if (instancia == null) {
            instancia = new SistemaGestionCompras();
        }

        return instancia;
    }

    public void agregarProveedor(Proveedor proveedor) {
        proveedores.add(proveedor);
    }

    public void agregarRubro(Rubro rubro) {
        rubros.add(rubro);
    }

    public void agregarItem(Item item) {
        items.add(item);
    }

    public void agregarUsuario(Usuario usuario) {
        usuarios.add(usuario);
    }

    public void agregarOrdenCompra(OrdenCompra ordenCompra) {
        ordenesCompra.add(ordenCompra);
    }

    public void agregarDocumentoComercial(DocumentoComercial documento) {
        documentosComerciales.add(documento);
    }

    public void agregarOrdenPago(OrdenPago ordenPago) {
        ordenesPago.add(ordenPago);
    }

    public Proveedor buscarProveedorPorCuit(int cuit) {
        for (Proveedor proveedor : proveedores) {
            if (proveedor.getCuit() == cuit) {
                return proveedor;
            }
        }

        return null;
    }

    public Item buscarItemPorCodigo(int codigo) {
        for (Item item : items) {
            if (item.getCodigo() == codigo) {
                return item;
            }
        }

        return null;
    }

    public OrdenCompra buscarOrdenCompraPorNumero(int numero) {
        for (OrdenCompra ordenCompra : ordenesCompra) {
            if (ordenCompra.getNumero() == numero) {
                return ordenCompra;
            }
        }

        return null;
    }

    public DocumentoComercial buscarDocumentoPorNumero(int nroDocumento) {
        for (DocumentoComercial documento : documentosComerciales) {
            if (documento.getNroDocumento() == nroDocumento) {
                return documento;
            }
        }

        return null;
    }

    public double calcularDeudaActualProveedor(Proveedor proveedor) {
        double deuda = 0;

        for (DocumentoComercial documento : documentosComerciales) {
            if (documento.getProveedor().getCuit() == proveedor.getCuit()) {
                if (documento.getEstado() != EstadoDocumentoComercial.CANCELADO) {
                    deuda = deuda + documento.impactoEnCuentaCorriente();
                    deuda = deuda - documento.getMontoPagado();
                }
            }
        }

        return deuda;
    }

    public void confirmarOrdenCompra(OrdenCompra ordenCompra) {
        double deudaActual = calcularDeudaActualProveedor(ordenCompra.getProveedor());
        ordenCompra.confirmar(deudaActual);
    }

    public ArrayList<DocumentoComercial> obtenerDocumentosPendientes() {
        ArrayList<DocumentoComercial> pendientes = new ArrayList<>();

        for (DocumentoComercial documento : documentosComerciales) {
            if (documento.getEstado() != EstadoDocumentoComercial.CANCELADO) {
                pendientes.add(documento);
            }
        }

        return pendientes;
    }

    public ArrayList<Proveedor> getProveedores() {
        return proveedores;
    }

    public ArrayList<Rubro> getRubros() {
        return rubros;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public ArrayList<OrdenCompra> getOrdenesCompra() {
        return ordenesCompra;
    }

    public ArrayList<DocumentoComercial> getDocumentosComerciales() {
        return documentosComerciales;
    }

    public ArrayList<OrdenPago> getOrdenesPago() {
        return ordenesPago;
    }

    public ArrayList<Usuario> getUsuarios() {
        return usuarios;
    }
}