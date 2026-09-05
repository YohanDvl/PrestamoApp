package prestamo.servidor.vistas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.InetAddress;
import prestamo.servidor.ServidorTcp;

public class VentanaPrincipal extends JFrame {
    private JButton btnIniciar;
    private JButton btnLimpiar;
    private JTextArea cajaLog;
    private JTextField campoIP;
    private JTextField campoPuerto;
    private JLabel txtEstado;
    private ServidorTcp s;

    public VentanaPrincipal() {
        initComponents();
    }

    private void initComponents() {
        setTitle("Servidor Prestamo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel panelConexion = new JPanel(new GridLayout(4, 2, 10, 10));
        panelConexion.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        panelConexion.add(new JLabel("DIRECCION IP:"));
        campoIP = new JTextField();
        campoIP.setEditable(false);
        panelConexion.add(campoIP);

        panelConexion.add(new JLabel("PUERTO DE RED:"));
        campoPuerto = new JTextField("9007");
        panelConexion.add(campoPuerto);

        panelConexion.add(new JLabel("ESTADO:"));
        txtEstado = new JLabel("DETENIDO");
        txtEstado.setForeground(Color.RED);
        panelConexion.add(txtEstado);

        btnIniciar = new JButton("INICIAR");
        btnIniciar.setForeground(new Color(0, 153, 51));
        panelConexion.add(btnIniciar);

        tabbedPane.addTab("CONEXION", panelConexion);

        JPanel panelLog = new JPanel(new BorderLayout(5, 5));
        cajaLog = new JTextArea();
        cajaLog.setEditable(false);
        panelLog.add(new JScrollPane(cajaLog), BorderLayout.CENTER);
        
        btnLimpiar = new JButton("LIMPIAR");
        panelLog.add(btnLimpiar, BorderLayout.SOUTH);

        tabbedPane.addTab("LOG DE CONEXIONES", panelLog);

        add(new JLabel("SERVIDOR PRESTAMO", SwingConstants.CENTER), BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                try {
                    campoIP.setText(InetAddress.getLocalHost().getHostAddress());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(VentanaPrincipal.this, "Falla en la conexion");
                }
            }
        });

        btnIniciar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (btnIniciar.getText().equalsIgnoreCase("INICIAR")) {
                    int puerto = Integer.parseInt(campoPuerto.getText());
                    s = new ServidorTcp(puerto, VentanaPrincipal.this);
                    s.start();
                } else {
                    if (s != null) {
                        s.detenerServicio();
                    }
                }
            }
        });

        btnLimpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cajaLog.setText("");
            }
        });
    }

    public JButton getBtnIniciar() { return btnIniciar; }
    public JLabel getTxtEstado() { return txtEstado; }
    public JTextArea getCajaLog() { return cajaLog; }
}
