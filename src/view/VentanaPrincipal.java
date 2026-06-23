package view;

import controller.ConsultasController;
import controller.DocumentoComercialController;
import controller.OrdenCompraController;
import controller.OrdenPagoController;

import modelo.*;
import modelo.enums.*;

import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class VentanaPrincipal extends JFrame {
    private JTextArea areaResultado;

    private SistemaGestionCompras sistema;

    private OrdenCompraController ordenCompraController;
    private DocumentoComercialController documentoController;
    private OrdenPagoController ordenPagoController;
    private ConsultasController consultasController;

    private Rubro rubro;
    private Proveedor proveedor;
    private Proveedor proveedor2;
    private Producto producto;
    private Usuario supervisor;

    private OrdenCompra oc;
    private OrdenCompra ocComparacion;
    private Factura factura;
    private OrdenPago op;

    private boolean datosCargados;
    private boolean ordenCompraCreada;
    private boolean facturaRegistrada;
    private boolean ordenPagoGenerada;

    public VentanaPrincipal() {
        sistema = SistemaGestionCompras.getInstancia();

        ordenCompraController = new OrdenCompraController();
        documentoController = new DocumentoComercialController();
        ordenPagoController = new OrdenPagoController();
        consultasController = new ConsultasController();

        datosCargados = false;
        ordenCompraCreada = false;
        facturaRegistrada = false;
        ordenPagoGenerada = false;

        setTitle("Sistema de Gestion de Compras - Sanatorio");
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BorderLayout());

        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(2, 4, 5, 5));

        JButton btnCargarDatos = new JButton("Cargar datos de prueba");
        JButton btnCrearOC = new JButton("Crear y validar OC");
        JButton btnRegistrarFactura = new JButton("Registrar factura");
        JButton btnGenerarOP = new JButton("Generar OP");
        JButton btnVerConsultas = new JButton("Ver consultas");
        JButton btnReportesObligatorios = new JButton("Ver reportes obligatorios");
        JButton btnProbarOCLimite = new JButton("Probar OC supera limite");
        JButton btnLimpiar = new JButton("Limpiar");

        panelBotones.add(btnCargarDatos);
        panelBotones.add(btnCrearOC);
        panelBotones.add(btnRegistrarFactura);
        panelBotones.add(btnGenerarOP);
        panelBotones.add(btnVerConsultas);
        panelBotones.add(btnReportesObligatorios);
        panelBotones.add(btnProbarOCLimite);
        panelBotones.add(btnLimpiar);

        areaResultado = new JTextArea();
        areaResultado.setEditable(false);

        JScrollPane scroll = new JScrollPane(areaResultado);

        panelPrincipal.add(panelBotones, BorderLayout.NORTH);
        panelPrincipal.add(scroll, BorderLayout.CENTER);

        add(panelPrincipal);

        btnCargarDatos.addActionListener(e -> cargarDatosBase());
        btnCrearOC.addActionListener(e -> crearYConfirmarOrdenCompra());
        btnRegistrarFactura.addActionListener(e -> registrarFactura());
        btnGenerarOP.addActionListener(e -> generarOrdenPago());
        btnVerConsultas.addActionListener(e -> mostrarConsultas());
        btnReportesObligatorios.addActionListener(e -> mostrarReportesObligatorios());
        btnProbarOCLimite.addActionListener(e -> probarOCSuperaLimite());
        btnLimpiar.addActionListener(e -> areaResultado.setText(""));
    }

    private void cargarDatosBase() {
        if (datosCargados) {
            areaResultado.append("Los datos de prueba ya fueron cargados.\n\n");
            return;
        }

        rubro = new Rubro(
                "Insumos descartables",
                "Material descartable medico"
        );

        proveedor = new Proveedor(
                12345678,
                "Proveedor Medico SA",
                "MedicoSur",
                "Av. Siempre Viva 123",
                123456789,
                "contacto@proveedor.com",
                CondicionImpositiva.RESPONSABLE_INSCRIPTO,
                555,
                new Date(),
                100000
        );

        proveedor.agregarRubro(rubro);

        proveedor2 = new Proveedor(
                20999888,
                "Insumos del Norte SRL",
                "InsumosNorte",
                "Av. Belgrano 900",
                111222333,
                "ventas@insumosnorte.com",
                CondicionImpositiva.RESPONSABLE_INSCRIPTO,
                888,
                new Date(),
                80000
        );

        proveedor2.agregarRubro(rubro);

        producto = new Producto(
                1,
                "Jeringa descartable",
                rubro,
                "Unidad",
                100,
                21,
                "L001",
                new Date(),
                500
        );

        supervisor = new Usuario(
                "Supervisor General",
                "supervisor@sanatorio.com",
                "1234",
                RolUsuario.SUPERVISOR
        );

        sistema.agregarRubro(rubro);
        sistema.agregarProveedor(proveedor);
        sistema.agregarProveedor(proveedor2);
        sistema.agregarItem(producto);
        sistema.agregarUsuario(supervisor);

        datosCargados = true;

        areaResultado.append("===== DATOS DE PRUEBA CARGADOS =====\n");
        areaResultado.append("Rubro: " + rubro.getNombre() + "\n");
        areaResultado.append("Proveedor 1: " + proveedor.getRazonSocial() + "\n");
        areaResultado.append("Proveedor 2: " + proveedor2.getRazonSocial() + "\n");
        areaResultado.append("Producto: " + producto.getDescripcion() + "\n");
        areaResultado.append("Usuario supervisor: " + supervisor.getNombre() + "\n\n");
    }

    private void crearYConfirmarOrdenCompra() {
        if (!datosCargados) {
            areaResultado.append("Primero debe cargar los datos de prueba.\n\n");
            return;
        }

        if (ordenCompraCreada) {
            areaResultado.append("La Orden de Compra ya fue creada.\n\n");
            return;
        }

        oc = ordenCompraController.crearOrdenCompra(
                1,
                proveedor
        );

        ordenCompraController.agregarLinea(
                oc,
                producto,
                100,
                120
        );

        ordenCompraController.confirmarOrdenCompra(oc);

        ocComparacion = ordenCompraController.crearOrdenCompra(
                2,
                proveedor2
        );

        ordenCompraController.agregarLinea(
                ocComparacion,
                producto,
                100,
                135
        );

        ordenCompraController.confirmarOrdenCompra(ocComparacion);

        ordenCompraCreada = true;

        areaResultado.append("===== ORDEN DE COMPRA PRINCIPAL =====\n");
        areaResultado.append("Numero OC: " + oc.getNumero() + "\n");
        areaResultado.append("Proveedor: " + oc.getProveedor().getRazonSocial() + "\n");
        areaResultado.append("Item: " + producto.getDescripcion() + "\n");
        areaResultado.append("Cantidad: 100\n");
        areaResultado.append("Precio unitario acordado: 120.0\n");
        areaResultado.append("Total OC: " + oc.calcularTotalBruto() + "\n");
        areaResultado.append("Limite deuda proveedor: " + proveedor.getLimiteDeudaAutorizado() + "\n");
        areaResultado.append("Estado OC: " + oc.getEstado() + "\n\n");

        areaResultado.append("===== OC DE COMPARACION DE PRECIOS =====\n");
        areaResultado.append("Numero OC: " + ocComparacion.getNumero() + "\n");
        areaResultado.append("Proveedor: " + ocComparacion.getProveedor().getRazonSocial() + "\n");
        areaResultado.append("Item: " + producto.getDescripcion() + "\n");
        areaResultado.append("Cantidad: 100\n");
        areaResultado.append("Precio unitario acordado: 135.0\n");
        areaResultado.append("Total OC: " + ocComparacion.calcularTotalBruto() + "\n");
        areaResultado.append("Estado OC: " + ocComparacion.getEstado() + "\n\n");
    }

    private void registrarFactura() {
        if (!ordenCompraCreada) {
            areaResultado.append("Primero debe crear y validar la Orden de Compra.\n\n");
            return;
        }

        if (facturaRegistrada) {
            areaResultado.append("La factura ya fue registrada.\n\n");
            return;
        }

        factura = documentoController.crearFactura(
                1001,
                proveedor
        );

        documentoController.agregarLineaFactura(
                factura,
                producto,
                100,
                120
        );

        boolean facturaValida = documentoController.validarYRegistrarFactura(
                factura,
                oc,
                null
        );

        facturaRegistrada = true;

        areaResultado.append("===== FACTURA =====\n");
        areaResultado.append("Numero factura: " + factura.getNroDocumento() + "\n");
        areaResultado.append("Proveedor: " + factura.getProveedor().getRazonSocial() + "\n");
        areaResultado.append("Factura validada contra OC: " + facturaValida + "\n");
        areaResultado.append("Estado factura: " + factura.getEstado() + "\n");
        areaResultado.append("Importe factura: " + factura.getImporteTotal() + "\n");
        areaResultado.append("Impacto en cuenta corriente: " + factura.impactoEnCuentaCorriente() + "\n");
        areaResultado.append("Deuda actual proveedor: " + sistema.calcularDeudaActualProveedor(proveedor) + "\n\n");
    }

    private void generarOrdenPago() {
        if (!facturaRegistrada) {
            areaResultado.append("Primero debe registrar la factura.\n\n");
            return;
        }

        if (ordenPagoGenerada) {
            areaResultado.append("La Orden de Pago ya fue generada.\n\n");
            return;
        }

        op = ordenPagoController.crearOrdenPago(
                1,
                proveedor
        );

        ordenPagoController.agregarDocumentoAPagar(
                op,
                factura,
                factura.getImporteTotal()
        );

        ordenPagoController.calcularRetenciones(op);

        double netoAPagar = op.calcularTotalNetoAPagar();

        ordenPagoController.agregarMedioPago(
                op,
                new Efectivo(netoAPagar)
        );

        boolean opEmitida = ordenPagoController.emitirOrdenPago(op);

        ordenPagoGenerada = true;

        areaResultado.append("===== ORDEN DE PAGO =====\n");
        areaResultado.append("Numero OP: " + op.getNumero() + "\n");
        areaResultado.append("Proveedor: " + op.getProveedor().getRazonSocial() + "\n");
        areaResultado.append("OP emitida: " + opEmitida + "\n");
        areaResultado.append("Total bruto pagado: " + op.calcularTotalBrutoPagado() + "\n");
        areaResultado.append("Total retenido: " + op.calcularTotalRetenido() + "\n");
        areaResultado.append("Total neto a pagar: " + op.calcularTotalNetoAPagar() + "\n");
        areaResultado.append("Total medios de pago: " + op.calcularTotalMediosPago() + "\n");
        areaResultado.append("Estado factura luego del pago: " + factura.getEstado() + "\n");
        areaResultado.append("Deuda actual proveedor luego del pago: " + sistema.calcularDeudaActualProveedor(proveedor) + "\n\n");
    }

    private void mostrarConsultas() {
        if (!datosCargados) {
            areaResultado.append("Primero debe cargar los datos de prueba.\n\n");
            return;
        }

        areaResultado.append("===== CONSULTAS Y REPORTES SIMPLES =====\n");
        areaResultado.append("Cantidad proveedores: " + sistema.getProveedores().size() + "\n");
        areaResultado.append("Cantidad items: " + sistema.getItems().size() + "\n");
        areaResultado.append("Cantidad OC: " + sistema.getOrdenesCompra().size() + "\n");
        areaResultado.append("Cantidad documentos: " + sistema.getDocumentosComerciales().size() + "\n");
        areaResultado.append("Cantidad OP: " + sistema.getOrdenesPago().size() + "\n");

        areaResultado.append("Documentos pendientes: " + consultasController.consultarCantidadDocumentosPendientes() + "\n");
        areaResultado.append("Deuda actual proveedor: " + consultasController.consultarDeudaActualProveedor(proveedor) + "\n");
        areaResultado.append("Documentos del proveedor: " + consultasController.consultarDocumentosPorProveedor(proveedor).size() + "\n");
        areaResultado.append("OC del proveedor: " + consultasController.consultarOrdenesCompraPorProveedor(proveedor).size() + "\n");
        areaResultado.append("OC emitidas: " + consultasController.consultarOrdenesCompraPorEstado(EstadoOrdenCompra.EMITIDA).size() + "\n");
        areaResultado.append("Pagos del proveedor: " + consultasController.consultarPagosPorProveedor(proveedor).size() + "\n");

        areaResultado.append("Total retenido IVA: " + consultasController.consultarTotalRetenidoPorImpuesto(TipoImpuesto.IVA) + "\n");
        areaResultado.append("Total retenido Ganancias: " + consultasController.consultarTotalRetenidoPorImpuesto(TipoImpuesto.GANANCIAS) + "\n");
        areaResultado.append("Total retenido IIBB: " + consultasController.consultarTotalRetenidoPorImpuesto(TipoImpuesto.IIBB) + "\n\n");
    }

    private void mostrarReportesObligatorios() {
        if (!datosCargados) {
            areaResultado.append("Primero debe cargar los datos de prueba.\n\n");
            return;
        }

        Date desde = new Date(System.currentTimeMillis() - (24L * 60 * 60 * 1000));
        Date hasta = new Date();

        areaResultado.append("===== REPORTES OBLIGATORIOS =====\n\n");

        areaResultado.append("1. TRAZABILIDAD DOCUMENTAL\n");
        areaResultado.append("Total documentos recibidos hoy: "
                + consultasController.consultarTotalDocumentosPorDia(new Date()) + "\n");
        areaResultado.append("Total documentos recibidos en periodo: "
                + consultasController.consultarTotalDocumentosPorPeriodo(desde, hasta) + "\n");

        if (proveedor != null) {
            areaResultado.append(consultasController.generarReporteDocumentosPorProveedor(proveedor));
        }

        areaResultado.append("\n2. GESTION DE DEUDA / CUENTA CORRIENTE\n");

        if (proveedor != null) {
            areaResultado.append(consultasController.generarCuentaCorrienteProveedor(proveedor));
            areaResultado.append(consultasController.generarReporteDeudaDetalladaProveedor(proveedor));
        }

        areaResultado.append("Documentos pendientes con antiguedad mayor o igual a 0 dias: "
                + consultasController.consultarDocumentosPendientesPorAntiguedad(0).size() + "\n");

        areaResultado.append("\n3. SEGUIMIENTO DE COMPRAS Y PAGOS\n");
        areaResultado.append("OC emitidas: "
                + consultasController.consultarOrdenesCompraPorEstado(EstadoOrdenCompra.EMITIDA).size() + "\n");

        if (rubro != null) {
            areaResultado.append("OC por rubro " + rubro.getNombre() + ": "
                    + consultasController.consultarOrdenesCompraPorRubro(rubro).size() + "\n");
        }

        if (proveedor != null) {
            areaResultado.append("OC por proveedor: "
                    + consultasController.consultarOrdenesCompraPorProveedor(proveedor).size() + "\n");
            areaResultado.append("Pagos por proveedor: "
                    + consultasController.consultarPagosPorProveedor(proveedor).size() + "\n");
        }

        areaResultado.append("Pagos por periodo: "
                + consultasController.consultarPagosPorPeriodo(desde, hasta).size() + "\n");
        areaResultado.append("Pagos por medio Efectivo: "
                + consultasController.consultarPagosPorMedioPago("Efectivo").size() + "\n");

        areaResultado.append("\n4. ANALISIS DE COSTOS Y PRECIOS\n");

        if (producto != null) {
            areaResultado.append(consultasController.generarReporteComparacionPreciosItem(producto));
        }

        areaResultado.append("\n5. REPORTES FISCALES\n");
        areaResultado.append("Total retenido IVA en periodo: "
                + consultasController.consultarTotalRetenidoPorImpuestoYPeriodo(TipoImpuesto.IVA, desde, hasta) + "\n");
        areaResultado.append("Total retenido Ganancias en periodo: "
                + consultasController.consultarTotalRetenidoPorImpuestoYPeriodo(TipoImpuesto.GANANCIAS, desde, hasta) + "\n");
        areaResultado.append("Total retenido IIBB en periodo: "
                + consultasController.consultarTotalRetenidoPorImpuestoYPeriodo(TipoImpuesto.IIBB, desde, hasta) + "\n");

        areaResultado.append("\nLibro IVA Compras:\n");
        areaResultado.append(consultasController.generarLibroIVACompras());

        areaResultado.append("\n");
    }

    private void probarOCSuperaLimite() {
        Rubro rubroPrueba = new Rubro(
                "Tecnologia medica",
                "Equipamiento medico de alto costo"
        );

        Proveedor proveedorPrueba = new Proveedor(
                87654321,
                "Proveedor Limite Bajo SA",
                "LimiteBajo",
                "Calle Falsa 456",
                987654321,
                "limite@proveedor.com",
                CondicionImpositiva.RESPONSABLE_INSCRIPTO,
                777,
                new Date(),
                1000
        );

        proveedorPrueba.agregarRubro(rubroPrueba);

        Producto productoCaro = new Producto(
                99,
                "Equipo medico costoso",
                rubroPrueba,
                "Unidad",
                5000,
                21,
                "L999",
                new Date(),
                10
        );

        sistema.agregarRubro(rubroPrueba);
        sistema.agregarProveedor(proveedorPrueba);
        sistema.agregarItem(productoCaro);

        OrdenCompra ocPrueba = ordenCompraController.crearOrdenCompra(
                99,
                proveedorPrueba
        );

        ordenCompraController.agregarLinea(
                ocPrueba,
                productoCaro,
                1,
                5000
        );

        ordenCompraController.confirmarOrdenCompra(ocPrueba);

        areaResultado.append("===== PRUEBA OC SUPERA LIMITE =====\n");
        areaResultado.append("Proveedor: " + proveedorPrueba.getRazonSocial() + "\n");
        areaResultado.append("Limite autorizado: " + proveedorPrueba.getLimiteDeudaAutorizado() + "\n");
        areaResultado.append("Total OC: " + ocPrueba.calcularTotalBruto() + "\n");
        areaResultado.append("Estado esperado: PENDIENTE_APROBACION\n");
        areaResultado.append("Estado obtenido: " + ocPrueba.getEstado() + "\n");

        if (ocPrueba.getEstado() == EstadoOrdenCompra.PENDIENTE_APROBACION) {
            areaResultado.append("Resultado: OK, la regla funciona correctamente.\n\n");
        } else {
            areaResultado.append("Resultado: ERROR, revisar confirmacion de OC.\n\n");
        }
    }
}