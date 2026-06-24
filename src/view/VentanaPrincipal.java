package view;

import controller.*;
import view.acciones.*;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {
    private final JTextArea areaResultado;

    private final ProveedorController proveedorController;
    private final RubroController rubroController;
    private final ItemController itemController;
    private final UsuarioController usuarioController;

    private final OrdenCompraController ordenCompraController;
    private final DocumentoComercialController documentoController;
    private final OrdenPagoController ordenPagoController;
    private final ConsultasController consultasController;

    private final EstadoVista estadoVista;

    public VentanaPrincipal() {
        proveedorController = new ProveedorController();
        rubroController = new RubroController();
        itemController = new ItemController();
        usuarioController = new UsuarioController();

        documentoController = new DocumentoComercialController();
        ordenCompraController = new OrdenCompraController(documentoController);
        ordenPagoController = new OrdenPagoController();

        consultasController = new ConsultasController(
                ordenCompraController,
                documentoController,
                ordenPagoController
        );

        estadoVista = new EstadoVista();

        setTitle("Sistema de Gestion de Compras - Sanatorio");
        setSize(1150, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BorderLayout());

        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(2, 4, 5, 5));

        JButton btnAltaRubro = new JButton("Alta rubro");
        JButton btnAltaProveedor = new JButton("Alta proveedor");
        JButton btnAltaItem = new JButton("Alta item");
        JButton btnCrearOC = new JButton("Crear OC");
        JButton btnRegistrarFactura = new JButton("Registrar factura");
        JButton btnGenerarOP = new JButton("Generar OP");
        JButton btnReportesObligatorios = new JButton("Ver reportes obligatorios");
        JButton btnLimpiar = new JButton("Limpiar");

        panelBotones.add(btnAltaRubro);
        panelBotones.add(btnAltaProveedor);
        panelBotones.add(btnAltaItem);
        panelBotones.add(btnCrearOC);
        panelBotones.add(btnRegistrarFactura);
        panelBotones.add(btnGenerarOP);
        panelBotones.add(btnReportesObligatorios);
        panelBotones.add(btnLimpiar);

        areaResultado = new JTextArea();
        areaResultado.setEditable(false);

        JScrollPane scroll = new JScrollPane(areaResultado);

        panelPrincipal.add(panelBotones, BorderLayout.NORTH);
        panelPrincipal.add(scroll, BorderLayout.CENTER);

        add(panelPrincipal);

        btnAltaRubro.addActionListener(event -> ejecutarAltaRubro());
        btnAltaProveedor.addActionListener(event -> ejecutarAltaProveedor());
        btnAltaItem.addActionListener(event -> ejecutarAltaItem());
        btnCrearOC.addActionListener(event -> ejecutarCrearOC());
        btnRegistrarFactura.addActionListener(event -> ejecutarRegistrarFactura());
        btnGenerarOP.addActionListener(event -> ejecutarGenerarOP());
        btnReportesObligatorios.addActionListener(event -> ejecutarReportes());
        btnLimpiar.addActionListener(event -> areaResultado.setText(""));
    }

    private void ejecutarAltaRubro() {
        new AltaRubroVista(
                this,
                areaResultado,
                rubroController
        ).ejecutar();
    }

    private void ejecutarAltaProveedor() {
        new AltaProveedorVista(
                this,
                areaResultado,
                proveedorController,
                rubroController
        ).ejecutar();
    }

    private void ejecutarAltaItem() {
        new AltaItemVista(
                this,
                areaResultado,
                itemController,
                rubroController
        ).ejecutar();
    }

    private void ejecutarCrearOC() {
        new CrearOrdenCompraVista(
                this,
                areaResultado,
                proveedorController,
                itemController,
                ordenCompraController,
                estadoVista
        ).ejecutar();
    }

    private void ejecutarRegistrarFactura() {
        new RegistrarFacturaVista(
                this,
                areaResultado,
                ordenCompraController,
                documentoController,
                usuarioController,
                estadoVista
        ).ejecutar();
    }

    private void ejecutarGenerarOP() {
        new GenerarOrdenPagoVista(
                this,
                areaResultado,
                documentoController,
                ordenPagoController,
                estadoVista
        ).ejecutar();
    }

    private void ejecutarReportes() {
        new ReportesVista(
                this,
                areaResultado,
                proveedorController,
                rubroController,
                itemController,
                consultasController
        ).ejecutar();
    }
}