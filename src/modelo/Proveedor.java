package modelo;

import modelo.enums.CondicionImpositiva;
import modelo.enums.TipoImpuesto;

import java.util.ArrayList;
import java.util.Date;

public class Proveedor {
    private int cuit;
    private String razonSocial;
    private String nombreComercial;
    private String domicilio;
    private int telefono;
    private String email;
    private CondicionImpositiva condicionImpositiva;
    private int nroInscripcionFiscal;
    private Date inicioActividades;
    private double limiteDeudaAutorizado;

    private ArrayList<Rubro> rubros;
    private ArrayList<CertificadoExclusion> certificados;

    public Proveedor(
            int cuit,
            String razonSocial,
            String nombreComercial,
            String domicilio,
            int telefono,
            String email,
            CondicionImpositiva condicionImpositiva,
            int nroInscripcionFiscal,
            Date inicioActividades,
            double limiteDeudaAutorizado
    ) {
        this.cuit = cuit;
        this.razonSocial = razonSocial;
        this.nombreComercial = nombreComercial;
        this.domicilio = domicilio;
        this.telefono = telefono;
        this.email = email;
        this.condicionImpositiva = condicionImpositiva;
        this.nroInscripcionFiscal = nroInscripcionFiscal;
        this.inicioActividades = inicioActividades;
        this.limiteDeudaAutorizado = limiteDeudaAutorizado;

        this.rubros = new ArrayList<>();
        this.certificados = new ArrayList<>();
    }

    public void agregarRubro(Rubro rubro) {
        rubros.add(rubro);
    }

    public void agregarCertificado(CertificadoExclusion certificado) {
        certificados.add(certificado);
    }

    public boolean tieneCertificadoVigente(TipoImpuesto tipoImpuesto) {
        Date hoy = new Date();

        for (CertificadoExclusion certificado : certificados) {
            if (certificado.getTipoImpuesto() == tipoImpuesto && certificado.estaVigente(hoy)) {
                return true;
            }
        }

        return false;
    }

    public int getCuit() {
        return cuit;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public double getLimiteDeudaAutorizado() {
        return limiteDeudaAutorizado;
    }

    public CondicionImpositiva getCondicionImpositiva() {
        return condicionImpositiva;
    }

    public ArrayList<Rubro> getRubros() {
        return rubros;
    }
}