package controller;

import modelo.CertificadoExclusion;
import modelo.Proveedor;
import modelo.Rubro;

import java.util.ArrayList;

public class ProveedorController {
    private ArrayList<Proveedor> proveedores;

    public ProveedorController() {
        this.proveedores = new ArrayList<>();
    }

    public void agregarProveedor(Proveedor proveedor) {
        proveedores.add(proveedor);
    }

    public void agregarRubroAProveedor(Proveedor proveedor, Rubro rubro) {
        proveedor.agregarRubro(rubro);
    }

    public void agregarCertificadoAProveedor(Proveedor proveedor, CertificadoExclusion certificado) {
        proveedor.agregarCertificado(certificado);
    }

    public Proveedor buscarProveedorPorCuit(int cuit) {
        for (Proveedor proveedor : proveedores) {
            if (proveedor.getCuit() == cuit) {
                return proveedor;
            }
        }

        return null;
    }

    public ArrayList<Proveedor> getProveedores() {
        return proveedores;
    }
}