package view.acciones;

import controller.DocumentoComercialController;
import controller.OrdenPagoController;
import modelo.*;
import view.EstadoVista;
import view.util.DialogoSwing;
import view.util.SelectorSwing;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class GenerarOrdenPagoVista {
    private final Component padre;
    private final JTextArea areaResultado;
    private final DocumentoComercialController documentoController;
    private final OrdenPagoController ordenPagoController;
    private final EstadoVista estadoVista;

    public GenerarOrdenPagoVista(
            Component padre,
            JTextArea areaResultado,
            DocumentoComercialController documentoController,
            OrdenPagoController ordenPagoController,
            EstadoVista estadoVista
    ) {
        this.padre = padre;
        this.areaResultado = areaResultado;
        this.documentoController = documentoController;
        this.ordenPagoController = ordenPagoController;
        this.estadoVista = estadoVista;
    }

    public void ejecutar() {
        DocumentoComercial documento = SelectorSwing.seleccionarDocumentoPendienteParaPago(
                padre,
                documentoController
        );

        if (documento == null) {
            return;
        }

        double saldoPendiente = documento.getImporteTotal() - documento.getMontoPagado();

        if (saldoPendiente <= 0) {
            DialogoSwing.mostrarError(padre, "El documento no tiene saldo pendiente.");
            return;
        }

        Double montoAplicado = DialogoSwing.pedirDouble(
                padre,
                "Ingrese monto a pagar. Saldo pendiente: " + saldoPendiente
        );

        if (montoAplicado == null) {
            return;
        }

        if (montoAplicado <= 0) {
            DialogoSwing.mostrarError(padre, "El monto aplicado debe ser mayor a cero.");
            return;
        }

        if (montoAplicado > saldoPendiente) {
            DialogoSwing.mostrarError(padre, "El monto aplicado no puede superar el saldo pendiente.");
            return;
        }

        OrdenPago ordenPago = ordenPagoController.crearOrdenPago(
                estadoVista.obtenerYAvanzarNumeroOP(),
                documento.getProveedor()
        );

        try {
            ordenPagoController.agregarDocumentoAPagar(
                    ordenPago,
                    documento,
                    montoAplicado
            );
        } catch (IllegalArgumentException ex) {
            DialogoSwing.mostrarError(padre, ex.getMessage());
            return;
        }

        ordenPagoController.calcularRetenciones(ordenPago);

        double netoAPagar = ordenPago.calcularTotalNetoAPagar();

        MedioPago medioPago = crearMedioPago(netoAPagar);
        if (medioPago == null) {
            return;
        }

        ordenPagoController.agregarMedioPago(
                ordenPago,
                medioPago
        );

        boolean opEmitida = ordenPagoController.emitirOrdenPago(ordenPago);

        areaResultado.append("===== ORDEN DE PAGO GENERADA =====\n");
        areaResultado.append("Numero OP: " + ordenPago.getNumero() + "\n");
        areaResultado.append("Proveedor: " + ordenPago.getProveedor().getRazonSocial() + "\n");
        areaResultado.append("Documento pagado: " + documento.getClass().getSimpleName()
                + " Nro " + documento.getNroDocumento() + "\n");
        areaResultado.append("OP emitida: " + opEmitida + "\n");
        areaResultado.append("Total bruto pagado: " + ordenPago.calcularTotalBrutoPagado() + "\n");
        areaResultado.append("Total retenido: " + ordenPago.calcularTotalRetenido() + "\n");
        areaResultado.append("Total neto a pagar: " + ordenPago.calcularTotalNetoAPagar() + "\n");
        areaResultado.append("Medio de pago: " + medioPago.getDescripcion() + "\n");
        areaResultado.append("Estado documento luego del pago: " + documento.getEstado() + "\n");
        areaResultado.append("Deuda actual proveedor: "
                + documentoController.calcularDeudaActualProveedor(documento.getProveedor()) + "\n\n");
    }

    private MedioPago crearMedioPago(double importe) {
        String[] opciones = {
                "Efectivo",
                "Transferencia bancaria",
                "Cheque propio",
                "Cheque tercero"
        };

        String seleccion = (String) JOptionPane.showInputDialog(
                padre,
                "Seleccione medio de pago.\nImporte neto a pagar: " + importe,
                "Medio de pago",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opciones,
                opciones[0]
        );

        if (seleccion == null) {
            return null;
        }

        if (seleccion.equals("Efectivo")) {
            return new Efectivo(importe);
        }

        if (seleccion.equals("Transferencia bancaria")) {
            Integer referencia = DialogoSwing.pedirEntero(padre, "Ingrese numero de referencia:");
            if (referencia == null) {
                return null;
            }

            String cuentaOrigen = DialogoSwing.pedirTextoObligatorio(padre, "Ingrese cuenta origen:");
            if (cuentaOrigen == null) {
                return null;
            }

            return new TransferenciaBancaria(importe, referencia, cuentaOrigen);
        }

        if (seleccion.equals("Cheque propio")) {
            Integer nroCheque = DialogoSwing.pedirEntero(padre, "Ingrese numero de cheque:");
            if (nroCheque == null) {
                return null;
            }

            String banco = DialogoSwing.pedirTextoObligatorio(padre, "Ingrese banco:");
            if (banco == null) {
                return null;
            }

            String firmante = DialogoSwing.pedirTextoObligatorio(padre, "Ingrese firmante:");
            if (firmante == null) {
                return null;
            }

            return new ChequePropio(
                    importe,
                    nroCheque,
                    banco,
                    new Date(),
                    new Date(),
                    firmante
            );
        }

        Integer nroCheque = DialogoSwing.pedirEntero(padre, "Ingrese numero de cheque:");
        if (nroCheque == null) {
            return null;
        }

        String banco = DialogoSwing.pedirTextoObligatorio(padre, "Ingrese banco:");
        if (banco == null) {
            return null;
        }

        String firmanteOriginal = DialogoSwing.pedirTextoObligatorio(padre, "Ingrese firmante original:");
        if (firmanteOriginal == null) {
            return null;
        }

        return new ChequeTercero(
                importe,
                nroCheque,
                banco,
                new Date(),
                new Date(),
                firmanteOriginal
        );
    }
}