package view;

public class EstadoVista {
    private int proximoNumeroOC;
    private int proximoNumeroFactura;
    private int proximoNumeroOP;

    public EstadoVista() {
        this.proximoNumeroOC = 1;
        this.proximoNumeroFactura = 1001;
        this.proximoNumeroOP = 1;
    }

    public int obtenerYAvanzarNumeroOC() {
        int numeroActual = proximoNumeroOC;
        proximoNumeroOC++;
        return numeroActual;
    }

    public int obtenerYAvanzarNumeroOP() {
        int numeroActual = proximoNumeroOP;
        proximoNumeroOP++;
        return numeroActual;
    }

    public int getProximoNumeroFactura() {
        return proximoNumeroFactura;
    }

    public void actualizarProximoNumeroFactura(int nroFactura) {
        if (nroFactura >= proximoNumeroFactura) {
            proximoNumeroFactura = nroFactura + 1;
        }
    }
}